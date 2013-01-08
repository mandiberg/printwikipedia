package info.bliki.wiki.template;

import info.bliki.api.Connector;
import info.bliki.wiki.filter.WikipediaScanner;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A template parser function for <code>{{localurl: ... }}</code> syntax
 * 
 */
public class Localurl extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new Localurl();

	public Localurl() {

	}

	public String parseFunction(char[] src, int beginIndex, int endIndex, IWikiModel model) throws IOException {
		List<String> list = new ArrayList<String>();
		WikipediaScanner.splitByPipe(src, beginIndex, endIndex, list);
		if (list.size() > 0) {
			String arg0 = parse(list.get(0), model);
			if (arg0.length() > 0 && list.size() == 1) {
				String result = "/wiki/" + URLEncoder.encode(Character.toUpperCase(arg0.charAt(0)) + "", Connector.UTF8_CHARSET)
						+ URLEncoder.encode(arg0.substring(1), Connector.UTF8_CHARSET);
				return result;
			}
			StringBuilder builder = new StringBuilder(arg0.length() + 32);
			builder.append("/w/index.php?title=");
			builder.append(URLEncoder.encode(Character.toUpperCase(arg0.charAt(0))+"", Connector.UTF8_CHARSET));
			builder.append(URLEncoder.encode(arg0.substring(1), Connector.UTF8_CHARSET));
			for (int i = 1; i < list.size(); i++) {
				builder.append("&");
				builder.append(parse(list.get(i), model));
			}
			return builder.toString();
		}
		return null;
	}
}
