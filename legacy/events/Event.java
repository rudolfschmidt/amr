package com.rudolfschmidt.amr.events;

public class Event {

	private final EventType eventType;
	private String value;

	public Event(EventType eventType, String value) {
		this.eventType = eventType;
		this.value = value;
	}

	public EventType getEventType() {
		return eventType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("%s: %s", eventType, value);
	}

}
