package com.rudolfschmidt.amr.nodenizer;

public class Attribute {

	public String key;
	public String value;

	public Attribute(String key) {
		this.key = key;
	}

	public Attribute(String key, String value) {
		this(key);
		this.value = value;
	}

	@Override
	public String toString() {
		return "ATTRIBUTE: " + key + ", " + value;
	}

}
