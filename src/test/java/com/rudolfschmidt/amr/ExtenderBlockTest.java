package com.rudolfschmidt.amr;

import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class ExtenderBlockTest {

	private static final String templatesDirectory = "src/test/resources/extender/block";
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
		test("a");
	}

	@Test
	public void b() throws Exception {
		test("b");
	}

	@Test
	public void c() throws Exception {
		test("c");
	}

	private void test(String dir) throws IOException {
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, dir, "out.html")));
		final String actual = engine.render(dir + "/in.html");
		assertEquals(format(expected), actual);
	}

	private static String format(String html) {
		return html.replaceAll("(?<=>)\\s+?(?=<)", "");
	}

}