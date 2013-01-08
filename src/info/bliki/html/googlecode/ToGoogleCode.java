package info.bliki.html.googlecode;

import info.bliki.html.wikipedia.ATag;
import info.bliki.html.wikipedia.AbstractHTMLToWiki;
import info.bliki.html.wikipedia.HTMLTag;
import info.bliki.html.wikipedia.IHTMLToWiki;
import info.bliki.html.wikipedia.NoOutputTag;
import info.bliki.html.wikipedia.OpenCloseTag;

import java.util.HashMap;
import java.util.Map;

/**
 * Convert HTML text to Google Code wiki syntax.
 * 
 * See: <a href="http://code.google.com/p/support/wiki/WikiSyntax">Google code
 * WikiSyntax</a>
 * 
 */
public class ToGoogleCode extends AbstractHTMLToWiki implements IHTMLToWiki {
	static private final Map<String, HTMLTag> TAG_MAP = new HashMap<String, HTMLTag>();
	static {
		TAG_MAP.put("a", new ATag("[", "]"));
		TAG_MAP.put("b", new OpenCloseTag("*", "*"));
		TAG_MAP.put("strong", new OpenCloseTag("*", "*"));
		TAG_MAP.put("i", new OpenCloseTag("_", "_"));
		TAG_MAP.put("em", new OpenCloseTag("_", "_"));
		TAG_MAP.put("table", new TableGCTag());
		// TAG_MAP.put("caption", new CaptionTag());
		TAG_MAP.put("tr", new TrGCTag());
		TAG_MAP.put("td", new TdGCTag());
		TAG_MAP.put("th", new ThGCTag());
		// TAG_MAP.put("img", new ImgTag());
		TAG_MAP.put("p", new OpenCloseTag("\n", "\n\n"));
		TAG_MAP.put("code", new OpenCloseTag("{{{", "}}}"));
		// TAG_MAP.put("blockquote", new OpenCloseTag("<blockquote>",
		// "</blockquote>"));
		// TAG_MAP.put("u", new OpenCloseTag("<u>", "</u>"));
		TAG_MAP.put("del", new OpenCloseTag("~~", "~~"));
		TAG_MAP.put("s", new OpenCloseTag("~~", "~~"));
		TAG_MAP.put("sub", new OpenCloseTag(",,", ",,"));
		TAG_MAP.put("sup", new OpenCloseTag("^", "^"));
		// TAG_MAP.put("div", new OpenCloseHTMLTag("\n<div", "\n</div>"));
		// TAG_MAP.put("font", new OpenCloseHTMLTag("<font", "</font>"));
		TAG_MAP.put("pre", new OpenCloseTag("\n{{{\n", "\n}}}\n"));
		TAG_MAP.put("h1", new OpenCloseTag("\n= ", " =\n", true));
		TAG_MAP.put("h2", new OpenCloseTag("\n== ", " ==\n", true));
		TAG_MAP.put("h3", new OpenCloseTag("\n=== ", " ===\n", true));
		TAG_MAP.put("h4", new OpenCloseTag("\n==== ", " ====\n", true));
		TAG_MAP.put("h5", new OpenCloseTag("\n===== ", " =====\n", true));
		TAG_MAP.put("h6", new OpenCloseTag("\n====== ", " ======\n", true));
		TAG_MAP.put("ul", new ListGCTag("*", "*", "#"));
		TAG_MAP.put("ol", new ListGCTag("#", "*", "#"));
		TAG_MAP.put("script", new NoOutputTag());
	}

	public ToGoogleCode(boolean noDiv, boolean noFont) {
		super(TAG_MAP, noDiv, noFont);
	}

	public ToGoogleCode() {
		this(false, false);
	}

}
