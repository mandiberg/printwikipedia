package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;

/**
 * A special Wikipedia tag (i.e. ==, ===, ''', '', ...)
 * 
 */
public class WPTag extends HTMLTag {

	public WPTag(String htmlName) {
		super(htmlName);
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WPTag) {
			// distinguish wikipedia tags from other tags
			return super.equals(obj);
		}
		return false;
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		if (getChildren().size() != 0) {
			super.renderHTML(converter, buf, model);
		}
	}

}