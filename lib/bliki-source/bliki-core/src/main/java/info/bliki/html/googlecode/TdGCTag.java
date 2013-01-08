package info.bliki.html.googlecode;

import info.bliki.html.wikipedia.ConvertEmptyHTMLTag;
import info.bliki.htmlcleaner.TagNode;



public class TdGCTag extends ConvertEmptyHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("|");
	}

	@Override
	public void close(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("|");
	}

}
