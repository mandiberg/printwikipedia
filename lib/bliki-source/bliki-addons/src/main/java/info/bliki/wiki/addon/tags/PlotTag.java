package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.Map;


public class PlotTag extends NowikiTag implements INoBodyParsingTag {
	private final static String HEADER1 = "<div id=\"plotter\">\n"
			+ "<a href=\"#\" id=\"showp\" onclick=\"$(\'plot\').show();$(\'hidep\').show();$(\'showp\').hide();\" />Show Plot</a> \n"
			+ "<a href=\"#\" style=\"display: none;\" id=\"hidep\" onclick=\"$(\'plot\').hide();$(\'hidep\').hide();$(\'showp\').show();\" />Hide Plot</a><br />\n";

	private final static String HEADER2 = "<applet id=\"plot\" style=\"display: none;\" code=\"FuncPlotter\" width=\"910\" height=\"530\" codebase=\"../static/lib\" archive=\"funcplotter.jar,meparser.jar\">\n";

	private final static String FOOTER1 = "</applet></div>";

	public PlotTag() {
		super("plot");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		buf.append(HEADER1);
		buf.append(HEADER2);
		String attValue = (String) tagAtttributes.get("xrange");
		if (attValue != null) {
			buf.append(" <param name=\"app.startup.xInterval\" value=\"");
			Utils.appendEscapedAttribute(buf, "xrange", tagAtttributes);
			buf.append("\"/>\n");
		} else {
			buf.append(" <param name=\"app.startup.xInterval\" value=\"");
			buf.append("-10, 10");
			buf.append("\"/>\n");
		}
		attValue = (String) tagAtttributes.get("yrange");
		if (attValue != null) {
			buf.append(" <param name=\"app.startup.yInterval\" value=\"");
			Utils.appendEscapedAttribute(buf, "yrange", tagAtttributes);
			buf.append("\"/>\n");
		} else {
			buf.append(" <param name=\"app.startup.yInterval\" value=\"");
			buf.append("-10, 10");
			buf.append("\"/>\n");
		}
		attValue = (String) tagAtttributes.get("function");
		if (attValue != null) {
			buf.append(" <param name=\"app.startup.function0\" value=\"");
			Utils.appendEscapedAttribute(buf, "function", tagAtttributes);
			buf.append("\"/>\n");
		}

		buf.append(FOOTER1);
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}
