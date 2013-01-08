package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #tag: ... }}</code> syntax.
 * 
 * See <a
 * href="http://www.mediawiki.org/wiki/Help:Magic_words#Miscellaneous">Magic
 * words-Miscellaneous</a>
 */
public class Tag extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Tag();

	public Tag() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String tagName = parse(list.get(0), model).trim();
			if (list.size() <= 1) {
				return "<" + tagName + "/>";
			} else {
				StringBuilder builder = new StringBuilder(src.length + tagName.length() + 10);
				builder.append("<");
				builder.append(tagName);
				String second = parse(list.get(1), model).trim();
				String attrName;
				String attrValue;
				String content = null;

				int index = second.indexOf('=');
				if (index >= 0) {
					attrName = second.substring(index + 1).trim();
					attrValue = second.substring(index + 1).trim();
					builder.append(" ");
					builder.append(attrName);
					builder.append("=\"");
					builder.append(attrValue);
					builder.append("\"");
				} else {
					content = second;
				}

				for (int i = 2; i < list.size(); i++) {
					String temp = parse(list.get(i), model);
					index = temp.indexOf('=');
					if (index >= 0) {
						attrName = temp.substring(index + 1).trim();
						attrValue = temp.substring(index + 1).trim();
						builder.append(" ");
						builder.append(attrName);
						builder.append("=\"");
						builder.append(attrValue);
						builder.append("\"");
					}
				}
				if (content == null) {
					builder.append("/>");
				} else {
					builder.append(">");
					builder.append(content);
					builder.append("</");
					builder.append(tagName);
					builder.append(">");
				}
				return builder.toString();
			}
		}
		return null;
	}

}
