package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPListFilterTest extends FilterTestSupport {
	final public static String LIST0 = "*Mixed list\n" + "*# with numbers\n" + "** and bullets\n" + "*# and numbers\n"
			+ "*bullets again\n" + "**bullet level 2\n" + "***bullet level 3\n" + "***#Number on level 4\n" + "**bullet level 2\n"
			+ "**#Number on level 3\n" + "**#Number [[Level:1|one]]s level 3\n" + "*#number level 2\n" + "*Level 1";

	final public static String LIST1 = "*#*";

	final public static String LIST2 = "# first\n##second";

	final public static String LIST3 = "# test 1\n" + "# test 2\n" + "## test 3\n" + "hello\n" + "## test 4";

	final public static String LIST4 = "# first\n  <!-- stupid comment-->  \n#second";

	final public static String LIST4A = "# first\n<!-- stupid comment-->#second";

	final public static String LIST4B = "# first<!-- stupid comment-->\n#second";

	final public static String LIST4C = "# first\n  <!-- stupid comment-->  \n";

	final public static String LIST_CONTINUATION = "* ''Unordered lists'' are easy to do:\n" + "** Start every line with a star.\n"
			+ "*: Previous item continues.";

	public WPListFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPListFilterTest.class);
	}

	public void testWPList01() {
		String testString = "\n*#: a nested list\n";

		WikipediaScanner scanner = new WikipediaScanner(testString, 0);
		scanner.setModel(wikiModel);
		WPList wpList = scanner.wpList();

		assertEquals("*-#-:-5|18|*#:\n", wpList.toString());

		assertEquals("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n"
				+ "<dd>a nested list</dd></dl></li></ol></li></ul>", wikiModel.render(testString));

	}

	public void testWPList02() {
		String testString = "\n*#; a nested list 1\n*#: a nested list 2\n";

		WikipediaScanner scanner = new WikipediaScanner(testString, 0);
		scanner.setModel(wikiModel);
		WPList wpList = scanner.wpList();

		assertEquals("*-#-;-5|20|*#;\n" + "25|40|*#:\n", wpList.toString());

		assertEquals("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n" + "<dt>a nested list 1</dt>\n"
				+ "<dd>a nested list 2</dd></dl></li></ol></li></ul>", wikiModel.render(testString));

	}

	public void testList0() {
		assertEquals("\n" + "<ul>\n" + "<li>Mixed list\n" + "<ol>\n" + "<li>with numbers</li></ol>\n" + "<ul>\n"
				+ "<li>and bullets</li></ul>\n" + "<ol>\n" + "<li>and numbers</li></ol></li>\n" + "<li>bullets again\n" + "<ul>\n"
				+ "<li>bullet level 2\n" + "<ul>\n" + "<li>bullet level 3\n" + "<ol>\n"
				+ "<li>Number on level 4</li></ol></li></ul></li>\n" + "<li>bullet level 2\n" + "<ol>\n" + "<li>Number on level 3</li>\n"
				+ "<li>Number <a href=\"http://www.bliki.info/wiki/Level:1\" title=\"Level:1\">ones</a> level 3</li></ol></li></ul>\n"
				+ "<ol>\n" + "<li>number level 2</li></ol></li>\n" + "<li>Level 1</li></ul>", wikiModel.render(LIST0));
	}

	public void testList1() {
		assertEquals("\n" + "<p>*#*</p>", wikiModel.render(LIST1));
	}

	public void testList2() {
		assertEquals("\n" + "<ol>\n" + "<li>first\n" + "<ol>\n" + "<li>second</li></ol></li></ol>", wikiModel.render(LIST2));
	}

	public void testList3() {
		assertEquals("\n" + "<ol>\n" + "<li>test 1</li>\n" + "<li>test 2\n" + "<ol>\n" + "<li>test 3</li></ol></li></ol>\n"
				+ "<p>hello\n" + "</p>\n" + "<ol>\n" + "<li>\n" + "<ol>\n" + "<li>test 4</li></ol></li></ol>", wikiModel.render(LIST3));
	}

	public void testList4() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li></ol>", wikiModel.render(LIST4));
	}

	public void testList4A() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li></ol>", wikiModel.render(LIST4A));
	}

	public void testList4B() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li></ol>", wikiModel.render(LIST4B));
	}

	public void testList4C() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li></ol>", wikiModel.render(LIST4C));
	}

	public void testList10() {
		assertEquals("\n" + "<ul>\n" + "<li>a simple test\n" + "x+y\n" + "</li></ul>\n" + "<p>test test</p>", wikiModel
				.render("*a simple test<nowiki>\n" + "x+y\n" + "</nowiki>\n" + "test test"));
	}

	public void testList11() {
		assertEquals("\n" + "<ul>\n" + "<li>a simple test blabla</li></ul>\n" + "<p>x+y\n" + "test test</p>", wikiModel
				.render("*a simple test <nowiki>blabla\n" + "x+y\n" + "test test"));
	}

	public void testList12() {
		assertEquals("\n" + "<ul>\n" + "<li>*</li></ul>", wikiModel.render("* *"));
		assertEquals("\n" + "<ul>\n" + "<li>#</li></ul>", wikiModel.render("* #"));
		// TODO solve this wrong JUnit test
		// assertEquals("", wikiModel.render("* :*"));
	}

	public void testList13() {
		assertEquals("\n" + "test 1\n" + "test 2\n" + "test 3\n" + "hello\n" + "\n" + "\n" + "test 4\n" + "", wikiModel.render(
				new PlainTextConverter(), LIST3));
	}

	public void testList14() {
		assertEquals("\n" + 
				"\n" + 
				"<ul>\n" + 
				"<li>item 1\n" + 
				"<ol>\n" + 
				"<li>item 1.1</li>\n" + 
				"<li>item 1.2</li></ol></li>\n" + 
				"<li>item 2</li></ul>", wikiModel.render("\n" + "*item 1\n" + "*# item 1.1\n" + "*# item 1.2\n" + "* item 2"));
	} 
	
	public void testListContinuation01() {
		assertEquals("\n" + "<dl>\n" + "<dd><span>simple definition</span></dd></dl>", wikiModel
				.render(": <span>simple definition</span>"));
	}

	public void testListContinuation02() {
		assertEquals("\n" + "<ul>\n" + "<li><i>Unordered lists</i> are easy to do:\n" + "<ul>\n"
				+ "<li>Start every line with a star.</li></ul>\n" + "<dl>\n" + "<dd>Previous item continues.</dd></dl></li></ul>",
				wikiModel.render(LIST_CONTINUATION));
	}

	public void testListContinuation03() {
		assertEquals("\n" + "<ul>\n" + "<li>item 1\n" + "<ul>\n" + "<li>item 1.1</li></ul>\n" + "<dl>\n"
				+ "<dd>continuation I am indented just right</dd></dl></li>\n" + "<li>item 1.2</li>\n" + "<li>item 2\n" + "<dl>\n"
				+ "<dd>continuation I am indented too much</dd></dl>\n" + "<ul>\n" + "<li>item 2.1\n" + "<ul>\n"
				+ "<li>item 2.1.1</li></ul></li></ul>\n" + "<dl>\n" + "<dd>continuation I am indented too little</dd></dl></li></ul>",
				wikiModel.render("* item 1\n" + "** item 1.1\n" + "*:continuation I am indented just right\n" + "*item 1.2\n" + "*item 2\n"
						+ "*:continuation I am indented too much\n" + "**item 2.1\n" + "***item 2.1.1\n"
						+ "*:continuation I am indented too little"));
	}

	public void testListContinuation04() {
		assertEquals("\n" + "\n" + "<dl>\n" + "<dt>definition list 1</dt>\n" + "<dt>definition list 2</dt>\n"
				+ "<dd>definition list 3</dd>\n" + "<dd>definition list 4</dd></dl>", wikiModel.render("\n" + "; definition list 1\n"
				+ "; definition list 2\n" + ": definition list 3\n" + ": definition list 4"));
	}

	public void testListContinuation05() {
		assertEquals("\n" + "<dl>\n" + "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
				+ "<dd>too</dd></dl></dd></dl>", wikiModel.render("; definition lists\n" + ": can be \n" + ":; nested : too"));
	}

	public void testListContinuation06() {
		assertEquals("\n" + "<ul>\n" + "<li>You can even do mixed lists\n" + "<ol>\n" + "<li>and nest them</li>\n"
				+ "<li>inside each other\n" + "<ul>\n" + "<li>or break lines<br/>in lists.</li></ul>\n" + "<dl>\n"
				+ "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
				+ "<dd>too</dd></dl></dd></dl></li></ol></li></ul>", wikiModel.render("* You can even do mixed lists\n"
				+ "*# and nest them\n" + "*# inside each other\n" + "*#* or break lines<br>in lists.\n" + "*#; definition lists\n"
				+ "*#: can be \n" + "*#:; nested : too"));
	}

}