package info.bliki.wiki.template;

import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.expr.eval.DoubleEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #expr: ... }}</code> syntax.
 * 
 * See <a href="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a> See: <a href="http://svn.wikimedia.org/viewvc/mediawiki/trunk/extensions/ParserFunctions/Expr.php?view=markup"
 * >Expr.php in MediaWiki SVN</a>
 */
public class Expr extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Expr();

	public Expr() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String expression = list.get(0);
			if (expression.length() == 0) {
				return null;
			}
			StringBuilder conditionBuffer = new StringBuilder(expression.length());
			TemplateParser.parse(expression, model, conditionBuffer, false);
			if (conditionBuffer.length() > 0) {
				try {
					DoubleEvaluator engine = new DoubleEvaluator();
					double d = engine.evaluate(conditionBuffer.toString());
					double dInt = Math.rint(d);
					// if (dInt == d) {
					if (Math.abs(dInt - d) < DoubleEvaluator.EPSILON) {
						return Long.toString(Math.round(d));
					}
					String result = Double.toString(d);
					return result.toUpperCase();
				} catch (Exception e) {
					return "<div class=\"error\">Expression error: " + e.getMessage() + "</div>";
					// e.printStackTrace();
				}
			}
		}
		return null;
	}
}
