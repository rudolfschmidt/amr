package com.rudolfschmidt.amr.model;

import java.util.HashMap;
import java.util.Map;

public class Model {

	private final Map<String, Object> map = new HashMap<>();

	public static Model model() {
		return new Model();
	}

	public static Model model(String key, Object value) {
		return model().add(key, value);
	}

	public Model add(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public Object get(String key) {
		return map.get(key);
	}

	public void remove(String key) {
		map.remove(key);
	}

	@Override
	public String toString() {
		return String.format("Model[%s]", map);
	}

}
