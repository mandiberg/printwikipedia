package info.bliki.wiki.tags;

import info.bliki.wiki.filter.CommonMarkConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;

/**
 * A special Wikipedia tag (i.e. ==, ===, ''', '', ...)
 *
 */
public class WPTag extends HTMLTag {

    public WPTag(String htmlName) {
        super(htmlName);
    }

    @Override
    public boolean isReduceTokenStack() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WPTag) {
            // distinguish wikipedia tags from other tags
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        if (getChildren().size() != 0) {
            super.renderHTML(converter, buf, model);
        }
    }

    @Override
    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (converter instanceof CommonMarkConverter && ((CommonMarkConverter) converter).renderEmphasis()) {
            switch (name) {
                case "b":
                    buf.append("**");
                    super.renderPlainText(converter, buf, wikiModel);
                    buf.append("**");
                    break;
                case "i":
                    buf.append("*");
                    super.renderPlainText(converter, buf, wikiModel);
                    buf.append("*");
                    break;
                default: super.renderPlainText(converter, buf, wikiModel);
            }
        } else {
            super.renderPlainText(converter, buf, wikiModel);
        }
    }
}
