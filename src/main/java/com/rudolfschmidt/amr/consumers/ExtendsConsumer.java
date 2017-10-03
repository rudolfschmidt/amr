package com.rudolfschmidt.amr.consumers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rudolfschmidt.amr.consumers.BlockConsumer.handeBlock;

public class ExtendsConsumer {

	public static String handleExtends(String html, Path templatePath) throws IOException {
		return handleExtends(html, templatePath, new ArrayDeque<>());
	}

	private static String handleExtends(String html, Path templatePath, Deque<String> blockList) throws IOException {
		final String regex = "<extends.*?file=(?<q>[\"'])(?<f>.*?)\\k<q>.*?>(?<c>[\\s\\S]*?)</extends>";
		final Matcher matcher = Pattern.compile(regex).matcher(html);

		if (matcher.find()) {
			blockList.add(matcher.group("c"));
			final Path includePath = templatePath.getParent().resolve(matcher.group("f"));
			if (Files.isRegularFile(includePath)) {
				return handleExtends(new String(Files.readAllBytes(includePath)), includePath, blockList);
			}
		} else {
			for (String block : blockList) {
				html = handeBlock(html, block);
			}
		}
		return html;
	}

}
