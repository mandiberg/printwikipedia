package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;

import java.io.IOException;
import java.util.Map;


/**
 * Wiki tag for the yacas eval tag which sends an expression to an applet
 * 
 */
public class YacasEvalTag extends NowikiTag {
	public YacasEvalTag() {
		super("yacaseval");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAttributes = node.getAttributes();

		String exprValue = (String) tagAttributes.get("expr");
		if (exprValue == null) {
			buf.append("<a href=\"javascript:yacasEval(\'NIL\');\">NIL</a>");
			return;
		}
    exprValue = Utils.escapeXml(exprValue, false, false, false);
		buf.append("<a href=\"javascript:yacasEval(\'");
		buf.append(exprValue);
		buf.append("\');\">");

		String titleValue = (String) tagAttributes.get("title");
		if (titleValue == null) {
			buf.append(exprValue);
			buf.append("</a>");
		} else {
			titleValue = Utils.escapeXml(titleValue, false, false, false);
			buf.append(titleValue);
			buf.append("</a>");
		}
		// String content = getBodyString();
		// if (content != null) {
		// content = content.trim();
		// buf.append(content);
		// }

	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}