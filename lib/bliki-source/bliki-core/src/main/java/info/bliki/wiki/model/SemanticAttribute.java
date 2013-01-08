package info.bliki.wiki.model;

/**
 * A semantic web attribute
 * 
 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
 * MediaWiki</a> for more information.
 * 
 */
public class SemanticAttribute {
	private String fAttribute;

	private String fValue;

	public SemanticAttribute(String attribute, String value) {
		this.fAttribute = attribute;
		this.fValue = value;
	}

	public String getAttribute() {
		return fAttribute;
	}

	public void setAttribute(String attribute) {
		this.fAttribute = attribute;
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		this.fValue = value;
	}
}
