package info.bliki.wiki.tags;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import info.bliki.api.Connector;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

/**
 * Wiki tag rendering TeX math
 * 
 */
public class MathTag extends NowikiTag {
	public MathTag() {
		super("math");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		if (model.isMathtranRenderer()) {
			// use the www.mathtran.org rendering service
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
		} else {
			// prepare for jsMath client side renderer
			String content = getBodyString();
			if (content != null && content.length() > 0) {
				writer.append("<span class=\"math\">");
				copyMathLTGT(content, writer);
				writer.append("</span>");
			}
		}
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}