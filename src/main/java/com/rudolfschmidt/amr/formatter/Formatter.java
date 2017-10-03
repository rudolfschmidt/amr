package com.rudolfschmidt.amr.formatter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

	private static final String[] VOIDS = new String[]{
			"area",
			"base",
			"br",
			"col",
			"command",
			"embed",
			"hr",
			"img",
			"input",
			"keygen",
			"link",
			"meta",
			"param",
			"source",
			"track",
			"wbr"
	};

	public static String format(String html) {

		html = html.replaceAll("[\r\n]+", ""); // remove line breaks
		html = html.replaceAll("(?<=>)\\s+(?=<)", ""); //remove spaces between elements
		html = html.replaceAll("<!--[\\s\\S]+?-->", ""); //remove html comments
		html = html.replaceAll("\\s+(?=>|[\\w-]+=(?<q>[\"']).*?\\k<q>)", " "); //remove spaces and line breaks inside element tag

		return html;

	}

	public static String formatPretty(String html) {

		html = format(html);

		Matcher matcher = Pattern.compile("<.+?>").matcher(html);
		StringBuffer buf = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buf, matcher.group().replace("\\", "\\\\").replace("$", "\\$") + "\n");
		}

		matcher = Pattern.compile(".+?(?=<|$)").matcher(buf.toString());
		buf = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(buf, matcher.group().replace("\\", "\\\\").replace("$", "\\$") + "\n");
		}

		final String[] lines = buf.toString().split("\n");

		buf = new StringBuffer();
		int indent = 0;
		int index = 0;

		prettyFormat(lines, index, indent, buf);

		return buf.toString();

	}

	private static void prettyFormat(String[] lines, int index, int indent, StringBuffer buf) {

		if (!(index < lines.length)) {
			return;
		}

		if (lines[index].startsWith("</")) {
			indent--;
			for (int i = 0; i < indent; i++) {
				buf.append("\t");
			}
			buf.append(lines[index]);
			buf.append("\n");
		} else if (isVoid(lines[index]) || isDocType(lines[index])) {
			for (int i = 0; i < indent; i++) {
				buf.append("\t");
			}
			buf.append(lines[index]);
			buf.append("\n");
		} else if (lines[index].startsWith("<")) {
			for (int i = 0; i < indent; i++) {
				buf.append("\t");
			}
			buf.append(lines[index]);
			if (index + 1 < lines.length && lines[index + 1].startsWith("</")) {
				buf.append(lines[index + 1]);
				buf.append("\n");
				index++;
			} else {
				buf.append("\n");
				indent++;
			}
		} else {
			buf.delete(buf.length() - 1, buf.length());
			buf.append(lines[index]);
			if (index + 1 < lines.length && lines[index + 1].startsWith("</")) {
				buf.append(lines[index + 1]);
				buf.append("\n");
				index++;
				indent--;
			} else if (index + 1 < lines.length && lines[index + 1].startsWith("<")) {
				buf.append("\n");
			}
		}

		prettyFormat(lines, ++index, indent, buf);

	}

	private static boolean isDocType(String line) {
		return line.toLowerCase().startsWith("<!doctype".toLowerCase());
	}

	private static boolean isVoid(String line) {
		return Arrays.stream(VOIDS).anyMatch(el -> line.startsWith("<" + el));
	}

}
