package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.nodenizer.Attribute;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rudolfschmidt.amr.Amr.parse;

public class Includer {

	public static List<Node> applyIncluder(Path templatePath, List<Node> nodes) throws IOException {
		final List<Node> posts = new ArrayList<>();
		for (Node node : nodes) {
			if (node.type == NodeType.ELEMENT && node.value.equals("include")) {
				final Optional<Attribute> file = node.attributes.stream().filter(a -> a.key.equals("file")).findAny();
				if (file.isPresent()) {
					final Attribute fileAttribute = file.get();
					final Path normalized = templatePath.getParent().resolve(fileAttribute.value);
					posts.addAll(parse(normalized));
				}
			} else {
				posts.add(new Node(node.type, node.value, node.attributes, applyIncluder(templatePath, node.nodes)));
			}
		}
		return posts;
	}

}
