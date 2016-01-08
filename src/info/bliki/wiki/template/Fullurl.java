package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A template parser function for <code>{{fullurl: ... }}</code> syntax
 *
 */
public class Fullurl extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Fullurl();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws UnsupportedEncodingException {
        if (list.size() > 0) {
            String arg0 = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            if (arg0.length() > 0) {
                if (list.size() == 1) {
                    String baseUrl = model.getWikiBaseURL();
                    baseUrl = baseUrl.replace("${title}", URLEncoder.encode(Character.toUpperCase(arg0.charAt(0)) + "",
                            UTF_8.name())
                            + URLEncoder.encode(arg0.substring(1), UTF_8.name()));
                    // String result = "http://en.wikipedia.org/wiki/"
                    // + URLEncoder.encode(Character.toUpperCase(arg0.charAt(0)) + "",
                    // Connector.UTF8_CHARSET)
                    // + URLEncoder.encode(arg0.substring(1), Connector.UTF8_CHARSET);
                    return baseUrl;
                }
                String baseEditUrl = model.getWikiBaseEditURL();
                baseEditUrl = baseEditUrl.replace("${title}", URLEncoder.encode(Character.toUpperCase(arg0.charAt(0)) + "",
                        UTF_8.name())
                        + URLEncoder.encode(arg0.substring(1), UTF_8.name()));
                StringBuilder builder = new StringBuilder(baseEditUrl.length() + 64);
                builder.append(baseEditUrl);
                // builder.append("http://en.wikipedia.org/w/index.php?title=");
                // builder.append(URLEncoder.encode(Character.toUpperCase(arg0.charAt(0))
                // + "", Connector.UTF8_CHARSET));
                // builder.append(URLEncoder.encode(arg0.substring(1),
                // Connector.UTF8_CHARSET));
                for (int i = 1; i < list.size(); i++) {
                    builder.append("&amp;");
                    builder.append(isSubst ? list.get(i) : parseTrim(list.get(i), model));
                }
                return builder.toString();
            }
        }
        return null;
    }
}
