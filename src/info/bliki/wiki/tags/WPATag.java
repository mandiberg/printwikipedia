package info.bliki.wiki.tags;

import info.bliki.wiki.filter.CommonMarkConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


public class WPATag extends HTMLTag {
    public static final String HREF = "href";
    public static final String ANCHOR = "anchor";
    public static final String WIKILINK = "wikilink";
    public static final String CLASS = "class";
    public static final String TITLE = "title";

    public WPATag() {
        super("a");
    }

    @Override
    public boolean isReduceTokenStack() {
        return false;
    }


    @Override
    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (getObjectAttributes().containsKey(WIKILINK) &&
            converter instanceof CommonMarkConverter) {
            ((CommonMarkConverter)converter).renderLink(this, buf, wikiModel);
        } else {
            super.renderPlainText(converter, buf, wikiModel);
        }
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        if (converter.renderLinks()) {
            super.renderHTML(converter, buf, model);
        } else {
            List<Object> children = getChildren();
            if (children.size() != 0) {
                converter.nodesToText(children, buf, model);
            }
        }
    }

    public String getLink() {
        Object link = getObjectAttributes().get(WIKILINK);
        return link == null ? null : link.toString();
    }

    public String getTitle() {
        return getAttributes().get(TITLE);
    }

    public String getAnchor() {
        Object anchor = getObjectAttributes().get(ANCHOR);
        return anchor == null ? null : anchor.toString();
    }
}
