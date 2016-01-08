package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.List;

public abstract class AbstractHTMLTag implements HTMLTag {
    protected boolean fconvertPlainText;

    public AbstractHTMLTag() {
        this(false);
    }

    public AbstractHTMLTag(boolean noNewLine) {
        this.fconvertPlainText = noNewLine;
    }

    @Override
    public void close(TagNode node, StringBuilder resultBuffer) {
        // do nothing by default
    }

    @Override
    public void open(TagNode node, StringBuilder resultBuffer) {
        // do nothing by default
    }

    @Override
    public void emptyContent(AbstractHTMLToWiki html2WikiConverter, TagNode node,
        StringBuilder resultBuffer, boolean showWithoutTag) {
        // do nothing by default
    }

    @Override
    public void content(AbstractHTMLToWiki w, TagNode node,
        StringBuilder resultBuffer, boolean showWithoutTag) {
        List<Object> children = node.getChildren();
        if (children.size() == 0) {
            emptyContent(w, node, resultBuffer, showWithoutTag);
        } else {
            if (!showWithoutTag) {
                open(node, resultBuffer);
            }
            if (fconvertPlainText) {
                StringBuilder buf = new StringBuilder();
                w.nodesToText(children, buf);
                char ch;
                for (int i = 0; i < buf.length(); i++) {
                    ch = buf.charAt(i);
                    if (ch == '\n' || ch == '\r' || ch == '\t') {
                        buf.setCharAt(i, ' ');
                    }
                }
                String str = buf.toString();
                str = str.trim();
                resultBuffer.append(str);
            } else {
                w.nodesToText(children, resultBuffer);
            }
            if (!showWithoutTag) {
                close(node, resultBuffer);
            }
        }
    }

    /**
     * Get the first ContentToken or TagNode with the given <code>tagName</code>.
     *
     * @param children
     * @param tagName
     * @return
     */
    public BaseToken getFirstContent(List<Object> children, String tagName) {
        if (children.size() != 0) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) != null) {
                    if (children.get(i) instanceof ContentToken
                        && ((ContentToken) children.get(i)).getContent().length() > 0) {
                        return ((ContentToken) children.get(i));
                    } else if (children.get(i) instanceof TagNode
                        && ((TagNode) children.get(i)).getName().equals(tagName)) {
                        return ((TagNode) children.get(i));
                    }
                }
            }
        }
        return null;
    }

}
