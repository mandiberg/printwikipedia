package info.bliki.wiki.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HTMLTag extends TagNode {
    public final static boolean NEW_LINES = true;

    // compile the regex, for better performance
    protected final static Pattern XML_QNAME = Pattern.compile("^[_A-Za-z][\\w\\.\\-]*(:[_A-Za-z][\\w\\.\\-]*)?$");

    public static void appendEscapedAttributes(Appendable buf, Map<String, String> tagAtttributes) throws IOException {
        if (tagAtttributes != null) {
            for (Map.Entry<String, String> currEntry : tagAtttributes.entrySet()) {
                String attName = currEntry.getKey();
                if (attName.length() >= 1 && Character.isLetter(attName.charAt(0))) {
                    // skip any invalid element names
                    // these usually come from bad markup input
                    // http://www.w3.org/TR/REC-xml-names/#NT-PrefixedName
                    if (!XML_QNAME.matcher(attName).matches()) {
                        continue;
                    }
                    String attValue = currEntry.getValue();
                    buf.append(" ");
                    buf.append(attName);
                    buf.append("=\"");
                    Utils.escapeXmlToBuffer(attValue, buf, false, false, false);
                    buf.append("\"");
                }
            }
        }
    }

    public HTMLTag(String name) {
        super(name);
    }

    public void appendAttributes(Appendable buf, Map<String, String> tagAtttributes) throws IOException {
        appendEscapedAttributes(buf, tagAtttributes);
    }

    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        boolean newLinesAfterTag = false;
        boolean newLinesAfterChildren = false;
        TagNode node = this;
        String name = node.getName();
        List<Object> children = node.getChildren();
        if (children.size() == 0) {
            // don't render empty tags (see Issue98)
            if (!name.equals("a")) {
                // because of section tags allow <a href=\"#Section..." />
                return;
            }
        }

        if (NEW_LINES) {
            if (name.equals("div") || name.equals("p") || name.equals("li") || name.equals("td")) {
                buf.append('\n');
            } else if (name.equals("table") || name.equals("ul") || name.equals("ol") || name.equals("th") || name.equals("tr")) {
                buf.append('\n');
                newLinesAfterTag = true;
                newLinesAfterChildren = true;
            } else if (name.equals("pre")) {
                buf.append('\n');
                newLinesAfterTag = false;
                newLinesAfterChildren = true;
            } else if (name.equals("blockquote")) {
                newLinesAfterChildren = true;
            }
        }
        buf.append('<');
        buf.append(name);

        Map<String, String> tagAtttributes = node.getAttributes();

        appendAttributes(buf, tagAtttributes);

        if (children.size() == 0) {
            buf.append(" />");
        } else {
            buf.append('>');
            if (newLinesAfterTag) {
                buf.append('\n');
            }
            converter.nodesToText(children, buf, model);
            if (newLinesAfterChildren) {
                buf.append('\n');
            }
            buf.append("</");
            buf.append(node.getName());
            buf.append('>');
        }
    }

    public void renderHTMLWithoutTag(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        TagNode node = this;
        List<Object> children = node.getChildren();
        if (children.size() != 0) {
            converter.nodesToText(children, buf, model);
        }
    }

    /**
     * Set if the text is rendered as a template. This method is a placeholder, it
     * can be overridden in subclasses.
     *
     * @param isTemplate
     *          <code>true</code>, if the text is rendered as a template. See
     *          {@link info.bliki.wiki.filter.WikipediaParser#isTemplate()}
     */
    public void setTemplate(boolean isTemplate) {
        // empty function
    }

    /**
     * Use this tag if no parent tag was found on the wiki model's tag stack.
     *
     * @return the default parent tag or <code>null</code>, if there's no default
     *         parent tag
     */
    public HTMLTag getDefaultParentTag() {
        return null;
    }
}
