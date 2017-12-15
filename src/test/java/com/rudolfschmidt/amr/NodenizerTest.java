package com.rudolfschmidt.amr;

import com.rudolfschmidt.amr.nodenizer.Node;
import com.rudolfschmidt.amr.tokenizer.Token;
import com.rudolfschmidt.amr.tokenizer.TokenType;
import org.junit.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.rudolfschmidt.amr.nodenizer.Nodenizer.nodenize;
import static org.junit.Assert.assertEquals;

public class NodenizerTest {

	private static final String templatesDirectory = "src/test/resources/nodenizer";

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
	public void a() throws IOException {
		test("a");
	}

	@Test
	public void b() throws IOException {
		test("b");
	}

	@Test
	public void c() throws IOException {
		test("c");
	}

	@Test
	public void d() throws IOException {
		test("d");
	}

	@Test
	public void e() throws IOException {
		test("e");
	}

	private void test(String dir) throws IOException {
		final List<Token> tokens = tokenize(dir + "/in.txt");
		final List<Node> nodes = nodenize(tokens);
		final String expected = new String(Files.readAllBytes(Paths.get(templatesDirectory, dir, "out.txt")));
		final StringBuilder actual = new StringBuilder();
		for (Node node : nodes) {
			actual.append(node.toString());
		}
		assertEquals(expected, actual.toString());
	}

	private List<Token> tokenize(String templateFile) throws IOException {
		final List<Token> tokens = new ArrayList<>();
		for (String line : Files.readAllLines(Paths.get(templatesDirectory, templateFile))) {
			final String[] arr = line.split(": ");
			tokens.add(new Token(TokenType.valueOf(arr[0]), arr[1]));
		}
		return tokens;
	}

}
