package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;

import java.io.IOException;
import java.util.List;


/**
 * Interface for converters which renders the internal node representation into
 * a string representation
 *
 */
public interface ITextConverter {
    /**
     * Convert the list of TagTokens into a given HTML string buffer
     *
     * @param nodes
     *          list of TagToken
     * @param resultBuffer
     *          the rendered HTML string
     * @param model
     *          the current wiki model
     */
    void nodesToText(List<?> nodes, Appendable resultBuffer, IWikiModel model) throws IOException;

    /**
     * Convert the imageTagNode into a given HTML string buffer
     *
     * @param imageTagNode
     *          the tag which carries the imageFormat wiki object as an object
     *          attribute
     * @param imageFormat
     * @param resultBuffer
     *          the rendered HTML string
     * @param model
     *          the current wiki model
     */
    void imageNodeToText(TagNode imageTagNode, ImageFormat imageFormat, Appendable resultBuffer, IWikiModel model) throws IOException;

    /**
     * @return If this method returns false, then the &lt;a&gt; tag should only render the
     * title of the link and not a link to another HTML document, but only the
     * link text.
     */
    boolean renderLinks();
}
