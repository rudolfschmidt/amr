package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class IfConsumer {

	public static String handleIf(String html, Model model) {
		final String regex = "<if.*?exp=(?<q>[\"'])(?<e>.*?)\\k<q>.*?>(?<d>[\\s\\S]*?)</if>";
		final Matcher matcher = Pattern.compile(regex).matcher(html);
		while (matcher.find()) {
			final Optional<Object> e = getModelValue(matcher.group("e"), model);
			if (e.filter(String.class::isInstance).map(String.class::cast).filter(value -> value.length() > 0).isPresent()) {
				html = html.replace(matcher.group(), matcher.group("d"));
			} else if (e.filter(Boolean.class::isInstance).map(Boolean.class::cast).filter(value -> value).isPresent()) {
				html = html.replace(matcher.group(), matcher.group("d"));
			} else if (e.filter(Number.class::isInstance).map(Number.class::cast).filter(value -> value.doubleValue() > 0).isPresent()) {
				html = html.replace(matcher.group(), matcher.group("d"));
			} else if (e.isPresent()) {
				html = html.replace(matcher.group(), matcher.group("d"));
			} else {
				html = html.replace(matcher.group(), "");
			}
		}
		return html;
	}

}
