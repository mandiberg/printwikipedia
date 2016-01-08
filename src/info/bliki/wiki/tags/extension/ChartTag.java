package info.bliki.wiki.tags.extension;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Wiki tag for the <a href="http://code.google.com/apis/chart/">Google Chart
 * API</a>
 *
 */
public class ChartTag extends HTMLTag implements INoBodyParsingTag {
    final static public HashSet<String> ALLOWED_ATTRIBUTES_SET = new HashSet<>(997);

    /**
     * See <a href=
     * "http://code.google.com/intl/de-DE/apis/chart/docs/chart_params.html">chart
     * parameters supported by the Google Chart API</a>.
     */
    final static public String[] ALLOWED_ATTRIBUTES = { "alt", "chbh", "chco", "chd", "chdl", "chdlp", "chds", "chem", "chf", "chfd",
            "chg", "chl", "chld", "chls", "chm", "chma", "choe", "chof", "chp", "chs", "chst", "cht", "chtm", "chtt", "chts", "chxt",
            "chxr", "chxl", "chxp", "chxs", "chxtc" };

    static {
        for (int i = 0; i < ALLOWED_ATTRIBUTES.length; i++) {
            ALLOWED_ATTRIBUTES_SET.add(ALLOWED_ATTRIBUTES[i]);
        }
    }

    public ChartTag() {
        super("chart");
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

        TagNode node = this;
        StringBuilder chartUrl = new StringBuilder(100);
        Map<String, String> tagAtttributes = node.getAttributes();
        Set<String> keysSet = tagAtttributes.keySet();
        for (String str : keysSet) {
            if (str.equals("alt")) {
                continue;
            }
            Utils.appendAmpersandEscapedAttribute(chartUrl, str, tagAtttributes);
        }

        buf.append("<img border=\"0\" src=\"http://chart.apis.google.com/chart?");
        buf.append(chartUrl);
        buf.append("\" alt=\"");
        Utils.appendEscapedAttribute(buf, "alt", tagAtttributes);
        buf.append("\" />");
    }

    @Override
    public boolean isAllowedAttribute(String attName) {
        return ALLOWED_ATTRIBUTES_SET.contains(attName);
    }

}
