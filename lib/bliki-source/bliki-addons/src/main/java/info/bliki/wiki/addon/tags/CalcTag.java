package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;

import java.io.IOException;
import java.util.Map;


/**
 * Wiki tag for simple calculation sheets
 * 
 */
public class CalcTag extends NowikiTag {
	public CalcTag() {
		super("calc");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		buf.append('\n');
		buf.append("<applet");
		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		appendAttributes(buf, tagAtttributes);
		buf.append("\n");
		String attValue = (String) tagAtttributes.get("code");
		if (attValue==null){
			buf.append(" code=\"net.sourceforge.webcompmath.applets.Evaluator\"");
		}
		attValue = (String) tagAtttributes.get("width");
		if (attValue==null){
			buf.append(" width=\"320\"");
		}
		attValue = (String) tagAtttributes.get("height");
		if (attValue==null){
			buf.append(" height=\"200\"");
		}
		buf.append(" codebase=\"../static/lib\" archive=\"meapplets.jar\">\n");
		// List children = node.getChildren();
		// if (children.size() == 0) {
		// } else { 
		//			
		// }
		String content = getBodyString();
		if (content != null) {
			content = content.trim();
			buf.append(content);
		}

		buf.append("\n</applet>");
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}