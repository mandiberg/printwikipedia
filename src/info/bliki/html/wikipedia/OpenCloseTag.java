package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

import java.util.List;

public class OpenCloseTag extends AbstractHTMLTag {
    protected String openStr;

    protected String closeStr;

    protected final boolean formatContent;

    /**
     *
     * @param opener
     *          opening string for this tag
     * @param closer
     *          closing string for this tag
     * @param convertPlainText
     *          create plain text output without wiki tags
     * @param formatContent
     *          format the intermediate resulting wiki content by reducing
     *          multiple spaces to only one space ' ' character
     */
    public OpenCloseTag(String opener, String closer, boolean convertPlainText, boolean formatContent) {
        super(convertPlainText);
        this.openStr = opener;
        this.closeStr = closer;
        this.formatContent = formatContent;
    }

    public OpenCloseTag(String opener, String closer, boolean convertPlainText) {
        this(opener, closer, convertPlainText, false);
    }

    public OpenCloseTag(String opener, String closer) {
        this(opener, closer, false, false);
    }

    @Override
    public void open(TagNode node, StringBuilder resultBuffer) {
        resultBuffer.append(openStr);
    }

    @Override
    public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
        List<Object> children = node.getChildren();
        if (children.size() != 0) {

            StringBuilder buf = new StringBuilder();
            if (fconvertPlainText) {
                w.nodesToPlainText(children, buf);
                char ch;
                for (int i = 0; i < buf.length(); i++) {
                    ch = buf.charAt(i);
                    if (ch == '\n' || ch == '\r' || ch == '\t') {
                        buf.setCharAt(i, ' ');
                    }
                }
            } else {
                w.nodesToText(children, buf);
            }
            String str = buf.toString();
            String trimmedStr = str.trim();
            boolean showWithout = showWithoutTag;
            if (trimmedStr.length() == 0) {
                showWithout = true;
            }
            if (!showWithout) {
                open(node, resultBuffer);
            }
            if (fconvertPlainText) {
                resultBuffer.append(trimmedStr);
            } else {
                if (formatContent) {
                    formatContent(trimmedStr, resultBuffer);
                } else {
                    resultBuffer.append(str);
                }
            }
            if (!showWithout) {
                close(node, resultBuffer);
            }

        }
    }

    public void formatContent(String str, StringBuilder resultBuffer) {
        char lastCh = 'X';
        char currentCh = 'X';
        boolean appendCh = true;
        for (int i = 0; i < str.length(); i++) {
            currentCh = str.charAt(i);
            if (currentCh != ' ') {
                if (lastCh == ' ' && appendCh) {
                    resultBuffer.append(' ');
                }
                resultBuffer.append(currentCh);
                if (currentCh == '\n') {
                    appendCh = false;
                } else {
                    appendCh = true;
                }
            }
            lastCh = currentCh;
        }
    }

    @Override
    public void close(TagNode node, StringBuilder resultBuffer) {
        resultBuffer.append(closeStr);
    }

    public String getOpenStr() {
        return openStr;
    }

    public void setOpenStr(String openStr) {
        this.openStr = openStr;
    }

    public String getCloseStr() {
        return closeStr;
    }

    public void setCloseStr(String closeStr) {
        this.closeStr = closeStr;
    }
}
