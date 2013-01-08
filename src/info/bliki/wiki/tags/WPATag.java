package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


public class WPATag extends HTMLTag {
	public WPATag() {
		super("a");
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}

	// public boolean isAllowedAttribute(String attributeName) {
	// if (attributeName.equals("href") || attributeName.equals("title") ||
	// attributeName.equals("rel")) {
	// return true;
	// }
	// return false;
	// }

	public String getCloseTag() {
		return "</a>";
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		if (!converter.noLinks()) {
			super.renderHTML(converter, buf, model);
		} else {
			List children = getChildren();
			if (children.size() != 0) {
				converter.nodesToText(children, buf, model);
			}
		}
	}

}