package com.rudolfschmidt.amr.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class ModelReader {

	public static Optional<Object> getModelValue(String key, Model model) {

		final Optional<Object> modelValue = Optional.ofNullable(model.get(key));

		if (modelValue.isPresent()) {
			return modelValue;
		}

		final String[] objectPaths = key.split("\\.");
		final Optional<Object> partialModelValue = Optional.ofNullable(model.get(objectPaths[0]));

		if (!partialModelValue.isPresent() || objectPaths.length < 2) {
			return Optional.empty();
		} else {
			return getModelValue(partialModelValue.get(), objectPaths, 1);
		}

	}

	private static Optional<Object> getModelValue(Object modelValue, String[] objectPaths, int pathIndex) {

		return Stream.of(modelValue.getClass().getDeclaredFields())
				.filter(field -> field.getName().equals(objectPaths[pathIndex]))
				.findAny()
				.map(field -> {
					field.setAccessible(true);
					try {
						return field.get(modelValue);
					} catch (IllegalAccessException e) {
						throw new ModelException();
					}
				})
				.flatMap(value -> {
					final Class[] primeTypes = {String.class, Number.class};
					if (!Arrays.asList(primeTypes).contains(value.getClass()) && pathIndex + 1 < objectPaths.length) {
						return getModelValue(value, objectPaths, pathIndex + 1);
					} else {
						return Optional.of(value);
					}
				});

	}

}
