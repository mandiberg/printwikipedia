package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PreFilterTest extends FilterTestSupport {
	public PreFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(PreFilterTest.class);
	}

	public void testPre0() {
		assertEquals("\n" + 
				"<p>\n" + 
				"The nowiki tag ignores [[Wiki]] &#39;&#39;markup&#39;&#39;.\n" + 
				"It reformats text by removing\n" + 
				"newlines    and multiple spaces.\n" + 
				"It still interprets special\n" + 
				"characters: &#38; \n" + 
				"</p>", wikiModel.render("<nowiki>\n" + 
				"The nowiki tag ignores [[Wiki]] ''markup''.\n" + 
				"It reformats text by removing\n" + 
				"newlines    and multiple spaces.\n" + 
				"It still interprets special\n" + 
				"characters: & \n" + 
				"</nowiki>"));
	}

	public void testPre1() {
		assertEquals("\n" + 
				"<p>First line:</p>\n" + 
				"<pre>\n" + 
				"pre text 1\n" + 
				"pre text 2\n" + 
				"</pre>\n" + 
				"<p>last line</p>", wikiModel.render("First line:\n"+
				" pre text 1\n"+
				" pre text 2\n"+
				"last line"));
	}
//	public void testPre1() {
//		assertEquals("", wikiModel.render("<pre>\n<nowiki>\n" + 
//				"The nowiki tag ignores [[Wiki]] ''markup''.\n" + 
//				"It reformats text by removing\n" + 
//				"newlines    and multiple spaces.\n" + 
//				"It still interprets special\n" + 
//				"characters: & \n" + 
//				"</nowiki>\n</pre>"));
//	}
	
	public void testPre10() {
		assertEquals(
				"\n" + 
				"<p>Aufzählungstypen dienen zur automatischen Nummerierung der in der Aufzählung enthaltenen Elemente. Die Syntax für die Definition von Aufzählungstypen verwendet das Schlüsselwort <tt>enum</tt> (Kurzform für Enumeration).</p>\n" + 
				"<p>Beim in C# verwendeten Aufzählungstyp kann ein zugrundeliegender Datentyp für die Nummerierung der Elemente angegeben werden kann. Per Voreinstellung wird der Datentyp <tt>int</tt> verwendet. </p> \n" + 
				"<pre>\n public enum Woche : int\n" + 
				" {\n" + 
				"   Montag = 1,\n" + 
				"   Dienstag,\n" + 
				"   Mittwoch,\n" + 
				"   Donnerstag,\n" + 
				"   Freitag,\n" + 
				"   Samstag,\n" + 
				"   Sonntag\n" + 
				" }\n" + 
				"\n</pre>",
				wikiModel
						.render("Aufzählungstypen dienen zur automatischen Nummerierung der in der Aufzählung enthaltenen Elemente. Die Syntax für die Definition von Aufzählungstypen verwendet das Schlüsselwort <tt>enum</tt> (Kurzform für Enumeration).\n"
								+ "\n"
								+ "Beim in C# verwendeten Aufzählungstyp kann ein zugrundeliegender Datentyp für die Nummerierung der Elemente angegeben werden kann. Per Voreinstellung wird der Datentyp <tt>int</tt> verwendet. \n"
								+ " \n"
								+ "  public enum Woche : int\n"
								+ "  {\n"
								+ "    Montag = 1,\n"
								+ "    Dienstag,\n"
								+ "    Mittwoch,\n"
								+ "    Donnerstag,\n" + "    Freitag,\n" + "    Samstag,\n" + "    Sonntag\n" + "  }\n" + ""));
	}

}