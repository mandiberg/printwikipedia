package info.bliki.wiki.template;

import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;

import java.util.List;

/**
 * A template parser function for <code>{{ #ifexist: ... }}</code> syntax. See
 * <a href="https://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a>
 *
 */
public class Ifexist extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Ifexist();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 1) {
            String wikiTopicName = isSubst ? list.get(0) : parseTrim(list.get(0), model);

            final INamespace namespace = model.getNamespace();
            // note: appended "#section" does not count for the check whether a page
            // exists or not!
            // -> strip off
            ParsedPageName parsedPagename = ParsedPageName.parsePageName(model, wikiTopicName, namespace.getMain(), false, true);
            String rawWikiContent = null;
            // if parsing failed, e.g. double "::" at the page titles beginning, this
            // is the same as if the page does not exist.
            if (parsedPagename.valid) {
                try {
                    rawWikiContent = model.getRawWikiContent(parsedPagename, null);
                } catch (WikiModelContentException e) {
                    // the requested templateName doesn't exist
                }
            }

            if (rawWikiContent != null) {
                return isSubst ? list.get(1) : parseTrim(list.get(1), model);
            } else {
                // the requested templateName doesn't exist
                if (list.size() >= 3) {
                    return isSubst ? list.get(2) : parseTrim(list.get(2), model);
                }
            }
        }
        return null;
    }
}
