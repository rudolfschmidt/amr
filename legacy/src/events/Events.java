package com.rudolfschmidt.amr.events;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class Events implements Iterable<Event> {

	private final Deque<Event> events = new ArrayDeque<>();
	private boolean accept = false;

	public void add(Event event) {
		events.add(event);
	}

	@Override
	public Iterator<Event> iterator() {
		return events.iterator();
	}

	public void addAll(Events events) {
		this.events.addAll(events.events);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		for (Event event : events) {
			sb.append(event).append("\n");
		}
		return sb.toString();
	}
}
