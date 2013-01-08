package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class HeaderFilterTest extends FilterTestSupport {
	public HeaderFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(HeaderFilterTest.class);
	}

	public void testBad01() {
		assertEquals("\n" + "<p>=</p>", wikiModel.render("="));
	}

	public void testBad02() {
		assertEquals("\n<p>=\n</p>", wikiModel.render("=\n"));
	}

	public void testBad03() {
		assertEquals("<a id=\"\" name=\"\"></a>\n", wikiModel.render("==  \r  \n"));
	}

	public void testH2() {
		assertEquals("<a id=\"Text\" name=\"Text\"></a><h2>Text</h2>", wikiModel.render("==Text=="));
	}

	public void testH6Whitespace() {
		assertEquals("<a id=\"Text_.C3.9Cbersicht\" name=\"Text_.C3.9Cbersicht\"></a><h6>Text Übersicht</h6>\n" + 
				"<p>A new line.</p>",
				wikiModel.render("=======Text Übersicht=======   \r \nA new line."));
	}

	public void testH2WithPunctuation() {
		assertEquals("\n" + "<p>==Text Übersicht==:</p>", wikiModel.render("==Text Übersicht==:"));
	}

	public void testH2Apostrophe() {
		assertEquals(
				"<a id=\"Wikipedia\'s_sister_projects\" name=\"Wikipedia\'s_sister_projects\"></a><h2>Wikipedia&#39;s sister projects</h2>",
				wikiModel.render("==Wikipedia's sister projects=="));
	}

	public void testH2Link01() {
		assertEquals(
				" \n" + 
				"<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#Text_.C3.9Cbersicht\">Text Übersicht</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"<a id=\"Text_.C3.9Cbersicht\" name=\"Text_.C3.9Cbersicht\"></a><h2>Text <a href=\"http://www.bliki.info/wiki/Overview\" title=\"Overview\">Übersicht</a></h2>",
				wikiModel.render("__FORCETOC__ \n==Text [[Overview|Übersicht]]=="));
	}
	
	public void testH3Link01() {
		assertEquals(
				" \n" + 
				"<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#Captions\">Captions</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"<a id=\"Captions\" name=\"Captions\"></a><h3><a href=\"http://www.bliki.info/wiki/Help:Table_Caption\" title=\"Help:Table Caption\">Captions</a></h3>",
				wikiModel.render("__FORCETOC__ \n===[[Help:Table Caption|Captions]]==="));
	}
}
