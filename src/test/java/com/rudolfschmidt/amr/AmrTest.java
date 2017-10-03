package com.rudolfschmidt.amr;

import org.junit.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rudolfschmidt.amr.formatter.Formatter.format;
import static com.rudolfschmidt.amr.model.Model.model;
import static org.junit.Assert.assertEquals;

public class AmrTest {

	private static final String templatesDirectory = "src/test/resources";
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
	public void includeFile() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "includeFile/out.html")));
		final String actual = engine.render("includeFile/in.html");
		assertEquals(format(expected), actual);
	}

	@Test
	public void ignoreModel() throws Exception {
		final Path path = Paths.get(templatesDirectory, "ignoreModel/out.html");
		final String expected = new String(Files.readAllBytes(path));
		final String actual = engine.render("ignoreModel/in.html");
		assertEquals(format(expected), actual);
	}

	@Test
	public void stringModel() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "stringModel/out.html")));
		final String actual = engine.render("stringModel/in.html", model().add("a", "foo").add("b", "bar"));
		assertEquals(format(expected), actual);
	}

	@Test
	public void objectModel() throws Exception {

		class ModelValue {

			private final Object value;

			private ModelValue(Object value) {
				this.value = value;
			}

		}

		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "objectModel/out.html")));
		final String actual = engine.render("objectModel/in.html", model().add("model", new ModelValue("foo")));
		assertEquals(format(expected), actual);

	}

	@Test
	public void nestedObjectModel() throws Exception {

		class ModelValue {

			private final Object value;

			private ModelValue(Object value) {
				this.value = value;
			}

		}

		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "nestedObjectModel/out.html")));
		final String actual = engine.render("nestedObjectModel/in.html", model().add("model", new ModelValue(new ModelValue("foo"))));
		assertEquals(format(expected), actual);

	}


	@Test
	public void blockFile() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "extends/simple/out.html")));
		final String actual = engine.render("extends/simple/in.html");
		assertEquals(format(expected), actual);

	}

	@Test
	public void nestedBlockFiles() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "extends/deep/out.html")));
		final String actual = engine.render("extends/deep/in.html");
		assertEquals(format(expected), actual);

	}

	@Test
	public void extendsA() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "extends/a/out.html")));
		final String actual = engine.render("extends/a/in.html");
		assertEquals(format(expected), actual);
	}

	@Test
	public void extendsB() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "extends/b/out.html")));
		final String actual = engine.render("extends/b/in.html");
		assertEquals(format(expected), actual);
	}

	@Test
	public void iteration() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "iteration/out.html")));
		final List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		final String actual = engine.render("iteration/in.html", model().add("items", list));
		assertEquals(format(expected), actual);
	}

	@Test
	public void iterationModel() throws Exception {

		class Location {

			private final String a;
			private final String b;

			public Location(String a, String b) {
				this.a = a;
				this.b = b;
			}

		}

		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "iterationModel/out.html")));
		final String actual = engine.render("iterationModel/in.html", model().add("locations", Arrays.asList(new Location("a", "b"), new Location("c", "d"))));
		assertEquals(format(expected), actual);
	}

}