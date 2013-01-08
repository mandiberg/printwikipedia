package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #ifeq: ... }}</code> syntax. See <a
 * href
 * ="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">Mediwiki's
 * Help:Extension:ParserFunctions</a>
 * 
 */
public class Ifeq extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Ifeq();

	public Ifeq() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 2) {
			String first = parse(list.get(0), model);
			String second = parse(list.get(1), model);
			if (equalsTypes(first, second)) {
				return parse(list.get(2), model);
			} else if (list.size() >= 4) {
				return parse(list.get(3), model);
			}
		}
		return null;
	}

	private boolean equalsTypes(String first, String second) {

		boolean result = false;
		if (first.length() == 0) {
			return second.length() == 0;
		}
		if (second.length() == 0) {
			return first.length() == 0;
		}
		if (first.charAt(0) == '+') {
			first = first.substring(1);
		}
		if (second.charAt(0) == '+') {
			second = second.substring(1);
		}

		try {
			double d1 = Double.parseDouble(first);
			double d2 = Double.parseDouble(second);
			if (d1 == d2) {
				result = true;
			}
		} catch (NumberFormatException e) {
			result = first.equals(second);
		}

		return result;
	}
}
