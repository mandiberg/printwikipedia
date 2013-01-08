package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class HTTPUrlFilterTest extends FilterTestSupport {
	public HTTPUrlFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(HTTPUrlFilterTest.class);
	}

	public void testUrlHTTP() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"externallink\" href=\"http://www.plog4u.de\" rel=\"nofollow\" title=\"http://www.plog4u.de\">http://www.plog4u.de</a></p>",
				wikiModel.render("see http://www.plog4u.de"));
	}

	public void testUrlFTP() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"externallink\" href=\"ftp://www.plog4u.de\" rel=\"nofollow\" title=\"ftp://www.plog4u.de\">ftp://www.plog4u.de</a></p>",
				wikiModel.render("see ftp://www.plog4u.de"));
	}

	public void testUrl2() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"externallink\" href=\"http://www.plog4u.de/index.php&#38;test_me\" rel=\"nofollow\" title=\"http://www.plog4u.de/index.php&#38;test_me\">http://www.plog4u.de/index.php&#38;test_me</a></p>",
				wikiModel.render("see http://www.plog4u.de/index.php&test_me"));
	}

	public void testUrl3() {
		assertEquals(
				"\n"
						+ "<ol>\n"
						+ "<li>Bare URL: <a class=\"externallink\" href=\"http://www.nupedia.com/\" rel=\"nofollow\" title=\"http://www.nupedia.com/\">http://www.nupedia.com/</a></li></ol>",
				wikiModel.render("# Bare URL: http://www.nupedia.com/"));
	}

	public void testUrl4() {
		assertEquals(
				"\n"
						+ "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"externallink\" href=\"ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\" title=\"ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13.&#160;Juli 2005</p>",
				wikiModel
						.render("Bericht über die Weltkulturerbe-Bewerbung von [ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005"));
	}

	public void testUrl5() {
		assertEquals(
				"\n"
						+ "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"externallink\" href=\"http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\" title=\"http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13.&#160;Juli 2005</p>",
				wikiModel
						.render("Bericht über die Weltkulturerbe-Bewerbung von [http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005"));
	}

	public void testImageUrl() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"externallink\" href=\"http://www.plog4u.de/image.gif\" rel=\"nofollow\" title=\"http://www.plog4u.de/image.gif\">http://www.plog4u.de/image.gif</a></p>",
				wikiModel.render("see http://www.plog4u.de/image.gif"));
	}

	public void testISBN() {
		assertEquals(
				"\n"
						+ "<p>Note that the numbers are not automatically made clickable until they are in this format:</p>\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\">ISBN 1413304540</a></dd></dl>\n"
						+ "\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 978-1413304541</a> <b><i>or</i></b> <a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 9781413304541</a> (without the dash)</dd></dl>",
				wikiModel.render("Note that the numbers are not automatically made clickable until they are in this format:\n" + "\n"
						+ ":ISBN 1413304540\n" + "\n" + ":ISBN 978-1413304541 \'\'\'\'\'or\'\'\'\'\' ISBN 9781413304541 (without the dash)"));
	}

	public void testMailto() {
		assertEquals(
				"\n"
						+ "<p>Linking to an e-mail address works the same way: \n"
						+ "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">mailto:someone@domain.com</a> or \n"
						+ "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">someone</a></p>",
				wikiModel.render("Linking to an e-mail address works the same way: \n" + "mailto:someone@domain.com or \n"
						+ "[mailto:someone@domain.com someone]"));
	}

	public void testWrongMailto() {
		assertEquals("\n" + "<p>Linking to an e-mail address works the same way: \n" + "mailto:some one@domain.com or \n"
				+ "[mailto:some one@domain.com someone]</p>", wikiModel.render("Linking to an e-mail address works the same way: \n"
				+ "mailto:some one@domain.com or \n" + "[mailto:some one@domain.com someone]"));
	}

	public void testUrlWithSpan() {
		assertEquals(
				"\n"
						+ "<p><a class=\"externallink\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&#38;action=edit\" rel=\"nofollow\" title=\"http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&#38;action=edit\"><span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span></a></p>",
				wikiModel
						.render("[http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&action=edit <span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span>]"));
	}

	public void testUrlTEL01() {
		assertEquals("\n"
				+ "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">tel:+0815-4711</a></p>", wikiModel
				.render("call tel:+0815-4711"));
	}

	public void testUrlTEL02() {
		assertEquals("\n"
				+ "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">a phone number</a></p>", wikiModel
				.render("call [tel:+0815-4711 a phone number]"));
	}
}