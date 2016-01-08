package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{plural: ... }}</code> <i>lower
 * case</i> syntax. See <a
 * href="http://en.wikipedia.org/wiki/Help:Variable#Formatting">Wikipedia -
 * Help:Variable#Formatting</a>
 *
 */
public class Plural extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Plural();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String arg0 = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            int n = 0;
            try {
                n = Integer.parseInt(arg0, 10);
            } catch (NumberFormatException nfe) {
                // fall through?
            }
            if (list.size() > 1) {
                if (n <= 1) {
                    return isSubst ? list.get(1) : parseTrim(list.get(1), model);
                } else {
                    if (list.size() > 2) {
                        return isSubst ? list.get(2) : parseTrim(list.get(2), model);
                    }
                }
            }
            return "";
        }
        return null;
    }
}
