package com.rudolfschmidt.amr.nodenizer;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public final NodeType type;
	public final String value;
	public final List<Attribute> attributes;
	public final List<Node> nodes;

	public Node(NodeType type, String value, List<Attribute> attributes, List<Node> nodes) {
		this.type = type;
		this.value = value;
		this.attributes = attributes;
		this.nodes = nodes;
	}

	public Node(NodeType type, String value, List<Attribute> attributes) {
		this(type, value, attributes, new ArrayList<>());
	}

	public Node(NodeType type, String value) {
		this(type, value, new ArrayList<>());
	}

	public void add(Node node) {
		nodes.add(node);
	}

	public void add(Attribute attribute) {
		attributes.add(attribute);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		string("", sb);
		return sb.toString();
	}

	public void string(String indent, StringBuilder sb) {
		sb.append(indent).append(type.name()).append(": ").append(value);
		attributes.forEach(a -> sb.append("\n").append(indent).append("\t").append(a));
		for (Node node : nodes) {
			sb.append("\n");
			node.string(indent + "\t", sb);
		}
	}

}
