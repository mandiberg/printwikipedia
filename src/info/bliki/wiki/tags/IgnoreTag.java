package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;

/**
 * Wiki tag which renders no HTML output. This tag is useful for ignoring wiki
 * extension tags, which shouldn't be supported. See <a
 * href="http://code.google.com/p/gwtwiki/issues/detail?id=94">Issue 94</a>
 *
 */
public class IgnoreTag extends HTMLTag implements INoBodyParsingTag {

    public IgnoreTag(String tagName) {
        super(tagName);
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        // doesn't add any output to the buffer
    }

    @Override
    public boolean isAllowedAttribute(String attName) {
        return true;
    }

}
