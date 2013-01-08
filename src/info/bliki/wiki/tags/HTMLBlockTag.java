package info.bliki.wiki.tags;

/**
 * A wiki tag that's partitioning the HTML document
 * 
 */
public class HTMLBlockTag extends HTMLTag {

	private final String fAllowedParents;

	public HTMLBlockTag(String name, String allowedParents) {
		super(name);
		fAllowedParents = allowedParents;
	}

	@Override
	public String getParents() {
		return fAllowedParents;
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}

	public String getCloseTag() {
		return "\n</" + name + ">";
	}
}