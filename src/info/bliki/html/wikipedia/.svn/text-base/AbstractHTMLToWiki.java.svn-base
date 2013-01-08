package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.EndTagToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Base class for all HTML to wiki tet converters.
 * 
 * @see info.bliki.html.wikipedia.ToWikipedia
 * @see info.bliki.html.googlecode.ToGoogleCode
 */
public class AbstractHTMLToWiki {
	final Map<String, HTMLTag> fHashMap;

	final boolean fNoDiv;

	final boolean fNoFont;

	public AbstractHTMLToWiki(Map<String, HTMLTag> map, boolean noDiv, boolean noFont) {
		super();
		fHashMap = map;
		fNoDiv = noDiv;
		fNoFont = noFont;
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
							resultBuffer.append("<br>");
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

	public void nodeToWiki(BaseToken node, StringBuilder wikiText) {
		if (node instanceof ContentToken) {
			ContentToken contentToken = (ContentToken) node;
			String content = contentToken.getContent();
			// content = content.replaceAll("&nbsp;", " ");
			content = StringUtils.replace(content, "&nbsp;", " ");
			wikiText.append(content);
		} else if (node instanceof TagNode) {
			TagNode tagNode = (TagNode) node;

			String name = tagNode.getName();
			HTMLTag tag = (HTMLTag) fHashMap.get(name);

			if (tag != null) {
				boolean showWithoutTag = false;
				if (fNoDiv && name.equals("div")) {
					showWithoutTag = true;
				}
				if (fNoFont && name.equals("font")) {
					showWithoutTag = true;
				}
				tag.content(this, tagNode, wikiText, showWithoutTag);
			} else {
				List children = tagNode.getChildren();
				if (children.size() != 0) {
					nodesToText(children, wikiText);
				}
			}
		}
	}

	protected void nodesToPlainText(List nodes, StringBuilder resultBuffer) {
		if (nodes != null && !nodes.isEmpty()) {
			Iterator childrenIt = nodes.iterator();
			while (childrenIt.hasNext()) {
				Object item = childrenIt.next();
				if (item != null) {
					if (item instanceof List) {
						nodesToPlainText((List) item, resultBuffer);
					} else if (item instanceof EndTagToken) {
						EndTagToken node = (EndTagToken) item;
						if (node.getName().equals("br")) {
							resultBuffer.append(" ");
						} else if (node.getName().equals("hr")) {
							resultBuffer.append(" ");
						}
					} else if (item instanceof BaseToken) {
						nodesToPlainText((BaseToken) item, resultBuffer);
					}
				}
			}
		}
	}

	public void nodesToPlainText(BaseToken node, StringBuilder plainText) {
		if (node instanceof ContentToken) {
			ContentToken contentToken = (ContentToken) node;
			String content = contentToken.getContent();
			content = content.replaceAll("&nbsp;", " ");
			plainText.append(content);
		} else if (node instanceof TagNode) {
			TagNode tagNode = (TagNode) node;
			List children = tagNode.getChildren();
			if (children.size() != 0) {
				nodesToPlainText(children, plainText);
			}
		}
	}

}