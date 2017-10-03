package com.rudolfschmidt.amr.consumers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rudolfschmidt.amr.Amr.parse;

public class IncludeConsumer {

	public static String handleInclude(String html, Path templatePath) throws IOException {
		final String regex = "<include.*?file=(?<q>[\"'])(?<f>.*?)\\k<q>.*?>[\\s\\S]*?</include>";
		final Matcher matcher = Pattern.compile(regex).matcher(html);
		while (matcher.find()) {
			final Path includePath = templatePath.getParent().resolve(matcher.group("f"));
			if (Files.isRegularFile(includePath)) {
				html = html.replace(matcher.group(), parse(includePath));
			}
		}
		return html;
	}

}
