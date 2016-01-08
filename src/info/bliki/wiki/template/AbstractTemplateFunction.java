package info.bliki.wiki.template;

import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

/**
 * An abstract template parser function.
 *
 */
public abstract class AbstractTemplateFunction implements ITemplateFunction {

    @Override
    public String getFunctionDoc() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String parseFunction(List<String> parts, IWikiModel model, char[] src, int beginIndex, int endIndex,
            boolean isSubst) throws IOException;

    /**
     * Parse the given content string with the template parser.
     *
     * @param content
     *          the raw content string
     * @param model
     *          the wiki model
     * @return
     */
    public static String parse(String content, IWikiModel model) {
        if (content == null || content.length() == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(content.length());
        try {
            TemplateParser.parse(content, model, buf, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }

    /**
     * Parse the given content string with the template parser and
     * <code>trim()</code> the resulting string.
     *
     * @param content
     *          the raw content string
     * @param model
     *          the wiki model
     * @return
     */
    public static String parseTrim(String content, IWikiModel model) {
        return parse(content, model).trim();
    }

}
