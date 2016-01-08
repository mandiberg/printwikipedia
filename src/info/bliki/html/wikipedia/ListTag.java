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

    public List<String> getListLines(String listChar, AbstractHTMLToWiki w,
        TagNode listNode) {
        List<String> list = new ArrayList<>();
        List<String> subList;
        List<Object> children = listNode.getChildren();
        TagNode subNode;
        List<Object> subChildren;
        TagNode subSubNode;
        StringBuilder subBuffer;
        String subString;
        if (children.size() != 0) {
            for (Object child : children) {
                if (child != null) {
                    if (child instanceof TagNode
                        && ((TagNode) child).getName().equals("li")) {
                        subNode = ((TagNode) child);
                        subChildren = subNode.getChildren();
                        subBuffer = new StringBuilder(" ");

                        for (Object subChild : subChildren) {
                            if (subChild != null) {
                                if (subChild instanceof TagNode) {
                                    subSubNode = ((TagNode) subChild);
                                    if (subSubNode.getName().equals("ol")
                                        || subSubNode.getName().equals("ul")) {
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
                                } else if (subChild instanceof List) {
                                    @SuppressWarnings("unchecked")
                                    final List<Object> subChildList = (List<Object>) subChild;
                                    w.nodesToText(subChildList, subBuffer);
                                } else {
                                    w.nodeToWiki((BaseToken) subChild, subBuffer);
                                }
                            }
                        }

                        if (subBuffer.length() > 1) {
                            subString = subBuffer.toString();
                            subString = subString.replaceAll(" \\n", " ");
                            subString = subString.replaceAll("\\n", " ");
                            list.add(listChar + ' ' + subString.trim());
                            subBuffer = null;
                        }

                    }
                }
            }
        }
        return list;
    }

    @Override
    public void content(AbstractHTMLToWiki w, TagNode node,
        StringBuilder resultBuffer, boolean showWithoutTag) {
        List<String> listLines = getListLines(fListChar, w, node);
        if (listLines.size() > 0) {
            resultBuffer.append("\n");
            for (int i = 0; i < listLines.size(); i++) {
                resultBuffer.append(listLines.get(i).toString().trim());
                resultBuffer.append("\n");
            }
        }
    }

}
