package info.bliki.wiki.filter;

import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPLinkFilterTest extends FilterTestSupport {
	public WPLinkFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPLinkFilterTest.class);
	}

	public void testLinkHash() {
		assertEquals(
				"\n" + 
				"<ol>\n" + 
				"<li>\n" + 
				"<ol>\n" + 
				"<li><a href=\"http://www.bliki.info/wiki/Using_Eclipse_Wikipedia_Editor:Getting_Started#Features\" title=\"Using Eclipse Wikipedia Editor:Getting Started\">Features</a></li></ol></li></ol>",
				wikiModel.render("##[[Using Eclipse Wikipedia Editor:Getting Started#Features|Features]]"));
	}

	public void testLink() {
		assertEquals(
				"\n" + 
				"<p>You could open the <a href=\"http://www.bliki.info/wiki/Wikipedia:sandbox\" title=\"Wikipedia:sandbox\">sandbox</a> in a separate window or tab to be able to see both this text and your tests in the sandbox.</p>",
				wikiModel
						.render("You could open the [[Wikipedia:sandbox|sandbox]] in a separate window or tab to be able to see both this text and your tests in the sandbox."));
	}

	public void testLink0() {
		assertEquals("\n" + "<p>[X]</p>", wikiModel.render("[X]"));
	}

	public void testLink1() {
		assertEquals("\n" + "<p><a href=\"http://en.wikipedia.org/wiki/Test\">Test</a></p>", wikiModel.render("[[en:Test|Test]]"));
	}

	public void testLink2() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Test\" title=\"Test\">Test</a></p>", wikiModel.render("[[Test|Test]]"));
	}

	public void testLink3() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Category:Test_page\" title=\"Category:Test page\">Category:Test page</a></p>",
				wikiModel.render("[[:Category:Test page]]"));
	}

	/**
	 * Categories are not rendered
	 * 
	 */
	public void testCategory01() {
		assertEquals("", wikiModel.render("[[Category:Tips and Tricks]]"));
		Map<String, String> map = wikiModel.getCategories();
		assertTrue(map.containsKey("Tips and Tricks"));
	}

	public void testCategory02() {
		assertEquals("", wikiModel.render("[[Category:Rock and Roll Hall of Fame inductees|Beatles, The]]"));
		Map<String, String> map = wikiModel.getCategories();
		// assertTrue(map.containsKey("Rock and Roll Hall of Fame inductees"));
		assertTrue(map.containsValue("Beatles, The"));
	}

	public void testLink5() {
		assertEquals("\n" + "<p><a href=\"http://wikitravel.org/en/test\">wikitravel:test</a></p>", wikiModel
				.render("[[wikitravel:test]]"));
	}

	public void testLink6() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Test:hello_world\" title=\"Test:hello world\">hello world</a></p>", wikiModel
				.render("[[Test:hello world|]]"));
	}

	public void testLink7() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Test(hello_world)\" title=\"Test(hello world)\">Test</a></p>", wikiModel
				.render("[[Test(hello world)|]]"));
	}

	public void testLink8() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Boston%2C_Massachusetts\" title=\"Boston, Massachusetts\">Boston</a></p>", wikiModel
				.render("[[Boston, Massachusetts|]]"));
	}

	public void testLink9() {
		assertEquals("\n" + 
				"<p>test [[lets start\n" + 
				"a 2 rows link]] test</p>", wikiModel.render("test [[lets start\na 2 rows link]] test"));
	}

	public void testLink9a() {
		assertEquals("\n" + 
				"<p>test <a href=\"http://www.bliki.info/wiki/Lets_start_a_2_rows_link\" title=\"lets start a 2 rows link\">lets start\n" + 
				"a 2 rows link</a> test</p>", wikiModel.render("test [[lets start a 2 rows link|lets start\na 2 rows link]] test"));
	}
	
	public void testLink10() {
		assertEquals("\n" + 
				"<p>test <a href=\"http://www.bliki.info/wiki/Lets_start\" title=\"lets start\">a 2 rows piped link</a> test</p>",
				wikiModel.render("test [[lets start|\na 2 rows piped link]] test"));
	}

	public void testLink11() {
		assertEquals("\n" + "<p>test\n" + "</p>\n" + "<ul>\n" + "<li>blabla[[List of cities by country#Morocco|</li></ul>\n"
				+ "<p>Cities in Morocco]]</p>", wikiModel.render("test\n*blabla[[List of cities by country#Morocco|\nCities in Morocco]]"));
	}
 
	//
	public void testLink12() {
		assertEquals(
				"\n" + 
				"<p>kellereien wie <a href=\"http://www.bliki.info/wiki/Henkell_%26amp%3B_S%C3%B6hnlein\" title=\"Henkell &amp;amp; Söhnlein\">Henkell</a>, <a href=\"http://www.bliki.info/wiki/S%C3%B6hnlein\" title=\"Söhnlein\">Söhnlein</a></p>",
				wikiModel.render("kellereien wie [[Henkell & Söhnlein|Henkell]], [[Söhnlein]]"));
		Set set = wikiModel.getLinks();
		assertTrue(set.contains("Söhnlein"));
		assertTrue(set.contains("Henkell &amp; Söhnlein"));
	}

	public void testLink13() {
		assertEquals("\n" + 
				"<p>test [[lets start a <a href=\"http://www.bliki.info/wiki/Nested\" title=\"nested\">nested</a> link]] test</p>",
				wikiModel.render("test [[lets start a [[nested]] link]] test"));
		Set<String> set = wikiModel.getLinks();
		assertTrue(set.contains("nested"));
	}

	public void testInterwiki1() {
		assertEquals("\n" + "<p><a href=\"http://de.wikipedia.org/wiki/Johann_Wolfgang_von_Goethe\">Goethes</a> Faust</p>", wikiModel
				.render("[[de:Johann Wolfgang von Goethe|Goethe]]s Faust"));
	}

	public void testInterwiki2() {
		assertEquals("\n" + 
				"<p><a href=\"/page/directory\">Page directory</a></p>", wikiModel
				.render("[[intra:page/directory|Page directory]]"));
	}
	
	public void testSectionLink01() {
		assertEquals("\n" + 
				"<p><a href=\"#Section_Link\" title=\"\">A Section Link</a></p>", wikiModel
				.render("[[#Section Link|A Section Link]]"));
	}

	public void testSpecialLink01() {
		assertEquals("\n" + 
				"<ul>\n" + 
				"<li><a href=\"http://www.bliki.info/wiki/Special:Specialpages\" title=\"Special:Specialpages\">Special Pages</a></li></ul>", wikiModel
				.render("* [[Special:Specialpages|Special Pages]]"));
	}
	
	public void testSectionLink02() {
		assertEquals("\n" + 
				"<p><a href=\"#Section.C3.A4.C3.B6.C3.BC\" title=\"\" /></p>", wikiModel.render("[[#Sectionäöü]]"));
	}
	public void testSubLink01() {
		assertEquals("\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Test/testing\" title=\"test/testing\">test/testing</a></p>", wikiModel.render("[[test/testing]]"));
	}
	public void testRedirect01() {
		assertEquals("", wikiModel.render("#REDIRECT [[Official position]]"));
		assertEquals("Official position", wikiModel.getRedirectLink());
	}

	public void testRedirect02() {
		assertEquals("", wikiModel.render(" \r \n  #REDIRECT[[Official position]] bla \n other blabls"));
		assertEquals("Official position", wikiModel.getRedirectLink());
	}

	// public static void main(String[] args) {
	// String test = Encoder.encode("erreäöü öüä++", ".");
	// System.out.println(test);
	// }
}
