package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagNode;

import java.util.List;


public class ATag extends AbstractHTMLTag {
	protected final String openStr;

	protected final String closeStr;

	public ATag() {
		openStr = "[[";
		closeStr = "]]";
	}

	public ATag(String opener, String closer) {
		super();
		openStr = opener;
		closeStr = closer;
	}

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
	}

	@Override
	public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
		List children = node.getChildren();
		BaseToken tok = getFirstContent(children, "img");
		if (tok != null) {
			if (tok instanceof TagNode) {
				w.nodeToWiki(tok, resultBuffer);
			} else {
				resultBuffer.append(openStr);
				w.nodesToText(children, resultBuffer);
				resultBuffer.append(closeStr);
			}
		}
	}

	@Override
	public void close(TagNode node, StringBuilder resultBuffer) {
	}
}
