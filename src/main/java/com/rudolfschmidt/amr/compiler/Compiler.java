package com.rudolfschmidt.amr.compiler;

import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.rudolfschmidt.amr.nodenizer.Nodenizer.VOIDS;

public class Compiler {

	public static String compile(List<Node> nodes, boolean prettyFormat) {

		final StringBuilder sb = new StringBuilder();

		nodes.forEach(n -> compile(n, "", prettyFormat, sb));

		return sb.toString();

	}

	public static void compile(Node node, String prefix, boolean prettyFormat, StringBuilder sb) {

		if (prettyFormat) {
			sb.append(prefix);
		}

		if (node.type.equals(NodeType.TEXT)) {
			if (prettyFormat || !node.value.startsWith("<!--")) {
				sb.append(node.value);
			}
			if (prettyFormat) {
				sb.append("\n");
			}
			return;
		}

		sb.append("<").append(node.value);

		node.attributes.forEach(a -> {
			sb.append(" ").append(a.key);
			Optional.ofNullable(a.value).ifPresent(v -> sb.append("=\"").append(v).append("\""));
		});

		sb.append(">");

		if (prettyFormat && node.nodes.stream().findFirst().filter(n -> n.type.equals(NodeType.ELEMENT)).isPresent()) {
			sb.append("\n");
		}

		if (node.nodes.size() < 2 && node.nodes.stream().noneMatch(n -> n.type.equals(NodeType.ELEMENT))) {

			node.nodes.forEach(n -> sb.append(n.value));

		} else {

			final Iterator<Node> iterator = node.nodes.iterator();

			while (iterator.hasNext()) {
				final Node current = iterator.next();
				compile(current, current.type.equals(NodeType.TEXT) ? "" : prefix + "\t", prettyFormat, sb);
				break;
			}

			while (iterator.hasNext()) {
				final Node current = iterator.next();
				compile(current, prefix + "\t", prettyFormat, sb);
			}

		}

		if (Arrays.stream(VOIDS).noneMatch(v -> v.equals(node.value))) {

			if (prettyFormat && node.nodes.stream().anyMatch(n -> n.type.equals(NodeType.ELEMENT))) {
				sb.append(prefix);
			}

			sb.append("</").append(node.value).append(">");

		}

		if (prettyFormat) {
			sb.append("\n");
		}

	}

}
