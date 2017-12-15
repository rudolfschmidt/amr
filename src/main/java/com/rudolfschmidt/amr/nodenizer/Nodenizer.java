package com.rudolfschmidt.amr.nodenizer;

import com.rudolfschmidt.amr.tokenizer.Token;

import java.util.*;

public class Nodenizer {

	public static final String[] VOIDS = new String[]{
			"area",
			"base",
			"br",
			"col",
			"command",
			"embed",
			"hr",
			"img",
			"input",
			"keygen",
			"link",
			"meta",
			"param",
			"source",
			"track",
			"wbr"
	};

	public static List<Node> nodenize(List<Token> tokens) {

		final List<Node> nodes = new ArrayList<>();

		nodenize(tokens.listIterator(), nodes);

		return nodes;

	}

	private static void nodenize(ListIterator<Token> iterator, List<Node> nodes) {

		while (iterator.hasNext()) {

			final Token token = iterator.next();

			switch (token.getTokenType()) {

				case TEXT_VALUE:

					nodes.add(new Node(NodeType.TEXT, token.getValue()));

					break;

				case ELEMENT_START:

					final Node element = new Node(NodeType.ELEMENT, token.getValue());

					nodes.add(element);

					nodenize(iterator, element, false);

					break;

			}

		}

	}

	private static void nodenize(ListIterator<Token> iterator, Node parent, boolean isVoid) {

		Attribute attribute = null;

		while (iterator.hasNext()) {

			final Token current = iterator.next();

			switch (current.getTokenType()) {

				case ELEMENT_START:

					if (isVoid) {

						iterator.previous();

						return;

					}

					final Node child = new Node(NodeType.ELEMENT, current.getValue());

					parent.add(child);

					nodenize(iterator, child, Arrays.stream(VOIDS).anyMatch(v -> v.equals(child.value)));

					break;

				case ATTRIBUTE_KEY:

					parent.add(attribute = new Attribute(current.getValue()));

					break;

				case ATTRIBUTE_VALUE:

					attribute.value = current.getValue();

					attribute = null;

					break;

				case TEXT_VALUE:

					if (isVoid) {
						iterator.previous();
						return;
					}

					parent.add(new Node(NodeType.TEXT, current.getValue()));

					break;

				case ELEMENT_END:

					if (isVoid) {
						iterator.previous();
					}

					return;

			}

		}

	}

}
