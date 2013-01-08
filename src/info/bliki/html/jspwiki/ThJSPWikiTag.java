package info.bliki.html.jspwiki;

import info.bliki.html.wikipedia.ConvertEmptyHTMLTag;
import info.bliki.htmlcleaner.TagNode;



public class ThJSPWikiTag extends ConvertEmptyHTMLTag {

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append("||");
	}
	
}
