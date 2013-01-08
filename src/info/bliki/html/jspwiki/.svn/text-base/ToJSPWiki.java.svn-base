package info.bliki.html.jspwiki;

import info.bliki.html.googlecode.ListGCTag;
import info.bliki.html.googlecode.TableGCTag;
import info.bliki.html.wikipedia.ATag;
import info.bliki.html.wikipedia.AbstractHTMLToWiki;
import info.bliki.html.wikipedia.HTMLTag;
import info.bliki.html.wikipedia.IHTMLToWiki;
import info.bliki.html.wikipedia.NoOutputTag;
import info.bliki.html.wikipedia.OpenCloseTag;
import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.EndTagToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Convert HTML text to Google Code wiki syntax
 * 
 */
public class ToJSPWiki extends AbstractHTMLToWiki implements IHTMLToWiki {
	static private final Map<String, HTMLTag> TAG_MAP = new HashMap<String, HTMLTag>();
	static {
		TAG_MAP.put("a", new ATag("[", "]"));
		TAG_MAP.put("b", new OpenCloseTag("__", "__"));
		TAG_MAP.put("strong", new OpenCloseTag("__", "__"));
		TAG_MAP.put("i", new OpenCloseTag("''", "''"));
		TAG_MAP.put("em", new OpenCloseTag("''", "''"));
		TAG_MAP.put("table", new TableGCTag());
		// TAG_MAP.put("caption", new CaptionTag());
		TAG_MAP.put("tr", new TrJSPWikiTag());
		TAG_MAP.put("td", new TdJSPWikiTag());
		TAG_MAP.put("th", new ThJSPWikiTag());
		// TAG_MAP.put("img", new ImgTag());
		TAG_MAP.put("p", new OpenCloseTag("\n", "\n\n"));
		// TAG_MAP.put("code", new OpenCloseTag("{{{", "}}}"));
		// TAG_MAP.put("blockquote", new OpenCloseTag("<blockquote>",
		// "</blockquote>"));
		// TAG_MAP.put("u", new OpenCloseTag("<u>", "</u>"));
		// TAG_MAP.put("del", new OpenCloseTag("~~", "~~"));
		// TAG_MAP.put("s", new OpenCloseTag("~~", "~~"));
		// TAG_MAP.put("sub", new OpenCloseTag(",,", ",,"));
		// TAG_MAP.put("sup", new OpenCloseTag("^", "^"));
		// TAG_MAP.put("div", new OpenCloseHTMLTag("\n<div", "\n</div>"));
		// TAG_MAP.put("font", new OpenCloseHTMLTag("<font", "</font>"));
		TAG_MAP.put("pre", new OpenCloseTag("\n{{\n", "\n}}\n"));
		TAG_MAP.put("h1", new OpenCloseTag("\n!!!", "\n", true));
		TAG_MAP.put("h2", new OpenCloseTag("\n!!!", "\n", true));
		TAG_MAP.put("h3", new OpenCloseTag("\n!!", "\n", true));
		TAG_MAP.put("h4", new OpenCloseTag("\n!", "\n", true));
		TAG_MAP.put("h5", new OpenCloseTag("\n!", "\n", true));
		TAG_MAP.put("h6", new OpenCloseTag("\n!", "\n", true));
		TAG_MAP.put("ul", new ListGCTag("*", "*", "#"));
		TAG_MAP.put("ol", new ListGCTag("#", "*", "#"));
		TAG_MAP.put("script", new NoOutputTag());
	}

	public ToJSPWiki(boolean noDiv, boolean noFont) {
		super(TAG_MAP, noDiv, noFont);
	}

	public ToJSPWiki() {
		this(false, false);
	}

	public void nodesToText(List nodes, StringBuilder resultBuffer) {
		if (nodes != null && !nodes.isEmpty()) {
			Iterator childrenIt = nodes.iterator();
			while (childrenIt.hasNext()) {
				Object item = childrenIt.next();
				if (item != null) {
					if (item instanceof List) {
						nodesToText((List) item, resultBuffer);
					} else if (item instanceof EndTagToken) {
						EndTagToken node = (EndTagToken) item;
						if (node.getName().equals("br")) {
							resultBuffer.append("\\\\\n");
						} else if (node.getName().equals("hr")) {
							resultBuffer.append("\n----\n");
						}
					} else if (item instanceof BaseToken) {
						nodeToWiki((BaseToken) item, resultBuffer);
					}
				}
			}
		}
	}
}
