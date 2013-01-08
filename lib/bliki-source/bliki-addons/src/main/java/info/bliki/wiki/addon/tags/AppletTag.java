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
public class AppletTag extends NowikiTag {
	public AppletTag() {
		super("applet");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		buf.append('\n');
		buf.append("<applet");
		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		appendAttributes(buf, tagAtttributes);
		buf.append(">\n");
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