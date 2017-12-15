package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class ModelConsumer {

	public static String handleModel(String html, Model model) {
		final Matcher matcher = Pattern.compile("\\{\\{(\\w+(?:\\.\\w+)*)\\}\\}").matcher(html);
		while (matcher.find()) {
			final String key = matcher.group(1);
			final String value = getModelValue(key, model).map(Object::toString).orElse(matcher.group());
			html = html.replaceAll("\\{\\{" + key + "\\}\\}", value);
		}
		return html;
	}

}
