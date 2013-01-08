package info.bliki.wiki.addon.tags;

import info.bliki.api.Connector;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Wiki tag rendering TeX math with the mathtran.org service
 * 
 */
public class TeXTag extends NowikiTag {
	public TeXTag() {
		super("tex");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		String content = getBodyString();
		if (content != null && content.length() > 0) {
			String sizeStr = "D=3";
			Map<String, String> tagAtttributes = getAttributes();
			String attributeValue = (String) tagAtttributes.get("d");
			if (attributeValue != null) {
				try {
					int size = Integer.parseInt(attributeValue);
					if (size > 0 && size <= 10) {
						sizeStr = "D=" + size;
					}
				} catch (NumberFormatException nfe) {
					// ignore attribute
				}
			}
			String texFormula = "http://www.mathtran.org/cgi-bin/mathtran?" + sizeStr + "&amp;tex="
					+ URLEncoder.encode(content, Connector.UTF8_CHARSET);
			writer.append("<span class=\"tex\"><img src=\"" + texFormula + "\" alt=\"");
			copyMathLTGT(content, writer);
			writer.append("\" /></span>");
		}
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}