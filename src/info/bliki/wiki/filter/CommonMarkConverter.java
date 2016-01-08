package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.WPATag;

import java.io.IOException;
import java.util.regex.Pattern;

public class CommonMarkConverter extends PlainTextConverter {
    private static final Pattern ESCAPE_IN_URI =
            Pattern.compile("(%[a-fA-F0-9]{0,2}|[^:/?#@!$&'()*+,;=a-zA-Z0-9\\-._~])");

    private static final Pattern NESTED_PARENS =
            Pattern.compile("\\([^\\)]*\\(");

    private boolean renderEmphasis;

    public CommonMarkConverter(boolean renderEmphasis) {
        this.renderEmphasis = renderEmphasis;
    }

    @Override public boolean renderLinks() {
        return true;
    }

    public boolean renderEmphasis() {
        return renderEmphasis;
    }

    public void renderLink(WPATag tag, Appendable buf, IWikiModel wikiModel) throws IOException {
        StringBuilder linkBuffer = new StringBuilder();
        tag.renderPlainText(new PlainTextConverter(), linkBuffer, wikiModel);

        String linkTitle = linkBuffer.toString();
        if (linkTitle.contains("#")) {
            linkTitle = linkTitle.substring(0, linkTitle.indexOf("#"));
        }

        buf.append("[").append(commonMarkEscapeLinkText(linkTitle)).append("]");
        buf.append("(").append(commonMarkEscapeLink(getWikiLink(tag, linkTitle))).append(")");
    }

    private String commonMarkEscapeLink(String link) {
        if (needsEscaping(link)) {
            return "<"+link+">";
        } else {
            return link;
        }
    }

    // in CommonMark the link text usually does not need to be quoted, since
    // predence rules are sane. an exception are unbalanced nested brackets,
    // just escape them wholesale
    private String commonMarkEscapeLinkText(String linkText) {
        return linkText.replace("[", "\\[")
                       .replace("]", "\\]");
    }

    protected String getWikiLink(WPATag tag, String linkContent) {
       String link = tag.getLink();
        if (link == null) {
            return null;
        } else {
            if (link.trim().isEmpty()) {
                link = linkContent;
            }

            if (tag.getAnchor() != null) {
                return link + "#" + tag.getAnchor();
            } else {
                return link;
            }
        }
    }

    private boolean needsEscaping(String string) {
        return ESCAPE_IN_URI.matcher(string).find() || NESTED_PARENS.matcher(string).find();
    }
}
