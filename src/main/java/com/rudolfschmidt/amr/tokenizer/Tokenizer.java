package com.rudolfschmidt.amr.tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	public static List<Token> tokenize(Path templatesFile) throws IOException {

		final String html = new String(Files.readAllBytes(templatesFile));

		final List<Token> tokens = new ArrayList<>();

		rootTokenize(html, tokens);

		return tokens;

	}

	private static void rootTokenize(String html, List<Token> tokens) {
		html = doctype(html, tokens);
		tokenize(html, tokens);
	}

	private static String doctype(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("<!doctype .+?>", Pattern.CASE_INSENSITIVE).matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.TEXT_VALUE, html.substring(matcher.start(), matcher.end())));
			return html.substring(matcher.end());
		}
		return html;
	}

	private static String tokenize(String html, List<Token> tokens) {
		html = removeComments(html,tokens);
		html = elementStart(html, tokens);
		html = elementEnd(html, tokens);
		html = textValue(html, tokens);
		return html;
	}

	private static String removeComments(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^\\s*(<!--[\\s\\S]*?-->)").matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.TEXT_VALUE, matcher.group(1)));
			return tokenize(html.substring(matcher.end()), tokens);
		}
		return html;
	}

	private static String elementStart(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^\\s*<(.+?)>", Pattern.DOTALL).matcher(html);

		if (matcher.find()) {
			final String name = matcher.group(1);
			if (!name.startsWith("/")) {
				elementName(name, tokens);
				return tokenize(html.substring(matcher.end()), tokens);
			}
		}
		return html;
	}

	private static void elementName(String element, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^[\\w-]+").matcher(element);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.ELEMENT_START, element.substring(matcher.start(), matcher.end())));
			attributes(element.substring(matcher.end()), tokens);
		}
	}

	private static String attributes(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^\\s+([\\w-]+)").matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.ATTRIBUTE_KEY, matcher.group(1)));
			return attributeValue(html.substring(matcher.end()), tokens);
		}
		return html;
	}

	private static String attributeValue(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^=(?<q>[\"'])(?<v>.*?)\\k<q>").matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.ATTRIBUTE_VALUE, matcher.group("v")));
			return attributes(html.substring(matcher.end()), tokens);
		}
		return attributes(html,tokens);
	}

	private static String elementEnd(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^\\s*</([\\w-]+)>").matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.ELEMENT_END, matcher.group(1)));
			return tokenize(html.substring(matcher.end()), tokens);
		}
		return html;
	}

	private static String textValue(String html, List<Token> tokens) {
		final Matcher matcher = Pattern.compile("^\\s*?(.+?)\\s*?(?=<)", Pattern.DOTALL).matcher(html);

		if (matcher.find()) {
			tokens.add(new Token(TokenType.TEXT_VALUE, matcher.group(1).trim()));
			return tokenize(html.substring(matcher.end()), tokens);
		}
		return html;
	}
}
