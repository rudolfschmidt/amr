package com.rudolfschmidt.amr;

import com.rudolfschmidt.amr.tokenizer.Token;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import static com.rudolfschmidt.amr.tokenizer.Tokenizer.tokenize;
import static org.junit.Assert.assertEquals;

public class TokenizerTest {

	private static final String templatesDirectory = "src/test/resources/tokenizer";

	@BeforeClass
	public static void beforeClass() {
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

	@Test
	public void d() throws Exception {
		test("d");
	}

	@Test
	public void e() throws Exception {
		test("e");
	}

	@Test
	public void f() throws Exception {
		test("f");
	}

	@Test
	public void g() throws Exception {
		test("g");
	}

	@Test
	public void comments() throws Exception {
		test("comments");
	}

	@Test
	public void h() throws Exception {
		test("h");
	}

	@Test
	public void i() throws Exception {
		test("i");
	}

	@Test
	public void doctypes() throws Exception {
		test("doctypes");
	}

	private void test(String dir) throws IOException {
		final List<Token> actual = tokenize(Paths.get(templatesDirectory, dir, "in.html"));
		final List<String> expected = Files.readAllLines(Paths.get(templatesDirectory, dir, "out.txt"));
		final Iterator<Token> a = actual.iterator();
		final Iterator<String> b = expected.iterator();
		while (a.hasNext() && b.hasNext()) {
			assertEquals(b.next(), a.next().toString());
		}
	}

}
