package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.expr.eval.DoubleEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #ifexpr: ... }}</code> syntax
 * 
 * See <a href="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a> See: <a href="http://svn.wikimedia.org/viewvc/mediawiki/trunk/extensions/ParserFunctions/Expr.php?view=markup"
 * >Expr.php in MediaWiki SVN</a>
 */
public class Ifexpr extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Ifexpr();

	public Ifexpr() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 1) {

			String condition = parse(list.get(0), model);
			if (condition.length() > 0) {
				try {
					DoubleEvaluator engine = new DoubleEvaluator();
					double d = engine.evaluate(condition);
					// if (d == 0.0) {
					if (Math.abs(d - 0.0) < DoubleEvaluator.EPSILON) {
						if (list.size() >= 3) {
							// &lt;else text&gt;
							return parse(list.get(2), model);
						}
						return null;
					}
					return parse(list.get(1), model);
				} catch (Exception e) {
					return "<div class=\"error\">Expression error: " + e.getMessage() + "</div>";
				}
			} else {
				if (list.size() >= 3) {
					// &lt;else text&gt;
					return parse(list.get(2), model);
				}
			}
		}
		return null;
	}
}
