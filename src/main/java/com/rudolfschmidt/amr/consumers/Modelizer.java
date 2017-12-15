package com.rudolfschmidt.amr.consumers;

import com.rudolfschmidt.amr.model.Model;
import com.rudolfschmidt.amr.nodenizer.Attribute;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.nodenizer.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.rudolfschmidt.amr.model.ModelReader.getModelValue;

public class Modelizer {

	public static List<Node> applyModelizer(List<Node> nodes, Model model) {
		final List<Node> posts = new ArrayList<>();
		for (Node node : nodes) {
			if (node.type == NodeType.TEXT) {
				posts.add(new Node(node.type, applyModel(node.value, model), node.attributes, node.nodes));
			} else {
				final List<Attribute> attributes = node.attributes.stream().map(attribute -> new Attribute(attribute.key, applyModel(attribute.value, model))).collect(Collectors.toList());
				posts.add(new Node(node.type, applyModel(node.value, model), attributes, applyModelizer(node.nodes, model)));
			}
		}
		return posts;
	}

	private static String applyModel(String str, Model model) {
		final Matcher matcher = Pattern.compile("\\{\\{(\\w+(?:\\.\\w+)*)\\}\\}").matcher(str);
		while (matcher.find()) {
			final String key = matcher.group(1);
			final String value = getModelValue(key, model).map(Object::toString).orElse(matcher.group());
			str = str.replaceAll("\\{\\{" + key + "\\}\\}", value);
		}
		return str;
	}

}
