package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPTableFilterTest extends FilterTestSupport {
	// unclosed outer table:
	public static final String TEST = "{| width=\"100%\" cellspacing=\"3\" cellpadding=\"6\"   \n"
			+ "|- valign=\"top\"   \n"
			+ "| width=\"40%\" bgcolor=\"#FFF4F4\" style=\"border: solid 1px #ffc9c9; padding:1em;\" cellpadding=\"0\" cellspacing=\"0\" |   \n"
			+ "{| cellspacing=\"0\" cellpadding=\"0\"   \n" + "| bgcolor=\"#FFF4F4\" |       \n"
			+ "\'\'\'Plog4u.org\'\'\' is dedicated to developing a Wikipedia Eclipse Plugin\n" + "\n" + "|}   \n"
			+ "| width=\"60%\" bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" |   \n"
			+ "{| cellspacing=\"0\" cellpadding=\"6\"   \n" + "| bgcolor=\"#f0f0ff\" |\n" + "\n" + "|}\n" + "\n" + "";

	public static final String TEST2 = "{| style=&quot;width:100%; border-top: 1px solid black; border-bottom: 1px solid black;&quot;\n"
			+ "|style=&quot;width:60%; text-align:right; font-size: 100%;background:#EEEEEE; color: black; padding-left: 3px; padding-right: 3px;&quot;|\n"
			+ "\n"
			+ "[[Wikinews:Willkommen|Willkommen]]\n"
			+ "&lt;br /&gt;\n"
			+ "&lt;div style=&quot;white-space: nowrap;&quot;&gt; \n"
			+ "[[Wikinews:Schreibe eine Nachricht|Mach mit!]] |\n"
			+ "[[Hilfe:Erste Schritte|Erste Schritte]] |\n"
			+ "[[Hilfe:Zweite Schritte|Zweite Schritte]] |\n"
			+ "[[Wikinews:Ententeich|Ententeich]] | \n"
			+ "[[Hilfe:FAQ|FAQ]] |\n"
			+ "[[:Kategorie:!Hauptkategorie|Index]] | \n"
			+ "&lt;span class=&quot;plainlinks&quot;&gt;[http://tools.wikimedia.de/~dapete/wikinews-rss/rss-de.php&lt;span style=&quot;color: #FFFFFF; background-color: #FF5500; background-image: none !important; border-color: #FF5500; border-style: outset; text-decoration: none !important; padding-left: 0.2em; padding-right: 0.2em; border-width: 0.15em; font-size: 95%; line-height: 95%; font-family: verdana, sans-serif; font-weight: bold;&quot;&gt;RSS&lt;/span&gt;]&lt;/span&gt;\n"
			+ "|style=&quot;width:40%; padding-left: 10px; padding-top: 15px;&quot;|\n"
			+ "&lt;div style=&quot;font-size: 285%;&quot;&gt;&lt;span style=&quot;color: #444444;&quot;&gt;\'\'\'WIKI\'\'\'&lt;/span&gt;&lt;span style=&quot;color: #999999;&quot;&gt;\'\'\'NEWS\'\'\'&lt;/span&gt;&lt;/div&gt;\n"
			+ "&lt;span style=&quot;font-size: 90%;&quot;&gt;{{CURRENTDAY}}. {{CURRENTMONTHNAMEGEN}} {{CURRENTYEAR}} {{CURRENTTIME}} UTC &lt;/span&gt;\n"
			+ "|}";

	public final String TEST3 = "=== Simple example ===\n"
			+ "Both of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.\n"
			+ "\n" + "\'\'\'Wiki markup\'\'\'\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><pre><nowiki>\n" + "{| \n"
			+ "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "</nowiki></pre></blockquote>\n" + "\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><pre><nowiki>\n" + "{| \n"
			+ "| A || B\n" + "|- \n" + "| C || D \n" + "|}\n" + "</nowiki></pre></blockquote>\n" + "\n"
			+ "\'\'\'What it looks like in your browser\'\'\'\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "{| \n" + "| A\n"
			+ "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "</blockquote>\n";

	public final String TEST5 = "<table><tr></tr><tr><td>&#160;</td><td width=\"48%\">\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
			+ "{| class=\"wikitable\"\n" + "|+Multiplication table\n" + "|-\n" + "! &times; !! 1 !! 2 !! 3\n" + "|-\n" + "! 1\n"
			+ "| 1 || 2 || 3\n" + "|-\n" + "! 2\n" + "| 2 || 4 || 6\n" + "|-\n" + "! 3\n" + "| 3 || 6 || 9\n" + "|-\n" + "! 4\n"
			+ "| 4 || 8 || 12\n" + "|-\n" + "! 5\n" + "| 5 || 10 || 15\n" + "|}\n" + "</blockquote></td></tr></table>\n";

	public final String TEST6 = "=== Nested tables ===\n"
			+ "This shows one table (in blue) nested inside another table\'s cell2.  \'\'Nested tables have to start on a new line.\'\'\n"
			+ "\n"
			+ "\'\'\'Wiki markup\'\'\'\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><p style=\"border: 1px dashed #2f6fab; background-color: #f9f9f9;\"><tt>\n"
			+ "<nowiki>{|</nowiki> border=\"1\"<br />\n" + "| &amp;alpha;<br />\n" + "| align=\"center\" | cell2<br />\n"
			+ "<span style=\"color: navy;\">\n"
			+ "\'\'\'<nowiki>{|</nowiki> border=\"2\" style=\"background-color:#ABCDEF;\"\'\'\'<br />\n" + "\'\'\'| NESTED\'\'\'<br />\n"
			+ "\'\'\'|-\'\'\'<br />\n" + "\'\'\'| TABLE\'\'\'<br />\n" + "\'\'\'<nowiki>|}</nowiki>\'\'\'<br />\n" + "</span>\n"
			+ "| valign=\"bottom\" | the original table again<br />\n" + "<nowiki>|}</nowiki>\n" + "</tt></p></blockquote>\n" + "\n"
			+ "\'\'\'What it looks like in your browser\'\'\'\n"
			+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "{| border=\"1\"\n"
			+ "| &alpha;\n" + "| align=\"center\" | cell2\n" + "{| border=\"2\" style=\"background-color:#ABCDEF;\"\n" + "| NESTED\n"
			+ "|-\n" + "| TABLE\n" + "|}\n" + "| valign=\"bottom\" | the original table again\n" + "|}\n" + "</blockquote>\n" + "\n" + "";

	public WPTableFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPTableFilterTest.class);
	}

	// public void testNestedTable0() {
	// assertEquals("", wikiModel.render(TEST2));
	// }
	public void testNestedTable1() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n"
						+ "<table cellpadding=\"6\" cellspacing=\"3\" width=\"100%\">\n"
						+ "<tr valign=\"top\">\n"
						+ "<td bgcolor=\"#FFF4F4\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: solid 1px #ffc9c9; padding:1em;\" width=\"40%\">\n"
						+ "\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table cellpadding=\"0\" cellspacing=\"0\">\n" + "<tr>\n"
						+ "<td bgcolor=\"#FFF4F4\">\n" + "<b>Plog4u.org</b> is dedicated to developing a Wikipedia Eclipse Plugin\n"
						+ "</td></tr></table></div>   </td>\n"
						+ "<td bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" width=\"60%\">\n" + "\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n" + "<table cellpadding=\"6\" cellspacing=\"0\">\n" + "<tr>\n"
						+ "<td bgcolor=\"#f0f0ff\">\n" + "</td></tr></table></div>\n" + "\n" + "</td></tr></table></div>", wikiModel
						.render(TEST));
	}

	public void testNestedTable2() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
				+ "<td><a href=\"http://www.bliki.info/wiki/Test_Link\" title=\"Test Link\">Test</a></td>\n" + "<td>&#945;</td>\n"
				+ "<td>\n" + "\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table bgcolor=\"#ABCDEF\" border=\"2\">\n" + "<tr>\n"
				+ "<td>nested</td></tr>\n" + "<tr>\n" + "<td>table</td></tr></table></div></td>\n"
				+ "<td>the original table again</td></tr></table></div>", wikiModel.render("{| border=1\n" + "|[[Test Link|Test]]\n"
				+ "| &alpha;\n" + "|\n" + "{| bgcolor=#ABCDEF border=2\n" + "|nested\n" + "|-\n" + "|table\n" + "|}\n"
				+ "|the original table again\n" + "|}"));
	}

	public void testBlockquoteTable01() {
		assertEquals(
				"<a id=\"Simple_example\" name=\"Simple_example\"></a><h3>Simple example</h3>\n"
						+ "<p>Both of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.</p>\n"
						+ "<p><b>Wiki markup</b>\n"
						+ "</p><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "<pre>\n"
						+ "{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "</pre></blockquote>\n"
						+ "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "<pre>\n"
						+ "{| \n" + "| A || B\n" + "|- \n" + "| C || D \n" + "|}\n" + "</pre></blockquote>\n" + "\n"
						+ "<p><b>What it looks like in your browser</b>\n"
						+ "</p><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A</td>\n" + "<td>B</td></tr>\n"
						+ "<tr>\n" + "<td>C</td>\n" + "<td>D</td></tr></table></div></blockquote>\n" + "", wikiModel.render(TEST3));
	}

	public void testBlockquoteTable02() {
		assertEquals("\n" + "<table>\n" + "\n" + "<tr>\n" + " \n" + "\n" + "<td />\n" + "\n" + "\n" + "</tr>\n" + "\n" + "</table>\n"
				+ "", wikiModel.render("<table><tr> \n" + "<td></td>\n\n</tr>\n</table>\n"));
	}

	public void testBlockquoteTable03() {
		assertEquals("\n" + "<table>\n" + "\n" + "<tr>\n" + " \n" + "\n" + "<td />\n" + "</tr>\n" + "<tr>\n" + "\n" + "<td />\n"
				+ "</tr>\n" + "</table>\n" + "", wikiModel.render("<table><tr> \n" + "<td></td><tr><td></tr></table>\n"));
	}

	public void testBlockquoteTable04() {
		assertEquals("\n" + "<table>\n" + "\n" + "<tr>\n" + " \n" + "\n" + "<td>number 1</td>\n" + "</tr>\n" + "<tr>\n" + "\n"
				+ "<td>number2\n" + "</td>\n" + "</tr>\n" + "</table>", wikiModel.render("<table><tr> \n"
				+ "<td>number 1<tr><td>number2</table>\n"));
	}

	public void testBlockquoteTable05() {
		assertEquals("\n" + "<table>\n" + "\n" + "<tr />\n" + "<tr>\n" + "\n" + "<td> </td>\n"
				+ "<td width=\"48%\"><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
				+ "\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table class=\"wikitable\">\n"
				+ "<caption>Multiplication table</caption>\n" + "<tr>\n" + "<th>&#215; </th>\n" + "<th>1 </th>\n" + "<th>2 </th>\n"
				+ "<th>3</th></tr>\n" + "<tr>\n" + "<th>1</th>\n" + "<td>1 </td>\n" + "<td>2 </td>\n" + "<td>3</td></tr>\n" + "<tr>\n"
				+ "<th>2</th>\n" + "<td>2 </td>\n" + "<td>4 </td>\n" + "<td>6</td></tr>\n" + "<tr>\n" + "<th>3</th>\n" + "<td>3 </td>\n"
				+ "<td>6 </td>\n" + "<td>9</td></tr>\n" + "<tr>\n" + "<th>4</th>\n" + "<td>4 </td>\n" + "<td>8 </td>\n"
				+ "<td>12</td></tr>\n" + "<tr>\n" + "<th>5</th>\n" + "<td>5 </td>\n" + "<td>10 </td>\n"
				+ "<td>15</td></tr></table></div></blockquote></td>\n" + "</tr>\n" + "</table>\n" + "", wikiModel.render(TEST5));
	}

	public void testBlockquoteTable06() {
		assertEquals("\n" + "<table align=\"center\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\">\n" + "\n" + "  \n" + "<tr>\n"
				+ "\n" + "     \n" + "<td>1</td>\n" + "     \n" + "<td>2</td>\n" + "  \n" + "</tr> \n" + "  \n" + "<tr>\n" + "\n"
				+ "     \n" + "<td>3</td> \n" + "     \n" + "<td>4</td> \n" + "  \n" + "</tr>\n" + "\n" + "</table>\n" + "", wikiModel
				.render("<table align=\"center\" border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n" + "   <tr>\n" + "      <td>1</td>\n"
						+ "      <td>2</td>\n" + "   </tr> \n" + "   <tr>\n" + "      <td>3</td> \n" + "      <td>4</td> \n" + "   </tr>\n"
						+ "</table>\n"));
	}

	// public void testBlockquoteTable07() {
	// assertEquals("", wikiModel.render(TEST6));
	// }

	public void testMathTable1() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
				+ "<td><span class=\"math\">\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</span></td></tr></table></div>",
				wikiModel.render("{| border=1\n" + "|-\n" + "|<math>\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</math>\n"
						+ "|}"));
	}

	public void testAll() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n"
						+ "<table cellpadding=\"6\" cellspacing=\"3\" width=\"100%\">\n"
						+ "<tr valign=\"top\">\n"
						+ "<td bgcolor=\"#FFF4F4\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: solid 1px #ffc9c9; padding:1em;\" width=\"40%\">\n"
						+ "\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table cellpadding=\"0\" cellspacing=\"0\">\n" + "<tr>\n"
						+ "<td bgcolor=\"#FFF4F4\">\n" + "<b>Plog4u.org</b> is dedicated to developing a Wikipedia Eclipse Plugin\n"
						+ "</td></tr></table></div>   </td>\n"
						+ "<td bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" width=\"60%\">\n" + "\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n" + "<table cellpadding=\"6\" cellspacing=\"0\">\n" + "<tr>\n"
						+ "<td bgcolor=\"#f0f0ff\">\n" + "</td></tr></table></div>\n" + "\n" + "</td></tr></table></div>", wikiModel
						.render(TEST));

		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
				+ "<td><a href=\"http://www.bliki.info/wiki/Test_Link\" title=\"Test Link\">Test</a></td>\n" + "<td>&#945;</td>\n"
				+ "<td>\n" + "\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table bgcolor=\"#ABCDEF\" border=\"2\">\n" + "<tr>\n"
				+ "<td>nested</td></tr>\n" + "<tr>\n" + "<td>table</td></tr></table></div></td>\n"
				+ "<td>the original table again</td></tr></table></div>", wikiModel.render("{| border=1\n" + "|[[Test Link|Test]]\n"
				+ "| &alpha;\n" + "|\n" + "{| bgcolor=#ABCDEF border=2\n" + "|nested\n" + "|-\n" + "|table\n" + "|}\n"
				+ "|the original table again\n" + "|}"));

		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
				+ "<td><span class=\"math\">\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</span></td></tr></table></div>",
				wikiModel.render("{| border=1\n" + "|-\n" + "|<math>\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</math>\n"
						+ "|}"));

		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table class=\"wikitable\" style=\"text-align:left\">\n"
				+ "<caption align=\"bottom\">My special table caption for &#60;Name of Topic&#62;</caption>\n" + "<tr>\n"
				+ "<td><b>Start</b></td>\n" + "<td colspan=\"3\">&#60;From Date - To Date&#62;</td></tr></table></div>", wikiModel
				.render("{| class=\"wikitable\" style=\"text-align:left\"\n"
						+ "|+align=\"bottom\"|My special table caption for {{{TopicName|<Name of Topic>}}}\n" + "|-\n" + "|\'\'\'Start\'\'\'\n"
						+ "| colspan=\"3\" | {{{Period|<From Date - To Date>}}}\n" + "|-\n" + "|}"));
	}

	public void testTHTableMix001() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside:	avoid;\">\n"
						+ "<table class=\"infobox bordered vcard\" style=\"width: 25em; text-align: left; font-size: 95%;\">\n"
						+ "<tr>\n"
						+ "<th class=\"fn\" colspan=\"2\" style=\"text-align:center; font-size:larger;\">Chris Capuano</th></tr>\n"
						+ "<tr>\n"
						+ "<th colspan=\"2\" style=\"text-align:center;\">\n"
						+ "<div style=\"width:300px\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:300px-Cap.jpg\" ><img src=\"http://www.bliki.info/wiki/300px-Cap.jpg\" class=\"location-none\" width=\"300\" />\n"
						+ "</a></div>\n"
						+ "<br/>\n"
						+ "</th></tr>\n"
						+ "<tr>\n"
						+ "<td colspan=\"2\" style=\"text-align:center; background: #042462;\"><span class=\"note\" style=\"color:white;\"><b><span style=\"color:white;\">Milwaukee Brewers</span> — No. 39</b></span></td></tr>\n"
						+ "<tr style=\"text-align: center;\">\n"
						+ "<td colspan=\"2\"><b><a href=\"http://www.bliki.info/wiki/Starting_pitcher\" title=\"Starting pitcher\">Starting pitcher</a></b></td></tr>\n"
						+ "<tr>\n"
						+ "<th colspan=\"2\" style=\"text-align:center;\">\n"
						+ "Born: <a href=\"http://www.bliki.info/wiki/18\" title=\"18\">18</a> <a href=\"http://www.bliki.info/wiki/1978\" title=\"1978\">1978</a><span style=\"display:none\"> (<span class=\"bday\">1978-8-18</span>)</span><span class=\"noprint\"> (age&#160;29)</span>\n"
						+ "</th> </tr></table></div>",
				wikiModel
						.render("{| class=\"infobox bordered vcard\" style=\"width: 25em; text-align: left; font-size: 95%;\"\n"
								+ "! colspan=\"2\" style=\"text-align:center; font-size:larger;\" class=\"fn\"| Chris Capuano\n"
								+ "|-\n"
								+ "<th colspan=\"2\" style=\"text-align:center;\">[[Image:Cap.jpg|300px]]<br></th>\n"
								+ "|-\n"
								+ "| colspan=\"2\" style=\"text-align:center; background: #042462;\"| <span style=\"color:white;\" class=\"note\">'''<span style=\"color:white;\">Milwaukee Brewers</span> — No. 39'''</span>\n"
								+ "|- style=\"text-align: center;\"\n" + "| ! colspan=\"2\" | '''[[Starting pitcher]]'''\n"
								+ "|- style=\"text-align: center;\"\n" + "\n" + "|-\n"
								+ "<th colspan=\"2\" style=\"text-align:center;\">Born: [[18]] [[1978]]<span\n"
								+ "style=\"display:none\"> (<span class=\"bday\">1978-{{padleft:8}}-{{padleft:\n"
								+ "18}}</span>)</span><span class=\"noprint\"> (age&nbsp;29)</span></th> \n" + "|-\n" + "|}"));
	}

	public void testEmptyCellTable() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n" + "<td></td>\n"
				+ "<td>a.</td></tr>\n" + "<tr>\n" + "<td></td>\n" + "<td>b.</td></tr></table></div>\n" + "", wikiModel
				.render("{|border=\"1\"\n" + "|\n" + "|a.\n" + "|-\n" + "|\n" + "|b.\n" + "|}\n" + ""));
	}

	public void testBlockquoteTableText01() {
		assertEquals(
				"Simple exampleBoth of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.Wiki markup\n"
						+ "<nowiki>\n"
						+ "{| \n"
						+ "| A \n"
						+ "| B\n"
						+ "|- \n"
						+ "| C\n"
						+ "| D\n"
						+ "|}\n"
						+ "</nowiki>\n"
						+ "<nowiki>\n"
						+ "{| \n"
						+ "| A || B\n"
						+ "|- \n"
						+ "| C || D \n"
						+ "|}\n"
						+ "</nowiki>\n"
						+ "What it looks like in your browser\n"
						+ "\n" + "\n" + "", wikiModel.render(new PlainTextConverter(), TEST3));
	}

	public void testWPTableText01() {
		assertEquals("\n" + "\n" + "\n" + "\n" + "Plog4u.org is dedicated to developing a Wikipedia Eclipse Plugin\n" + "     \n"
				+ "\n" + "\n" + " \n" + "\n" + " ", wikiModel.render(new PlainTextConverter(), TEST));
	}

	// public void testWPTable02() {
	// String WIKIPEDIA =
	// "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\">\n"
	// +
	// "<tr>\n" +
	// "<th>Year</th>\n" +
	// "<th>Make</th>\n" +
	// "<th>Model</th>\n" +
	// "<th>Description</th>\n" +
	// "</tr>\n" +
	// "<tr>\n" +
	// "\n" +
	// "<td>69</td>\n" +
	// "<td>Plymouth</td>\n" +
	// "<td>Road Runner</td>\n" +
	// "<td>very fast</td>\n" +
	// "</tr>\n" +
	// "<tr>\n" +
	// "<td>98</td>\n" +
	// "<td>Honda</td>\n" +
	// "<td>Accord</td>\n" +
	// "<td>Very efficient\n" +
	// "\n" +
	// "<p><b>This text should be on a new line</b></p>\n" +
	// "</td>\n" +
	// "</tr>\n" +
	// "</table>\n" +
	// "";
	// String test =
	// "{| border=\"1\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\"\n" +
	// "!Year\n" +
	// "!Make\n" +
	// "!Model\n" +
	// "!Description\n" +
	// "|-\n" +
	// "|69\n" +
	// "|Plymouth\n" +
	// "|Road Runner\n" +
	// "|very fast\n" +
	// "|-\n" +
	// "|98\n" +
	// "|Honda\n" +
	// "|Accord\n" +
	// "|Very efficient\n" +
	// "'''This text should be on a new line'''\n" +
	// "|}";
	// assertEquals(WIKIPEDIA, wikiModel.render(test));
	// }
	//	
	// public void testWPTable03() {
	// String WIKIPEDIA = "";
	// String test =
	// "Very efficient\n\n" +
	// "'''This text should be on a new line'''\n";
	// assertEquals(WIKIPEDIA, wikiModel.render(test));
	// }
}