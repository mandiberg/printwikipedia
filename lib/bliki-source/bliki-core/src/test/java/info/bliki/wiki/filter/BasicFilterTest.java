package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BasicFilterTest extends FilterTestSupport {
	public BasicFilterTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public static Test suite() {
		return new TestSuite(BasicFilterTest.class);
	}

	public void testTT() {
		assertEquals("\n" + "<p><b>hosted by:</b><br/></p>", wikiModel.render("'''hosted by:'''<br>"));
	}

	public void testBlankInput() {
		assertEquals("", wikiModel.render(""));
	}

	public void testNullInput() {
		assertEquals("", wikiModel.render(null));
	}

	public void testCharInput() {
		assertEquals("\n" + "<p>[</p>", wikiModel.render("["));
	}

	public void testParagraph1() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>", wikiModel.render("This is a simple paragraph."));
	}

	public void testParagraph2() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>", wikiModel
				.render("This is a simple paragraph.\n\nA second paragraph."));
	}

	public void testParagraph3() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>", wikiModel
				.render("This is a simple paragraph.\n\nA second paragraph."));
	}

	public void testNowiki01() {
		assertEquals("\n" + "<p>\n" + "* This is not an unordered list item.</p>", wikiModel
				.render("<nowiki>\n* This is not an unordered list item.</nowiki>"));
	}

	public void testNowiki02() {
		assertEquals("\n" + "<p>\n" + "* This is not an unordered list item.</p>", wikiModel
				.render("<noWiki>\n* This is not an unordered list item.</nowiKi>"));
	}

	public void testSimpleList() {
		assertEquals("\n" + "<ul>\n" + "<li>Item 1</li>\n" + "<li>Item 2</li></ul>", wikiModel.render("* Item 1\n" + "* Item 2"));
	}

	public void testSimpleTable() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>a</td>\n"
				+ "<td>b</td></tr></table></div>", wikiModel.render("{|\n" + "|a\n|b\n" + "|}"));
	}

	public void testNOTOC() {
		assertEquals("\n" + "<p>jhfksd  sflkjsd</p>", wikiModel.render("jhfksd __NOTOC__ sflkjsd"));
	}

	public void testWrongNOTOC() {
		assertEquals("\n" + "<p>jhfksd __WRONGTOC__ sflkjsd</p>", wikiModel.render("jhfksd __WRONGTOC__ sflkjsd"));
	}

	public void testbq1() {
		assertEquals(
				"<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
						+ "<p><b>Hello World</b></p></blockquote>",
				wikiModel
						.render("<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n'''Hello World'''</blockquote>"));
	}

	public void testbq2() {
		assertEquals("<blockquote>\n" + "<p>The <b>blockquote</b> command formats block \n"
				+ "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n"
				+ "</p></blockquote>\n" + "", wikiModel.render("<blockquote>\n" + "The \'\'\'blockquote\'\'\' command formats block \n"
				+ "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n" + "</blockquote>\n"));
	}

	public void testbq3() {
		assertEquals("<blockquote>start blockquote here\n" + "\n" + "<p>line above me\n"
				+ "no line above me and i am <b>bold</b></p>\n" + "\n" + "<p>and line above me\n"
				+ "end of blockquote here</p></blockquote> ", wikiModel.render("<blockquote>start blockquote here\n" + "\n"
				+ "line above me\n" + "no line above me and i am <b>bold</b>\n" + "\n" + "\n" + "and line above me\n"
				+ "end of blockquote here</blockquote> "));
	}

	public void testPreBlock() {
		assertEquals("\n<pre>\n* Lists are easy to do:\n" + "** start every line\n" + "* with a star\n" + "** more stars mean\n"
				+ "*** deeper levels\n</pre>", wikiModel.render(" * Lists are easy to do:\n" + " ** start every line\n" + " * with a star\n"
				+ " ** more stars mean\n" + " *** deeper levels"));
	}

	public void testNestedPreBlock() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\" width=\"79%\">\n" + "<tr>\n"
				+ "<th>wikitext</th></tr>\n" + "<tr>\n" + "<td>\n" + "<pre>\n* Lists are easy to do:\n" + "** start every line\n"
				+ "* with a star\n" + "** more stars mean\n" + "*** deeper levels\n</pre></td></tr></table></div>", wikiModel
				.render("{|border=1 width=\"79%\"\n" + "!wikitext\n" + "|-\n" + "|\n" + " * Lists are easy to do:\n"
						+ " ** start every line\n" + " * with a star\n" + " ** more stars mean\n" + " *** deeper levels\n" + "|}"));
	}

	public void testPBlock() {
		assertEquals(
				"\n"
						+ "<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
						+ "&#60;p style=&#34;padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;&#34;&#62; &#60;tt&#62; <br/>\n"
						+ "&#38;#123;&#38;#124; border=&#34;5&#34; cellspacing=&#34;5&#34; cellpadding=&#34;2&#34; &#60;br&#160;/&#62; <br/>\n"
						+ "&#38;#124; style=&#34;text-align: center;&#34; &#38;#124; &#38;#91;&#38;#91;Image:gnome-system.png]] &#60;br&#160;/&#62; <br/>\n"
						+ "&#38;#124;- &#60;br&#160;/&#62; <br/>\n"
						+ "&#38;#33; Computer &#60;br&#160;/&#62; <br/>\n"
						+ "&#38;#124;- &#60;br&#160;/&#62; <br/>\n"
						+ "<b>&#38;#124; style=&#34;color: yellow; background-color: green;&#34; &#38;#124; Processor Speed: &#38;#60;span style=&#34;color: red;&#34;&#62; 1.8 GHz &#38;#60;/span&#62; &#60;br&#160;/&#62;</b> <br/>\n"
						+ "&#38;#124;&#38;#125; &#60;br&#160;/&#62; <br/>\n" + "&#60;/tt&#62; &#60;/p&#62;\n" + "</tt> </p>",
				wikiModel
						.render("<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
								+ "&#60;p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> &#60;tt> <br />\n"
								+ "&amp;#123;&amp;#124; border=\"5\" cellspacing=\"5\" cellpadding=\"2\" &#60;br&nbsp;&#47;> <br />\n"
								+ "&amp;#124; style=\"text-align: center;\" &amp;#124; &amp;#91;&amp;#91;Image:gnome-system.png]] &#60;br&nbsp;&#47;> <br />\n"
								+ "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
								+ "&amp;#33; Computer &#60;br&nbsp;&#47;> <br />\n"
								+ "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
								+ "\'\'\'&amp;#124; style=\"color: yellow; background-color: green;\" &amp;#124; Processor Speed: &amp;#60;span style=\"color: red;\"> 1.8 GHz &amp;#60;/span> &#60;br&nbsp;&#47;>\'\'\' <br />\n"
								+ "&amp;#124;&amp;#125; &#60;br&nbsp;&#47;> <br />\n" + "&#60;/tt> &#60;/p>\n" + "</tt> </p>"));
	}

	public void testALink001() {
		assertEquals("\n" + "<p><a href=\"http://www.test.com\" rel=\"nofollow\">Test2</a></p>", wikiModel
				.render("<a href=\"http://www.test.com\">Test2</a>"));
	}

	public void testXSS001() {
		assertEquals("<h1>Test</h1>", wikiModel.render("<h1 onmouseover=\"javascript:alert(\'yo\')\">Test</h1>"));
	}

	public void testSignature01() {
		assertEquals("\n" + "<p>a simple~~~~test</p>", wikiModel.render("a simple~~~~test"));
	}

	public void testSignature02() {
		assertEquals("\n" + "<p>a simple~~~~</p>", wikiModel.render("a simple~~~~"));
	}

	public void testSignature03() {
		assertEquals("\n" + "<p>a simple~~~~~test</p>", wikiModel.render("a simple~~~~~test"));
	}

	public void testSignature04() {
		assertEquals("\n" + "<p>a simple~~~~~</p>", wikiModel.render("a simple~~~~~"));
	}

	public void testSignature05() {
		assertEquals("\n" + "<p>a simple~~~test</p>", wikiModel.render("a simple~~~test"));
	}

	public void testSignature06() {
		assertEquals("\n" + "<p>a simple~~~</p>", wikiModel.render("a simple~~~"));
	}
	
	public void testSignature07() {
		assertEquals("\n" + "<p>~~~test</p>", wikiModel.render("~~~test"));
	}

	public void testSignature08() {
		assertEquals("\n" + "<p>~~~</p>", wikiModel.render("~~~"));
	}
	
	public void testSpan001() {
		
		assertEquals("\n" + 
				"<p><span class=\"xxx\">test</span></p>", wikiModel.render("<span class=\"xxx\"\n" + 
		">test</span>"));
		
	}
}