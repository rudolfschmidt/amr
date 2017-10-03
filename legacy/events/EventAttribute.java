package com.rudolfschmidt.amr.events;

import java.util.Optional;

public class EventAttribute {

	private final String attributeKey;
	private String attributeValue;

	public EventAttribute(String attributeKey) {
		this.attributeKey = attributeKey;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getAttributeKey() {
		return attributeKey;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(attributeKey);
		if (Optional.ofNullable(attributeValue).filter(value -> !value.isEmpty()).isPresent()) {
			sb.append("=\"").append(attributeValue).append("\"");
		}
		return sb.toString();
	}
}
