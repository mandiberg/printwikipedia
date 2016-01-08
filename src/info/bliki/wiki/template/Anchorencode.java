package info.bliki.wiki.template;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.model.IWikiModel;

import java.util.List;

/**
 * A template parser function for <code>{{urlencode: ... }}</code> syntax
 *
 */
public class Anchorencode extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Anchorencode();

    @Override
    public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) {
        if (list.size() > 0) {
            String result = isSubst ? list.get(0) : parseTrim(list.get(0), model);
            return Encoder.encodeDotUrl(result);
        }
        return null;
    }
}
