package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

import java.util.List;
import java.util.Map;

public class FontTag extends OpenCloseHTMLTag {
    public FontTag(String opener, String closer) {
        super(opener, closer);
    }

    @Override
    public void open(TagNode node, StringBuilder resultBuffer) {
        resultBuffer.append(openStr);

        Map<String, String> tagAttributes = node.getAttributes();
        boolean first = true;
        for (Map.Entry<String, String> currEntry : tagAttributes.entrySet()) {
            if (first) {
                resultBuffer.append(" ");
                first = false;
            }
            String attName = currEntry.getKey();
            if (attName.length() >= 1 && Character.isLetter(attName.charAt(0))) {
                String attValue = currEntry.getValue();

                resultBuffer.append(" " + attName + "=\"" + attValue + "\"");
            }
        }
        resultBuffer.append(">");
    }

    @Override
    public void content(AbstractHTMLToWiki w, TagNode node,
        StringBuilder resultBuffer, boolean showWithoutTag) {
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
            String attValue = null;
            if (!showWithout) {
                open(node, resultBuffer);
            } else {
                Map<String, String> tagAttributes = node.getAttributes();
                attValue = tagAttributes.get("face");
                if (attValue != null && attValue.contains("Courier")) {
                    resultBuffer.append("<tt>");
                } else {
                    attValue = null;
                }
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
            } else {
                if (attValue != null) {
                    resultBuffer.append("</tt>");
                }
            }

        }
    }
}
