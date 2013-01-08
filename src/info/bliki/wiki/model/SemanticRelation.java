package info.bliki.wiki.model;

/**
 * A semantic web relation 
 * 
 * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic MediaWiki</a> for more information.
 *
 */
public class SemanticRelation {
	private String fRelation;

	private String fValue;

	public SemanticRelation(String relation, String value) {
		this.fRelation = relation;
		this.fValue = value;
	}

	public String getRelation() {
		return fRelation;
	}

	public void setRelation(String relation) {
		this.fRelation = relation;
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		this.fValue = value;
	}
}
