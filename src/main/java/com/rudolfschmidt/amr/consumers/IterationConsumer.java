package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.rudolfschmidt.amr.Amr.renderModel;
import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class IterationConsumer {

	public static String handleIteration(String html, Model model) {
		final String regex = "<for.*?item=(?<a>[\"'])(?<item>.*?)\\k<a>.*?items=(?<b>[\"'])(?<items>.*?)\\k<b>.*?>(?<d>[\\s\\S]*?)</for>";
		final Matcher matcher = Pattern.compile(regex).matcher(html);

		while (matcher.find()) {
			final String items = matcher.group("items");
			final Optional<Object> value = getModelValue(items, model);
			if (value.isPresent()) {
				final String itemKey = matcher.group("item");
				final Deque<String> iterated = new ArrayDeque<>();
				if (value.get().getClass().isArray()) {
					for (Object item : (Object[]) value.get()) {
						model.add(itemKey, item);
						iterated.add(renderModel(matcher.group("d"), model));
					}
					model.remove(itemKey);
				} else if (value.filter(Iterable.class::isInstance).isPresent()) {
					for (Object item : value.map(Iterable.class::cast).get()) {
						model.add(itemKey, item);
						iterated.add(renderModel(matcher.group("d"), model));
					}
					model.remove(itemKey);
				}
				html = html.replace(matcher.group(), iterated.stream().collect(Collectors.joining()));
			}
		}

		return html;

	}

}
