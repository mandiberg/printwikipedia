package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.ArrayList;
import java.util.List;


public class ListTag extends AbstractHTMLTag {
	String fListChar;

	public ListTag(String listChar) {
		this.fListChar = listChar;
	}

	public List getListLines(String listChar, AbstractHTMLToWiki w, TagNode listNode) {
		List list = new ArrayList();
		List subList;
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
											subList = getListLines("*", w, subSubNode);
										} else {
											subList = getListLines("#", w, subSubNode);
										}
										// append subList to result list
										for (int k = 0; k < subList.size(); k++) {
											list.add(listChar + subList.get(k).toString());
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
							subString = subBuffer.toString();
							subString = subString.replaceAll(" \\n", " ");
							subString = subString.replaceAll("\\n", " ");
							list.add(listChar + subString);
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
		List listLines = getListLines(fListChar, w, node);
		if (listLines.size() > 0) {
			resultBuffer.append("\n");
			for (int i = 0; i < listLines.size(); i++) {
				resultBuffer.append(listLines.get(i).toString().trim());
				resultBuffer.append("\n");
			}
		}
	}

}
