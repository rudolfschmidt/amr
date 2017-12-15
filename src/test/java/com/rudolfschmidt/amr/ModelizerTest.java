package com.rudolfschmidt.amr;

import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.rudolfschmidt.amr.model.Model.model;
import static org.junit.Assert.assertEquals;

public class ModelizerTest {

	private static final String templatesDirectory = "src/test/resources/modelizer";
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
	public void a() throws Exception {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "a/out.html")));
		final String actual = engine.render("a/in.html", model("a", "foo").add("b", "bar"));
		assertEquals(format(expected), actual);
	}

	@Test
	public void b() throws Exception {

		class ModelValue {
			private final Object value;

			private ModelValue(Object value) {
				this.value = value;
			}
		}

		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "b/out.html")));
		final String actual = engine.render("b/in.html", model("model", new ModelValue("foo")));
		assertEquals(format(expected), actual);
	}

	private static String format(String html) {
		return html.replaceAll("(?<=>)\\s+?(?=<)", "");
	}

}