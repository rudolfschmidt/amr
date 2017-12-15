package com.rudolfschmidt.amr;

import org.junit.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static com.rudolfschmidt.amr.model.Model.model;
import static org.junit.Assert.assertEquals;

public class ConditionerTest {

	private static final String templatesDirectory = "src/test/resources/conditioner";
	private static Amr engine;

	@BeforeClass
	public static void beforeClass() {
		engine = new Amr(templatesDirectory, false);
	}

	@AfterClass
	public static void afterClass() {
	}

	@After
	public void after() {
	}

	@Before
	public void before() {
	}

	@Test
	public void strings() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "out.html")));
		final String actual = engine.render("in.html", model("a", "a").add("b", ""));
		assertEquals(format(expected), actual);
	}

	@Test
	public void bools() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "out.html")));
		final String actual = engine.render("in.html", model("a", true).add("b", false));
		assertEquals(format(expected), actual);
	}

	@Test
	public void numbers() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "out.html")));
		final String actual = engine.render("in.html", model("a", 1).add("b", 0));
		assertEquals(format(expected), actual);
	}

	@Test
	public void object() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "out.html")));
		final String actual = engine.render("in.html", model("a", new Object()).add("b", null));
		assertEquals(format(expected), actual);
	}

	private static String format(String html) {
		return html.replaceAll("(?<=>)\\s+?(?=<)", "");
	}

}