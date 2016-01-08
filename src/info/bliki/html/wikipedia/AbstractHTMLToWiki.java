package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.EndTagToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.List;
import java.util.Map;

/**
 * Base class for all HTML to wiki text converters.
 *
 * @see info.bliki.html.wikipedia.ToWikipedia
 * @see info.bliki.html.googlecode.ToGoogleCode
 */
public class AbstractHTMLToWiki {
    final Map<String, HTMLTag> fHashMap;

    final boolean fNoDiv;

    final boolean fNoFont;

    final boolean fNoMSWordTags;

    public AbstractHTMLToWiki(Map<String, HTMLTag> map, boolean noDiv, boolean noFont, boolean noMSWordTags) {
        super();
        fHashMap = map;
        fNoDiv = noDiv;
        fNoFont = noFont;
        fNoMSWordTags = noMSWordTags;
    }

    public AbstractHTMLToWiki(Map<String, HTMLTag> map, boolean noDiv, boolean noFont) {
        this(map, noDiv, noFont, false);
    }

    public void nodesToText(List<Object> nodes, StringBuilder resultBuffer) {
        if (nodes != null && !nodes.isEmpty()) {
            for (Object item : nodes) {
                if (item != null) {
                    if (item instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<Object> list = (List<Object>) item;
                        nodesToText(list, resultBuffer);
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
            content = content.replaceAll("&nbsp;", " ");
            // content = StringUtils.replace(content, "&nbsp;", " ");
            wikiText.append(content);
        } else if (node instanceof TagNode) {
            TagNode tagNode = (TagNode) node;

            String name = tagNode.getName();
            HTMLTag tag = fHashMap.get(name);

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
                if (name.equals("br")) {
                    wikiText.append("<br>");
                } else if (name.equals("hr")) {
                    wikiText.append("\n----\n");
                } else {
                    List<Object> children = tagNode.getChildren();
                    if (children.size() != 0) {
                        nodesToText(children, wikiText);
                    }
                }
            }
        }
    }

    protected void nodesToPlainText(List<Object> nodes, StringBuilder resultBuffer) {
        if (nodes != null && !nodes.isEmpty()) {
            for (Object item : nodes) {
                if (item != null) {
                    if (item instanceof List) {
                        @SuppressWarnings("unchecked")
                        final List<Object> list = (List<Object>) item;
                        nodesToPlainText(list, resultBuffer);
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
            // TODO refactor this:
            String content = contentToken.getContent();
            content = content.replaceAll("&nbsp;", " ");
            content = content.replaceAll("&lt;", "<");
            content = content.replaceAll("&gt;", ">");
            content = content.replaceAll("&quot;", "\"");
            content = content.replaceAll("&amp;", "&");
            content = content.replaceAll("&apos;", "'");
            plainText.append(content);
        } else if (node instanceof TagNode) {
            TagNode tagNode = (TagNode) node;
            List<Object> children = tagNode.getChildren();
            if (children.size() != 0) {
                nodesToPlainText(children, plainText);
            }
        }
    }

}
