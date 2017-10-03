package com.rudolfschmidt.amr.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventWriter {

	public static Events writeEvents(Path templateFile) throws IOException {

		final Events events = new Events();

		writeEvents(new String(Files.readAllBytes(templateFile)), events);

		return events;

	}

	private static void writeEvents(String str, Events events) {

		while (!str.trim().isEmpty()) {

			str = startElement(str, events);

			str = characters(str, events);

			str = endElement(str, events);

		}

	}

	private static String startElement(String str, Events events) {

		final Matcher matcher = Pattern.compile("^<([^/].*?)>").matcher(str);

		if (matcher.find()) {

			elementName(matcher.group(1), events);

			str = str.substring(matcher.end()).trim();

		}

		return str;

	}

	private static void elementName(String str, Events events) {

		final Matcher matcher = Pattern.compile("^[\\w-]+").matcher(str);

		if (matcher.find()) {

			events.add(new Event(EventType.START_ELEMENT, str.substring(matcher.start(), matcher.end())));

			attributes(str.substring(matcher.end()).trim(), events);

		}

	}

	private static void attributes(String str, Events events) {

		for (String value : str.split("\\s")) {

			value = attributeKey(value, events);

			attributeValue(value, events);

		}

	}

	private static String attributeKey(String str, Events events) {

		final Matcher matcher = Pattern.compile("^[\\w-]+").matcher(str);

		if (matcher.find()) {

			events.add(new Event(EventType.ATTRIBUTE_KEY, str.substring(matcher.start(), matcher.end())));

			str = str.substring(matcher.end());

		}

		return str;

	}

	private static void attributeValue(String str, Events events) {

		Matcher matcher;

		if ((matcher = Pattern.compile("^='.*?[^\\\\]'").matcher(str)).find()) {

			String value;
			value = str.substring(matcher.start() + 1, matcher.end());
			value = value.substring(1, value.length() - 1);
			value = value.replaceAll("\"", "'");
			value = value.replace("\\", "");

			events.add(new Event(EventType.ATTRIBUTE_VALUE, value));

		} else if ((matcher = Pattern.compile("^=\".*?\"").matcher(str)).find()) {

			String value;
			value = str.substring(matcher.start() + 1, matcher.end());
			value = value.substring(1, value.length() - 1);
			value = value.replace("'", "\"");

			events.add(new Event(EventType.ATTRIBUTE_VALUE, value));

		}

	}

	private static String endElement(String str, Events events) {

		final Matcher matcher = Pattern.compile("^</(.*?)>").matcher(str);

		if (matcher.find()) {

			events.add(new Event(EventType.END_ELEMENT, matcher.group(1)));

			str = str.substring(matcher.end()).trim();

		}

		return str;

	}

	private static String characters(String str, Events events) {

		final Matcher matcher = Pattern.compile("^[^<].*?(?=<)").matcher(str);

		if (matcher.find()) {

			events.add(new Event(EventType.CHARACTERS, str.substring(matcher.start(), matcher.end())));

			str = str.substring(matcher.end()).trim();

		}

		return str;

	}

}
