package info.bliki.html.googlecode;

import info.bliki.html.wikipedia.AbstractHTMLTag;
import info.bliki.html.wikipedia.AbstractHTMLToWiki;
import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;


public class ListGCTag extends AbstractHTMLTag {
	String fListChar;

	String fULTag;

	String fOLTag;

	public ListGCTag(String listChar, String ulTag, String olTag) {
		super();
		this.fListChar = listChar;
		this.fULTag = ulTag;
		this.fOLTag = olTag;
	}

	public List<String> getListLines(String listChar, AbstractHTMLToWiki w, TagNode listNode) {
		List<String> list = new ArrayList<String>();
		List<String> subList;
		List children = listNode.getChildren();
		TagNode subNode;
		List subChildren;
		TagNode subSubNode;
		StringBuilder subBuffer;
		String subString;
		if (children.size() != 0) {
			for (int i = 0; i < children.size(); i++) {
				if (children.get(i) != null) {
					if (children.get(i) instanceof TagNode && ((TagNode) children.get(i)).getName().equals("li")) {
						subNode = ((TagNode) children.get(i));
						subChildren = subNode.getChildren();
						subBuffer = new StringBuilder(" ");

						for (int j = 0; j < subChildren.size(); j++) {
							if (subChildren.get(j) != null) {
								if (subChildren.get(j) instanceof TagNode) {
									subSubNode = ((TagNode) subChildren.get(j));
									if (subSubNode.getName().equals("ol") || subSubNode.getName().equals("ul")) {
										if (subBuffer.length() > 1) {
											subString = subBuffer.toString();
											subString = subString.replaceAll(" \\n", " ");
											subString = subString.replaceAll("\\n", " ");
											list.add(listChar + subString);
											subBuffer = new StringBuilder(" ");
										}
										if (subSubNode.getName().equals("ul")) {
											subList = getListLines(fULTag, w, subSubNode);
										} else {
											subList = getListLines(fOLTag, w, subSubNode);
										}
										// append subList to result list
										for (int k = 0; k < subList.size(); k++) {
											list.add(' ' + subList.get(k));
										}
									} else {
										w.nodeToWiki(subSubNode, subBuffer);
									}
								} else {
									w.nodeToWiki((BaseToken) subChildren.get(j), subBuffer);
								}
							}
						}

						if (subBuffer.length() > 1) {
							subBuffer.insert(0, listChar);
							subString = subBuffer.toString();
							subString = subString.replaceAll(" \\n", " ");
							subString = subString.replaceAll("\\n", " ");
							subString = subString.trim();
							list.add(subString);
							subBuffer = null;
						}

					}
				}
			}
		}
		return list;
	}

	@Override
	public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
		List<String> listLines = getListLines(fListChar, w, node);
		if (listLines.size() > 0) {
			resultBuffer.append("\n");
			for (int i = 0; i < listLines.size(); i++) {
				resultBuffer.append(listLines.get(i));
				resultBuffer.append("\n");
			}
		}
	}

}
