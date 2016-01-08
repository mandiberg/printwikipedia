package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{uc: ... }}</code> <i>upper case</i>
 * syntax. See <a
 * href="http://en.wikipedia.org/wiki/Help:Variable#Formatting">Wikipedia -
 * Help:Variable#Formatting</a>
 *
 */
public class UC extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new UC();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String result = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            return result.toUpperCase();
        }
        return null;
    }
}
