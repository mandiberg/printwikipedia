package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;

import java.io.IOException;
import java.util.Map;


/**
 * Wiki tag for Java Applets: &lt;applet&gt;reference text...&lt;/applet&gt;
 * 
 */
public class WCMTag extends NowikiTag {
	public WCMTag() {
		super("wcm");
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
			buf.append(" code=\"net.sourceforge.webcompmath.applets.SimpleGraph\"");
		}
		attValue = (String) tagAtttributes.get("width");
		if (attValue==null){
			buf.append(" width=\"640\"");
		}
		attValue = (String) tagAtttributes.get("height");
		if (attValue==null){
			buf.append(" height=\"400\"");
		}
		buf.append(" codebase=\"../static/lib\" archive=\"webcompmath.jar\">\n");
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