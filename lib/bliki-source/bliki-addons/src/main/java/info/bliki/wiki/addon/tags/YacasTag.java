package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;

import java.io.IOException;
import java.util.Map;


/**
 * Wiki tag for the yacas CAS applet
 * 
 */
public class YacasTag extends NowikiTag {
	public YacasTag() {
		super("yacas");
	} 

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		buf.append("\n<applet name=\"datahub\" codebase=\"../static/lib\" archive=\"yacas.jar\" code=\"net.sf.yacas.DatahubApplet\" width=\"1\" height=\"1\">\n"
				+ "</applet>\n");
		buf.append("<applet name=\"console\" codebase=\"../static/lib\" archive=\"yacas.jar\" code=\"net.sf.yacas.ConsoleApplet\" width=\"640\" height=\"240\">\n"
				+ "\n" + "  <param name=\"bkred\" value=\"224\">\n" + "  <param name=\"bkgrn\" value=\"224\">\n"
				+ "  <param name=\"bkblu\" value=\"224\">\n" + "\n" + "  <param name=\"programMode\" value=\"console\">\n" + "\n"
				+ "  <param name=\"progressbar\" value=\"true\">\n" + "  <param name=\"boxmessage\" value=\"Loading Yacas...\">\n");
		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		buf.append("\n"); 
		String attValue = (String) tagAtttributes.get("init1");
		if (attValue == null) {
			buf.append("  <param name=\"init1\" value=\"Load(\'\'yacasinit.ys\'\')\">\n");
		} else {
			attValue = Utils.escapeXml(attValue, false, false, false);
			buf.append("  <param name=\"init1\" value=\"");
			buf.append(attValue);
			buf.append("\">\n");
		}
		attValue = (String) tagAtttributes.get("init2");
		if (attValue == null) {
			buf.append("  <param name=\"init2\" value=\"TeXWrite(x):=WriteString(TeXForm(x));\">\n");
		} else {
			attValue = Utils.escapeXml(attValue, false, false, false);
			buf.append("  <param name=\"init2\" value=\"");
			buf.append(attValue);
			buf.append("\">\n");
		}
		attValue = (String) tagAtttributes.get("init3");
		if (attValue == null) {
			buf.append("  <param name=\"init3\" value=\"PrettyPrinter\'Set(\'\'TeXWrite\'\')\">\n");
		} else {
			attValue = Utils.escapeXml(attValue, false, false, false);
			buf.append("  <param name=\"init3\" value=\"");
			buf.append(attValue);
			buf.append("\">\n");
		}
		attValue = (String) tagAtttributes.get("history1");
		if (attValue == null) {
			buf.append("  <param name=\"history1\" value=\"Write(\'\'$plot2d:pencolor 255 128 128 pensize 3.0 lines2d 4 1.0 1.0 2.0 2.0 3.0 5.0 4 4$\'\')\">\n");
		} else {
			attValue = Utils.escapeXml(attValue, false, false, false);
			buf.append("  <param name=\"history1\" value=\"");
			buf.append(attValue);
			buf.append("\">\n");
		}

		buf.append("\nPlease <a href=\\\"http://java.sun.com/getjava\\\">install Java 1.5</a> (or later) to use this wiki.\n</applet>");
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}