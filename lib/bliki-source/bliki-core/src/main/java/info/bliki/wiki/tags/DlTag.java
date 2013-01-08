package info.bliki.wiki.tags;

import info.bliki.wiki.model.Configuration;


public class DlTag extends HTMLBlockTag {
	public DlTag() {
		super("dl", Configuration.SPECIAL_BLOCK_TAGS);
	}
}