package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

public class PTag extends HTMLBlockTag {
	public PTag() {
		super("p", "|address" + Configuration.SPECIAL_BLOCK_TAGS);
	}

	public String getCloseTag() {
		return "\n</p>";
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
		// super.renderHTML(converter, buf, model);
		// use this to avoid empty <p /> tags in the html
		List<Object> children = this.getChildren();
		if (children.size() != 0) {
			super.renderHTML(converter, buf, model);
		}
	}
}