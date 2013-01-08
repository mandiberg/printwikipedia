package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TOCFilterTest extends FilterTestSupport {
	public TOCFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TOCFilterTest.class);
	}

	public void testTOC01() {
		assertEquals("\n" + 
				"<div style=\"page-break-inside:	avoid;\">\n" + 
				"<table align=\"right\">\n" + 
				"<tr>\n" + 
				"<td><table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#hello_world_2\">hello world 2</a>\n" + 
				"</li>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world3\">hello world3</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				" </td></tr></table></div>\n" + 
				"\n" + 
				"<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<a id=\"hello_world3\" name=\"hello_world3\"></a><h3>hello world3</h3>\n" + 
				"<p>hello world 3</p>", wikiModel
				.render("{| align=\"right\" \n" + "| __TOC__ \n" + "|}\n" + "\n" + "==hello world 2==\n" + "hello world 2\n" + "\n"
						+ "===hello world3===\n" + "hello world 3"));
	}

	public void testTOC02() {
		assertEquals("\n" + "<div style=\"page-break-inside:	avoid;\">\n" + "<table align=\"right\">\n" + "<tr>\n"
				+ "<td><table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + "<tr>\n" + "<td>\n" + "<div id=\"toctitle\">\n"
				+ "<h2>Contents</h2>\n" + "</div>\n" + "<ul>\n" + "<li class=\"toclevel-1\"><a href=\"#hello_world_1\">hello world 1</a>\n"
				+ "</li>\n" + "<ul>\n" + "<li class=\"toclevel-2\"><a href=\"#hello_world_2\">hello world 2</a>\n" + "</li>\n"
				+ "<li class=\"toclevel-2\"><a href=\"#hello_world1a\">hello world1a</a>\n" + "</li>\n" + "</ul>\n"
				+ "</ul></td></tr></table><hr/>\n" + "</td></tr></table></div>\n" + "\n"
				+ "<a id=\"hello_world_1\" name=\"hello_world_1\"></a><h1>hello world 1</h1>\n" + "<p>hello world 1</p>\n"
				+ "<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + "<p>hello world 2</p>\n"
				+ "<a id=\"hello_world1a\" name=\"hello_world1a\"></a><h2>hello world1a</h2>\n" + "<p>hello world 1a\n" + "</p>", wikiModel
				.render("{| align=\"right\"\n" + "| __TOC__\n" + "|}\n" + "\n" + "=hello world 1=\n" + "hello world 1\n" + "\n"
						+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world1a==\n" + "hello world 1a\n" + ""));
	}

	public void testTOC03() {
		assertEquals("<a id=\"hello_world_1\" name=\"hello_world_1\"></a><h1>hello world 1</h1>\n" + "<p>hello world 1</p>\n"
				+ "<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + "<p>hello world 2\n" + "</p>", wikiModel
				.render("=hello world 1=\n" + "hello world 1\n" + "\n" + "==hello world 2==\n" + "hello world 2\n"));
	}

	public void testTOC04() {
		assertEquals("<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#hello_world_1\">hello world 1</a>\n" + 
				"</li>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world_2\">hello world 2</a>\n" + 
				"</li>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world_3\">hello world 3</a>\n" + 
				"</li>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-3\"><a href=\"#hello_world_4\">hello world 4</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"<a id=\"hello_world_1\" name=\"hello_world_1\"></a><h1>hello world 1</h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<a id=\"hello_world_3\" name=\"hello_world_3\"></a><h2>hello world 3</h2>\n" + 
				"<p>hello world 3\n" + 
				"</p><a id=\"hello_world_4\" name=\"hello_world_4\"></a><h3>hello world 4</h3>\n" + 
				"<p>hello world 4\n" + 
				"</p>", wikiModel.render("=hello world 1=\n" + "hello world 1\n" + "\n"
				+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world 3==\n" + "hello world 3\n" + "===hello world 4===\n"
				+ "hello world 4\n"));
	}

	public void testTOC05() {
		assertEquals(" \n" + 
				"<a id=\"hello_world_1\" name=\"hello_world_1\"></a><h1>hello world 1</h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<a id=\"hello_world_3\" name=\"hello_world_3\"></a><h2>hello world 3</h2>\n" + 
				"<p>hello world 3\n" + 
				"</p><a id=\"hello_world_4\" name=\"hello_world_4\"></a><h3>hello world 4</h3>\n" + 
				"<p>hello world 4\n" + 
				"</p>", wikiModel.render("__NOTOC__ \n" + "=hello world 1=\n" + "hello world 1\n" + "\n"
				+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world 3==\n" + "hello world 3\n" + "===hello world 4===\n"
				+ "hello world 4\n"));
	} 
	 
	public void testTOC06() {
		assertEquals("<a id=\"hello_world_1\" name=\"hello_world_1\"></a><h1>hello world 1</h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<a id=\"hello_world_2\" name=\"hello_world_2\"></a><h2>hello world 2</h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<a id=\"hello_world_.C3.9Cbersicht\" name=\"hello_world_.C3.9Cbersicht\"></a><h2>hello world Übersicht</h2>\n" + 
				"<p>hello world Übersicht\n" + 
				"</p>", wikiModel.render("=hello world 1=\n" + "hello world 1\n" + "\n"
				+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world Übersicht==\n" + "hello world Übersicht\n" ));
	}
}