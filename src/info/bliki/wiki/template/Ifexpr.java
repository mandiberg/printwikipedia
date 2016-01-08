package info.bliki.wiki.template;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.expr.eval.DoubleEvaluator;

import java.util.List;

/**
 * A template parser function for <code>{{ #ifexpr: ... }}</code> syntax
 *
 * See <a href="https://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a> See: <a href="http://svn.wikimedia.org/viewvc/mediawiki/trunk/extensions/ParserFunctions/Expr.php?view=markup"
 * >Expr.php in MediaWiki SVN</a>
 */
public class Ifexpr extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Ifexpr();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 1) {
            String condition = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            if (condition.length() > 0) {
                try {
                    DoubleEvaluator engine = new DoubleEvaluator();
                    double d = engine.evaluate(condition);
                    // if (d == 0.0) {
                    if (Math.abs(d - 0.0) < DoubleEvaluator.EPSILON) {
                        if (list.size() >= 3) {
                            // &lt;else text&gt;
                            return isSubst ? list.get(2) : parseTrim(list.get(2), model);
                        }
                        return null;
                    }
                    return isSubst ? list.get(1) : parseTrim(list.get(1), model);
                } catch (Exception e) {
                    if (Configuration.DEBUG) {
                        System.out.println("#ifexpr error: "+condition);
                    }
                    if (Configuration.STACKTRACE) {
                        e.printStackTrace();
                    }
                    return "<div class=\"error\">Expression error: " + e.getMessage() + "</div>";
                }
            } else {
                if (list.size() >= 3) {
                    // &lt;else text&gt;
                    return isSubst ? list.get(2) : parseTrim(list.get(2), model);
                }
            }
        }
        return null;
    }
}
