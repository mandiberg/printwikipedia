package info.bliki.wiki.template;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{urlencode: ... }}</code> syntax
 * 
 */
public class Anchorencode extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Anchorencode();

	public Anchorencode() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String result = parse(list.get(0), model);
			return Encoder.encodeDotUrl(result);
		}
		return null;
	}
}
