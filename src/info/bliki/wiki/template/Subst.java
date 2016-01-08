package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{subst: ... }}</code>. See <a
 * href="http://en.wikipedia.org/wiki/Help:Substitution"
 * >Wikipedia-Help:Substitution</a>
 *
 */
public class Subst extends Safesubst {
    public final static ITemplateFunction CONST = new Subst();

    @Override
    public String parseFunction(List<String> parts1, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (!model.isTemplateTopic()) {
            return super.parseFunction(parts1, model,src, beginIndex,endIndex,isSubst);
        }
        return "";
    }
}
