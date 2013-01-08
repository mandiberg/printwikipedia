package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ucfirst: ... }}</code> <i>first character to upper case</i>
 * syntax
 * 
 */
public class UCFirst extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new UCFirst();

	public UCFirst() {

	}
 
	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String word = parse(list.get(0), model);
			if (word.length() > 0) {
				return Character.toUpperCase(word.charAt(0)) + word.substring(1);
			}
			return "";
		}
		return null;
	}
}
