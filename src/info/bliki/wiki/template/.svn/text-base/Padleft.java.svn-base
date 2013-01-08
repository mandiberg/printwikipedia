package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{padleft: ... }}</code>, which
 * <i>pads a string with a character to the specified width</i>. See: <a
 * href="http://meta.wikimedia.org/wiki/Help:Magic_words#Formatting">Magic words -
 * Formatting</a>
 * 
 */
public class Padleft extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Padleft();

	public Padleft() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() == 1) {
			return parse(list.get(0), model);
		}
		if (list.size() > 1) {
			String arg0 = parse(list.get(0), model);
			String arg1 = parse(list.get(1), model);
			String arg2 = "0"; // default value
			if (list.size() > 2) {
				arg2 = parse(list.get(2), model);
			}
			int arg1Int = 0;
			try {
				arg1Int = Integer.parseInt(arg1);
			} catch (NumberFormatException e) {
				return arg0;
			}
			if (arg1Int < 0) {
				return null;
			}
			if (arg0.length() >= arg1Int) {
				return arg0.substring(0, arg1Int);
			}
			if (arg2.length() != 1) {
				return null;
			}
			arg1Int = arg1Int - arg0.length();
			char arg2Char = arg2.charAt(0);
			StringBuilder buffer = new StringBuilder(arg1Int);
			for (int i = 0; i < arg1Int; i++) {
				buffer.append(arg2Char);
			}
			buffer.append(arg0);
			return buffer.toString();
		}
		return null;
	}
}
