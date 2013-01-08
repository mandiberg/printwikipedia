package info.bliki.wiki.template;

import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{ #ifexist: ... }}</code> syntax. See
 * <a href="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions">
 * Mediwiki's Help:Extension:ParserFunctions</a>
 * 
 */
public class Ifexist extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Ifexist();

	public Ifexist() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 1) {
			String wikiTopicName = parse(list.get(0), model);
			int index = wikiTopicName.indexOf(":");
			String namespace = "";
			String templateName = wikiTopicName;
			if (index != -1) {
				namespace = wikiTopicName.substring(0, index);
				if (!model.isNamespace(namespace)) {
					namespace = "";
				} else {
					templateName = wikiTopicName.substring(index + 1);
				}
			}
			if (model.getRawWikiContent(namespace, templateName, null) != null) {
				return parse(list.get(1), model);
			} else {
				// the requested templateName doesn't exist
				if (list.size() >= 3) {
					return parse(list.get(2), model);
				}
			}
		}
		return null;
	}
}
