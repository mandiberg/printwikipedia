package info.bliki.wiki.tags;

import info.bliki.wiki.model.Configuration;

public class HrTag extends HTMLEndTag {
	public HrTag() {
		super("hr");
	}

	@Override
	public String getParents() {
		return Configuration.SPECIAL_BLOCK_TAGS;
	}
}