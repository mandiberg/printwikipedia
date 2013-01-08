package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.Map;


/**
 * Wiki tag for the <a href="http://code.google.com/apis/chart/">Google Chart
 * API</a>
 * 
 */
public class ChartTag extends HTMLTag implements INoBodyParsingTag {
	public ChartTag() {
		super("chart");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		StringBuilder chartUrl = new StringBuilder(100);
		Utils.appendEscapedAttribute(chartUrl, "url", tagAtttributes);
		// chart type
		Utils.appendAmpersandEscapedAttribute(chartUrl, "cht", tagAtttributes);
		// chart data
		Utils.appendAmpersandEscapedAttribute(chartUrl, "chd", tagAtttributes);
		// chart size in pixel
		Utils.appendAmpersandEscapedAttribute(chartUrl, "chs", tagAtttributes);
		// x-axis and y-axis labels are required?
		Utils.appendAmpersandEscapedAttribute(chartUrl, "chxt", tagAtttributes);
		// the x-axis and y-axis labels (separated by pipe)
		Utils.appendAmpersandEscapedAttribute(chartUrl, "chxl", tagAtttributes);
		buf.append("<img border=\"0\" src=\"http://chart.apis.google.com/chart?");
		buf.append(chartUrl);
		buf.append("\" alt=\"");
		Utils.appendEscapedAttribute(buf, "alt", tagAtttributes);
		buf.append("\" />");
	}

}