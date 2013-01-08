package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DefinitionListFilterTest extends FilterTestSupport {
	public DefinitionListFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(DefinitionListFilterTest.class);
	}

	public void testDefinitionList0() {
		assertEquals(
				"\n" + 
				"<dl>\n" + 
				"<dd><i>There is also an <a href=\"http://www.bliki.info/wiki/Asteroid\" title=\"asteroid\">asteroid</a> <a href=\"http://www.bliki.info/wiki/9969_Braille\" title=\"9969 Braille\">9969 Braille</a></i></dd></dl>",
				wikiModel.render(":''There is also an [[asteroid]] [[9969 Braille]]''"));
	}

	public void testDefinitionList1() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dt>name</dt>\n" + 
				"<dd>Definition</dd></dl>", wikiModel.render(";name:Definition"));
	}

	public void testDefinitionList2() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dt>name </dt>\n" + 
				"<dd>Definition</dd></dl>", wikiModel.render("; name : Definition"));
	}

	public void testDefinitionList10() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>a simple test\n" + 
				"  x+y\n" + 
				"  </dd></dl>\n" + 
				"<p>test test</p>", wikiModel
				.render(":a simple test<nowiki>\n" + "  x+y\n" + "  </nowiki>\n" + "test test"));
	}

	public void testDefinitionList11() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>a simple test<span class=\"math\">ein text</span></dd></dl>\n" + 
				"<pre>\n" + 
				" x+y\n" + 
				"  \n" + 
				"</pre>\n" + 
				"<p>test test</p>", wikiModel.render(":a simple test<math>ein text\n" + "  x+y\n" + "  \n" + "test test"));
	}
	
	public void testDefinitionList12() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>blabla\n" + 
				"<dl>\n" + 
				"<dd>blablabla</dd></dl></dd></dl>\n" + 
				"<pre>\n" + 
				"test it\n" + 
				"\n" + 
				"</pre>", wikiModel.render(":blabla\n::blablabla\n" + " test it\n"));
	}
}