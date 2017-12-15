package com.rudolfschmidt.amr;

import org.junit.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static com.rudolfschmidt.amr.model.Model.model;
import static org.junit.Assert.assertEquals;

public class IteratorTest {

	private static final String templatesDirectory = "src/test/resources/iterator";
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
		final ArrayList<Object> items = new ArrayList<>();
		items.add("a");
		items.add("b");
		items.add("c");
		final String actual = engine.render("a/in.html", model("items", items));
		assertEquals(format(expected), actual);
	}

	@Test
	public void b() throws Exception {

		class Location {
			private final String a;
			private final String b;

			public Location(String a, String b) {
				this.a = a;
				this.b = b;
			}
		}

		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, "b/out.html")));
		final ArrayList<Object> items = new ArrayList<>();
		items.add("a");
		items.add("b");
		items.add("c");
		final String actual = engine.render("b/in.html", model("items", Arrays.asList(new Location("a", "b"), new Location("c", "d"))));
		assertEquals(format(expected), actual);
	}

	private static String format(String html) {
		return html.replaceAll("(?<=>)\\s+?(?=<)", "");
	}

}