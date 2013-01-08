package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BoldFilterTest extends FilterTestSupport {
	public BoldFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(BoldFilterTest.class);
	}

	public void testBoldItalic() {
		// close tags if user forget it:
		assertEquals("\n" + "<p><b><i>kursiv und fett</i></b><br/>\n" + "<br/>\n" + "test</p>", wikiModel
				.render("'''''kursiv und fett'''''<br />\n" + "<br />\n" + "test"));
	}

	public void testBold00() {
		assertEquals("\n" + "<p><b>&#38;</b></p>", wikiModel.render("'''&amp;'''"));
	}

	public void testBold01() {
		assertEquals("\n<p><b>Text</b></p>", wikiModel.render("'''Text'''"));
	}

	public void testBold02() {
		assertEquals("\n" + "<p><b>Text1</b><i>&#39;&#38;</i></p>", wikiModel.render("'''Text1''''''&amp;'''"));
	}

	public void testBold03() {
		// close tags if user forget it:
		assertEquals("\n" + "<p><b>Text</b></p>", wikiModel.render("'''Text''"));
	}

	public void testBold04() {
		// see bug #1860386 - bug in emphasize with 4 apostrophes
		assertEquals("\n" + "<p>some<b>&#39;emphasized</b>&#39;text</p>", wikiModel.render("some''''emphasized''''text"));
	}

	public void testBold05() {
		assertEquals("\n" + "<p>some<b><i>&#39;emphasized</i></b>&#39;text</p>", wikiModel.render("some''''''emphasized''''''text"));
	}

	public void testBoldItalicStack() {
		// close tags if user forget it:
		assertEquals("\n" + "<p><b>Text<i>hallo</i></b></p>", wikiModel.render("<b>Text<i>hallo"));
	}

	public void testBoldItalic2() {
		// close tags if user forget it:
		assertEquals("\n" + "<p>Ein kleiner <b><i>Text</i></b><b> 2 3 4 </b> .</p>", wikiModel
				.render("Ein kleiner '''''Text'' 2 3 4 ''' ."));
	}

	public void testBoldItalic3() {
		// close tags if user forget it:
		assertEquals("\n" + "<p>Ein kleiner <b><i>Text</i></b><i> 2 3 4 </i> .</p>", wikiModel
				.render("Ein kleiner '''''Text''' 2 3 4 '' ."));
	}

	public void testBoldItalic4() {
		// close tags if user forget it:
		assertEquals("\n" + "<p>Ein kleiner <b>Text<i> 2 3 4 </i></b> .</p>", wikiModel.render("Ein kleiner '''Text'' 2 3 4 ''''' ."));
	}

	public void testBoldWithPunctuation() {
		assertEquals("\n" + "<p><b>Text</b>:</p>", wikiModel.render("'''Text''':"));
	}
	 
	public void testOpenBold() {
		assertEquals("\n" + 
				"<p>para test <b>bold</b>\n" + 
				"next para</p>", wikiModel.render("para test '''bold\n" + 
				"next para"));
	}
}