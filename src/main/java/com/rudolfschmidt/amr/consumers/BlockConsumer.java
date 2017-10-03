package com.rudolfschmidt.amr.consumers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BlockConsumer {

	static String handeBlock(String html, String block) {
		final String regex = "<block.*?name=(?<q>[\"'])(?<n>.*?)\\k<q>.*?>(?<c>[\\s\\S]*?)</block>";
		final Matcher matcher = Pattern.compile(regex).matcher(block);
		while (matcher.find()) {
			final String n = "<block.*?name=(?<q>[\"'])" + matcher.group("n") + "\\k<q>.*?>[\\s\\S]*?</block>";
			html = html.replaceAll(n, matcher.group("c").replace("\\", "\\\\").replace("$", "\\$"));
		}
		return html;
	}

}
