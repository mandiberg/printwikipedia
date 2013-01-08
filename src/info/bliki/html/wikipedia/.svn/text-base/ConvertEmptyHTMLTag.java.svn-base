package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

public abstract class ConvertEmptyHTMLTag extends AbstractHTMLTag {
	boolean fconvertPlainText;

	public ConvertEmptyHTMLTag() {
		this(false);
	}

	public ConvertEmptyHTMLTag(boolean noNewLine) {
		super(noNewLine);
	}
 
	public void emptyContent(AbstractHTMLToWiki html2WikiConverter, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
		if (!showWithoutTag) {
			open(node, resultBuffer);
			resultBuffer.append(' ');
			close(node, resultBuffer);
		}
	}

}
