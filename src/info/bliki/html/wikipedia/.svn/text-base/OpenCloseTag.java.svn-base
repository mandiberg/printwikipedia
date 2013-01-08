package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

import java.util.List;


public class OpenCloseTag extends AbstractHTMLTag {
	protected final String openStr;

	protected final String closeStr;

	public OpenCloseTag(String opener, String closer, boolean convertPlainText) {
		super(convertPlainText);
		openStr = opener;
		closeStr = closer;
	}

	public OpenCloseTag(String opener, String closer) {
		super(false);
		openStr = opener;
		closeStr = closer;
	}

	@Override
	public void open(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append(openStr);
	}

	@Override
	public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
		List children = node.getChildren();
		if (children.size() != 0) {

			StringBuilder buf = new StringBuilder();
			if (fconvertPlainText) {
				w.nodesToPlainText(children, buf);
				char ch;
				for (int i = 0; i < buf.length(); i++) {
					ch = buf.charAt(i);
					if (ch == '\n' || ch == '\r' || ch == '\t') {
						buf.setCharAt(i, ' ');
					}
				}
			} else {
				w.nodesToText(children, buf);
			}
			String str = buf.toString();
			String trimmedStr = str.trim();
			boolean showWithout = showWithoutTag;
			if (trimmedStr.length() == 0) {
				showWithout = true;
			}
			if (!showWithout) {
				open(node, resultBuffer);
			}
			if (fconvertPlainText) {
				resultBuffer.append(trimmedStr);
			} else {
				resultBuffer.append(str);
			}
			if (!showWithout) {
				close(node, resultBuffer);
			}

		}
	}

	@Override
	public void close(TagNode node, StringBuilder resultBuffer) {
		resultBuffer.append(closeStr);
	}
}
