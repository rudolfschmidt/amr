package com.rudolfschmidt.amr;

import com.rudolfschmidt.amr.model.Model;
import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.tokenizer.Token;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.List;

import static com.rudolfschmidt.amr.compiler.Compiler.compile;
import static com.rudolfschmidt.amr.consumers.Conditioner.applyConditioner;
import static com.rudolfschmidt.amr.consumers.Extender.applyExtender;
import static com.rudolfschmidt.amr.consumers.Includer.applyIncluder;
import static com.rudolfschmidt.amr.consumers.Iterator.applyIterator;
import static com.rudolfschmidt.amr.consumers.Modelizer.applyModelizer;
import static com.rudolfschmidt.amr.nodenizer.Nodenizer.nodenize;
import static com.rudolfschmidt.amr.tokenizer.Tokenizer.tokenize;


public class Amr {

	private final String templatesDirectory;
	private final boolean prettyFormat;

	public Amr(
			final String templatesDirectory,
			final boolean prettyFormat
	) {
		this.templatesDirectory = templatesDirectory;
		this.prettyFormat = prettyFormat;
	}

	public static List<Node> parse(Path templatePath) throws IOException {

		final List<Token> tokens = tokenize(templatePath);

		List<Node> nodes;
		nodes = nodenize(tokens);
		nodes = applyExtender(templatePath, nodes);
		nodes = applyIncluder(templatePath, nodes);

		return nodes;

	}

	public static List<Node> parse(Path templatePath, Model model) throws IOException {

		List<Node> nodes;
		nodes = parse(templatePath);
		nodes = parse(nodes, model);

		return nodes;

	}

	public static List<Node> parse(List<Node> nodes, Model model) {

		nodes = applyIterator(nodes, model);
		nodes = applyConditioner(nodes, model);
		nodes = applyModelizer(nodes, model);

		return nodes;

	}

	public String render(String templateFile) throws IOException {

		return compile(parse(Paths.get(templatesDirectory, templateFile)), prettyFormat);

	}

	public String render(String templateFile, Model model) throws IOException {

		return compile(parse(Paths.get(templatesDirectory, templateFile), model), prettyFormat);

	}

}
