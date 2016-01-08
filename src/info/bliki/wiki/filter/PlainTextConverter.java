package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;

import java.io.IOException;
import java.util.List;

import static info.bliki.wiki.model.Configuration.RENDERER_RECURSION_LIMIT;

/**
 * A converter which renders the internal tree node representation as plain text
 * without HTML tags and images
 */
public class PlainTextConverter implements ITextConverter {
    private boolean renderLinks;

    public PlainTextConverter(boolean renderLinks) {
        this.renderLinks = renderLinks;
    }
    public PlainTextConverter() {
        this(false);
    }

    @Override
    public void nodesToText(List<?> nodes, Appendable resultBuffer, IWikiModel model) throws IOException {
        assert (model != null);
        if (nodes != null && !nodes.isEmpty()) {
            try {
                int level = model.incrementRecursionLevel();

                if (level > RENDERER_RECURSION_LIMIT) {
                    resultBuffer.append("Error - recursion limit exceeded rendering tags in PlainTextConverter#nodesToText().");
                    return;
                }
                for (Object item : nodes) {
                    if (item instanceof List<?>) {
                        nodesToText((List<?>) item, resultBuffer, model);
                    } else if (item instanceof PlainTextConvertable) {
                        ((PlainTextConvertable) item).renderPlainText(this, resultBuffer, model);
                    }
                }
            } finally {
                model.decrementRecursionLevel();
            }
        }
    }

    @Override
    public boolean renderLinks() {
        return renderLinks;
    }

    @Override
    public void imageNodeToText(TagNode imageTagNode, ImageFormat imageFormat, Appendable resultBuffer, IWikiModel model)
            throws IOException {
    }
}
