package info.bliki.wiki.filter;

import info.bliki.wiki.model.SemanticAttribute;
import info.bliki.wiki.model.SemanticRelation;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPSemanticLinkTest extends FilterTestSupport {
	public WPSemanticLinkTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPSemanticLinkTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wikiModel.setSemanticWebActive(true);
	}

	/*
	 * Test a semantic relation
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 */
	public void testLink01() {

		assertEquals(
				"\n"
						+ "<p>Berlin is the capital of <a class=\"interwiki\" href=\"http://www.bliki.info/wiki/Germany\" title=\"Germany\">Germany</a>.</p>",
				wikiModel.render("Berlin is the capital of [[Is capital of::Germany]]."));
		List<SemanticRelation> list = wikiModel.getSemanticRelations();
		SemanticRelation rel = list.get(0);
		assertTrue(rel.getRelation().equals("Is capital of"));
		assertTrue(rel.getValue().equals("Germany"));
	}

	/*
	 * Test a semantic attribute
	 * 
	 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
	 * MediaWiki</a> for more information.
	 */
	public void testLink02() {

		assertEquals("\n<p>The population is 3,993,933.</p>", wikiModel.render("The population is [[Has population:=3,993,933]]."));
		List<SemanticAttribute> list = wikiModel.getSemanticAttributes();
		SemanticAttribute rel = list.get(0);
		assertTrue(rel.getAttribute().equals("Has population"));
		assertTrue(rel.getValue().equals("3,993,933"));
	}

	public void testLink03() {

		assertEquals(
				"\n"
						+ "<p>Make <a class=\"interwiki\" href=\"http://www.bliki.info/wiki/Link\" title=\"link\">alternate text</a> appear in place of the link.</p>",
				wikiModel.render("Make [[example relation::link|alternate text]] appear in place of the link."));
		List<SemanticRelation> list = wikiModel.getSemanticRelations();
		SemanticRelation rel = list.get(0);
		assertTrue(rel.getRelation().equals("example relation"));
		assertTrue(rel.getValue().equals("link"));
	}

	public void testLink04() {

		assertEquals("\n<p>To hide the property  from appearing at all</p>", wikiModel
				.render("To hide the property [[    example relation::link   | ]] from appearing at all"));
		List<SemanticRelation> list = wikiModel.getSemanticRelations();
		SemanticRelation rel = list.get(0);
		assertTrue(rel.getRelation().equals("example relation"));
		assertTrue(rel.getValue().equals("link"));
	}

	public void testLink05() {

		assertEquals("\n" + 
				"<p>The <a href=\"http://www.bliki.info/wiki/C%2B%2B_::_operator\" title=\"C++ :: operator\">C++ :: operator</a>.</p>", wikiModel
				.render("The [[:C++ :: operator]]."));
		List<SemanticRelation> list = wikiModel.getSemanticRelations();
		assertTrue(list == null);
	}
}
