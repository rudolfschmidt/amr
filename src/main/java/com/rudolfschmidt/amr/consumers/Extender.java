package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.nodenizer.Attribute;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rudolfschmidt.amr.Amr.parse;

public class Extender {

	private static final String EXTENDS = "extends";
	private static final String BLOCK = "block";
	private static final String APPEND = "append";
	private static final String PREPEND = "prepend";

	public static List<Node> applyExtender(Path templatePath, List<Node> nodes) throws IOException {
		final List<Node> posts = new ArrayList<>();
		for (Node node : nodes) {
			if (node.type.equals(NodeType.ELEMENT) && node.value.equals(EXTENDS)) {
				final Optional<Attribute> file = node.attributes.stream().filter(a -> a.key.equals("file")).findAny();
				if (file.isPresent()) {
					final Attribute fileAttribute = file.get();
					final Path normalized = templatePath.getParent().resolve(fileAttribute.value);
					extendsTemplate(parse(normalized), node.nodes, posts);
					return posts;
				}
			}
			posts.add(new Node(node.type, node.value, node.attributes, applyExtender(templatePath, node.nodes)));
		}
		return posts;
	}

	private static void extendsTemplate(List<Node> templateNodes, List<Node> extendsBlocks, List<Node> posts) {
		for (Node templateNode : templateNodes) {
			if (isBlock(templateNode)) {
				for (Node extendsBlock : extendsBlocks) {
					if (isBlock(extendsBlock) && sameName(templateNode, extendsBlock)) {
						posts.addAll(extendsBlock.nodes);
					} else if (isAppend(extendsBlock) && sameName(templateNode, extendsBlock)) {
						posts.addAll(Stream.concat(templateNode.nodes.stream(), extendsBlock.nodes.stream()).collect(Collectors.toList()));
					} else if (isPrepend(extendsBlock) && sameName(templateNode, extendsBlock)) {
						posts.addAll(Stream.concat(extendsBlock.nodes.stream(), templateNode.nodes.stream()).collect(Collectors.toList()));
					}
				}
			} else {
				final Node post = new Node(templateNode.type, templateNode.value, templateNode.attributes);
				posts.add(post);
				extendsTemplate(templateNode.nodes, extendsBlocks, post.nodes);
			}
		}
	}

	private static boolean sameName(Node a, Node b) {
		for (Attribute x : a.attributes) {
			if (x.key.equals("name")) {
				for (Attribute y : b.attributes) {
					if (y.key.equals("name") && x.value.equals(y.value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean isBlock(Node node) {
		return node.type == NodeType.ELEMENT && node.value.equals(BLOCK);
	}

	private static boolean isAppend(Node node) {
		return node.type == NodeType.ELEMENT && node.value.equals(APPEND);
	}

	private static boolean isPrepend(Node node) {
		return node.type == NodeType.ELEMENT && node.value.equals(PREPEND);
	}

}
