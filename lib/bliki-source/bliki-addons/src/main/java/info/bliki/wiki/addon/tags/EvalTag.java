package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;

import java.io.IOException;
import java.util.Map;


public class EvalTag extends HTMLTag {
	// http://matheclipse.org/eval.jsp?ci=x:namex:i:value|z:namez|ta::t:valueta&ca=LabelD:D[x,z]|Binomial:Binomial[x,z]
	public EvalTag() {
		super("eval");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		StringBuilder evalUrl = new StringBuilder(512);
		// eval input fields/textareas
		Utils.appendAmpersandEscapedAttribute(evalUrl, "ci", tagAtttributes);
		// eval actions
		Utils.appendAmpersandEscapedAttribute(evalUrl, "ca", tagAtttributes);
		// URL points to http://matheclipse.org/eval.jsp
		buf.append("<a href=\"../eval.jsp?");
		buf.append(evalUrl);
		buf.append("\" >");
		renderHTMLWithoutTag(converter, buf, model);
		buf.append("</a>");
	}

}
