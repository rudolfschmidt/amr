package com.rudolfschmidt.amr;

import com.rudolfschmidt.amr.model.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.rudolfschmidt.amr.consumers.ExtendsConsumer.handleExtends;
import static com.rudolfschmidt.amr.consumers.IfConsumer.handleIf;
import static com.rudolfschmidt.amr.consumers.IncludeConsumer.handleInclude;
import static com.rudolfschmidt.amr.consumers.IterationConsumer.handleIteration;
import static com.rudolfschmidt.amr.consumers.ModelConsumer.handleModel;
import static com.rudolfschmidt.amr.formatter.Formatter.format;
import static com.rudolfschmidt.amr.formatter.Formatter.formatPretty;


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

	public String render(String templateFile) throws IOException {
		final String html = parse(Paths.get(templatesDirectory, templateFile));
		return prettyFormat ? formatPretty(html) : format(html);
	}

	public String render(String templateFile, Model model) throws IOException {
		String html = parse(Paths.get(templatesDirectory, templateFile));
		html = renderModel(html, model);
		return prettyFormat ? formatPretty(html) : format(html);
	}

	public static String renderModel(String html, Model model) {
		html = handleIteration(html, model);
		html = handleIf(html, model);
		html = handleModel(html, model);
		return html;
	}

	public static String parse(Path templatePath) throws IOException {

		String html = new String(Files.readAllBytes(templatePath));

		html = handleExtends(html, templatePath);
		html = handleInclude(html, templatePath);

		return html;

	}


}
