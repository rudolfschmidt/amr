package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;
import com.rudolfschmidt.amr.nodenizer.Attribute;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rudolfschmidt.amr.Amr.parse;
import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class Iterator {

	public static List<Node> applyIterator(List<Node> nodes, Model model) {
		final List<Node> posts = new ArrayList<>();
		for (Node node : nodes) {
			if (node.type.equals(NodeType.ELEMENT) && node.value.equals("for")) {
				final Attribute items = node.attributes.stream().filter(a -> a.key.equals("items")).findAny().orElseThrow(IllegalStateException::new);
				final Attribute item = node.attributes.stream().filter(a -> a.key.equals("item")).findAny().orElseThrow(IllegalStateException::new);
				final Optional<Object> modelValue = getModelValue(items.value, model);
				if (modelValue.isPresent()) {
					final List<Node> iterated = new ArrayList<>();
					if (modelValue.get().getClass().isArray()) {
						for (Object current : (Object[]) modelValue.get()) {
							model.add(item.value, current);
							iterated.addAll(parse(node.nodes, model));
						}
						model.remove(item.value);
					} else if (modelValue.filter(Iterable.class::isInstance).isPresent()) {
						for (Object current : modelValue.map(Iterable.class::cast).get()) {
							model.add(item.value, current);
							iterated.addAll(parse(node.nodes, model));
						}
						model.remove(item.value);
					}
					return iterated;
				}
			} else {
				posts.add(new Node(node.type, node.value, node.attributes, applyIterator(node.nodes, model)));
			}
		}
		return posts;
	}

}
