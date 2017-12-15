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

    private static final String[] IGNOREABLES = new String[]{
            "script",
    };

    public static String format(String html) {
        html = removeComments(html);
        html = removeSpaces(html);
        html = stripBetweenElements(html);
        return html;
    }

    private static String removeComments(String html) {
        return html.replaceAll("<!--[\\s\\S]+?-->", "");
    }

    private static String removeSpaces(String html) {
        return html.replaceAll("\\s+", " ");
    }

    private static String stripBetweenElements(String html) {
        return html.replaceAll("(?<=>)\\s+?(?=<)", "");
    }

    public static String formatPretty(String html) {

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
        prettyFormat(lines, buf);
        return buf.toString();

    }

    private static void prettyFormat(String[] lines, StringBuffer buf) {

        int indent = 0;
        boolean ignore = false;

        for (int i = 0; i < lines.length; i++) {

            if (lines[i].startsWith("</")) {
                for (String ignoreable : IGNOREABLES) {
                    if (lines[i].startsWith("</" + ignoreable)) {
                        ignore = false;
                    }
                }
                indent--;
                for (int j = 0; j < indent; j++) {
                    buf.append("\t");
                }
                buf.append(lines[i]);
                buf.append("\n");
            } else if (isVoid(lines[i]) || isDocType(lines[i])) {
                for (int j = 0; j < indent; j++) {
                    buf.append("\t");
                }
                buf.append(lines[i]);
                buf.append("\n");
            } else if (lines[i].startsWith("<")) {
                for (int j = 0; j < indent; j++) {
                    buf.append("\t");
                }
                buf.append(lines[i]);
                for (String ignoreable : IGNOREABLES) {
                    if (lines[i].startsWith("<" + ignoreable)) {
                        ignore = true;
                    }
                }
                if (i + 1 < lines.length && lines[i + 1].startsWith("</")) {
                    buf.append(lines[i + 1]);
                    buf.append("\n");
                    i++;
                } else {
                    buf.append("\n");
                    indent++;
                }
            } else if (ignore) {
                for (int j = 0; j < indent; j++) {
                    buf.append("\t");
                }
                buf.append(lines[i]);
                buf.append("\n");
            } else {
                buf.delete(buf.length() - 1, buf.length());
                buf.append(lines[i]);
                if (i + 1 < lines.length && lines[i + 1].startsWith("</")) {
                    buf.append(lines[i + 1]);
                    buf.append("\n");
                    i++;
                    indent--;
                } else if (i + 1 < lines.length && lines[i + 1].startsWith("<")) {
                    buf.append("\n");
                }

            }

        }

    }

    private static boolean isDocType(String line) {
        return line.toLowerCase().startsWith("<!doctype".toLowerCase());
    }

    private static boolean isVoid(String line) {
        return Arrays.stream(VOIDS).anyMatch(el -> line.startsWith("<" + el));
    }

}
