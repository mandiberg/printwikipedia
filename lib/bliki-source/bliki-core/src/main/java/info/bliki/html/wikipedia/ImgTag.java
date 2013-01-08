package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

import java.util.Map;


public class ImgTag extends AbstractHTMLTag {

	@Override
	public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {

		Map<String, String> tagAtttributes = node.getAttributes();
		if (tagAtttributes != null) {
			String srcValue = (String) tagAtttributes.get("src");
			if (srcValue != null) {
				int index = srcValue.lastIndexOf('/');
				if (index >= 0) {
					srcValue = srcValue.substring(index + 1);
				}
				if (srcValue.endsWith(".jpg") || srcValue.endsWith(".jpeg") || srcValue.endsWith(".png") || srcValue.endsWith(".gif")) {
					String altValue = (String) tagAtttributes.get("alt");
					
					resultBuffer.append("[[Image:");
					resultBuffer.append(srcValue);
					if (altValue != null) {
						resultBuffer.append("|"); 
						resultBuffer.append(altValue);
					}
					resultBuffer.append("]]");
				}
			}
		}
	}
}
