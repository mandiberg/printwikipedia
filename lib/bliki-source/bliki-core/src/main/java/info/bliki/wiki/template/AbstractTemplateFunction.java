package info.bliki.wiki.template;

import info.bliki.wiki.filter.TemplateParser;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;

/**
 * A template parser function for <code>{{ #if: ... }}</code> syntax
 * 
 */
public class AbstractTemplateFunction implements ITemplateFunction {
	public final static ITemplateFunction CONST = new AbstractTemplateFunction();

	public AbstractTemplateFunction() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		return null;
	}

	public String getFunctionDoc() {
		return null;
	}

	/**
	 * Parse the given plain content string with the template parser.
	 * 
	 * @param plainContent
	 * @param model
	 * @return
	 */
	public String parse(String plainContent, IWikiModel model) {
		if (plainContent == null || plainContent.length() == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder(plainContent.length() * 2);
		try {
			TemplateParser.parse(plainContent, model, buf, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf.toString().trim();
	}
}
