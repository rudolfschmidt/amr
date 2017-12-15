package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;
import com.rudolfschmidt.amr.nodenizer.Attribute;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class Conditioner {

	public static List<Node> applyConditioner(List<Node> nodes, Model model) {
		final List<Node> posts = new ArrayList<>();
		for (Node node : nodes) {
			if (node.type == NodeType.ELEMENT && node.value.equals("if")) {
				final Optional<Attribute> expression = node.attributes.stream().filter(a -> a.key.equals("expression")).findAny();
				if (expression.isPresent()) {
					final Attribute attribute = expression.get();

					boolean condition = false;
					final String[] tokens = attribute.value.split("\\s");
					for (int i = 0; i < tokens.length; i++) {
						if (!tokens[i].equals("&&") && !tokens[i].equals("||")) {
							if (i - 1 > 0 && tokens[i - 1].equals("&&")) {
								condition &= evaluate(tokens[i], model);
							} else if (i - 1 > 0 && tokens[i - 1].equals("||")) {
								condition |= evaluate(tokens[i], model);
							} else {
								condition = evaluate(tokens[i], model);
							}
						}
					}

					if (condition) {
						posts.addAll(node.nodes);
					}

				}
			} else {
				posts.add(new Node(node.type, node.value, node.attributes, applyConditioner(node.nodes, model)));
			}
		}
		return posts;
	}

	private static boolean evaluate(String token, Model model) {

		if (token.startsWith("!")) {
			return getModelValue(token.substring(1), model).map(Conditioner::evaluate).map(bool -> !bool).orElse(true);
		} else {
			return getModelValue(token, model).map(Conditioner::evaluate).orElse(false);
		}

	}

	private static boolean evaluate(Object model) {
		final Optional<Object> e = Optional.ofNullable(model);
		if (e.filter(String.class::isInstance).isPresent()) {
			return e.map(String.class::cast).filter(value -> value.length() > 0).isPresent();
		} else if (e.filter(Boolean.class::isInstance).isPresent()) {
			return e.filter(value -> value.equals(Boolean.TRUE)).isPresent();
		} else if (e.filter(Number.class::isInstance).isPresent()) {
			return e.map(Number.class::cast).filter(value -> value.doubleValue() > 0).isPresent();
		} else if (e.isPresent()) {
			return true;
		}
		return false;
	}

}
