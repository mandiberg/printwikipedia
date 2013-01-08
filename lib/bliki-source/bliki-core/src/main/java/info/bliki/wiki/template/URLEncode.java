package info.bliki.wiki.template;

import info.bliki.api.Connector;
import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{urlencode: ... }}</code> syntax
 * 
 */
public class URLEncode extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new URLEncode();

	public URLEncode() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String result = parse(list.get(0), model);
			return URLEncoder.encode(result, Connector.UTF8_CHARSET);
		}
		return null;
	}
}
