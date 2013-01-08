package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class CiteFilterTest extends FilterTestSupport {
	public CiteFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(CiteFilterTest.class);
	}

	public void testDivTag1() {
		assertEquals(
				"\n" + 
				"<p><cite id=\"Test\">\n" + 
				"a cite text\n" + 
				"</cite></p>",
				wikiModel
						.render("<cite id=\"Test\">\n"
								+ "a cite text\n"
								+ "</cite>"));
	}
}