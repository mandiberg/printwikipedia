package info.bliki.wiki.template;

import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.expr.eval.DoubleEvaluator;

import java.io.IOException;
import java.util.List;

/**
 * A template parser function for <code>{{ #expr: ... }}</code> syntax.
 *
 * See <a href="https://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a> See: <a href="http://svn.wikimedia.org/viewvc/mediawiki/trunk/extensions/ParserFunctions/Expr.php?view=markup"
 * >Expr.php in MediaWiki SVN</a> or<a
 * href="http://en.wikipedia.org/wiki/Help:Calculation">Wikipedia -
 * Help:Calculation</a>.
 */
public class Expr extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Expr();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws IOException {
        if (list.size() > 0) {
            String expression = parseTrim(list.get(0), model);
            if (expression.length() == 0) {
                return null;
            }
            if (!isSubst) {
                StringBuilder conditionBuffer = new StringBuilder(expression.length());
                TemplateParser.parse(expression, model, conditionBuffer, false);
                if (conditionBuffer.length() == 0) {
                    return null;
                }
                expression = conditionBuffer.toString();
            }

            try {
                DoubleEvaluator engine = new DoubleEvaluator();
                double d = engine.evaluate(expression);
                return getWikiNumberFormat(d,model);
            } catch (Exception e) {
                if (Configuration.DEBUG) {
                    System.out.println("#expr error: "+expression);
                }
                if (Configuration.STACKTRACE) {
                    e.printStackTrace();
                }
                return "<div class=\"error\">Expression error: " + e.getMessage() + "</div>";
                // e.printStackTrace();
            }

        }
        return null;
    }

    public static String getWikiNumberFormat(double d,IWikiModel model) {
        double dInt = Math.rint(d);
        // if (dInt == d) {
        if (Math.abs(dInt - d) < DoubleEvaluator.EPSILON) {
            return Long.toString(Math.round(d));
        }
        return Double.toString(d).toUpperCase();
    }

}
