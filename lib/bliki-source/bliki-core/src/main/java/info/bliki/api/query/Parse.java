package info.bliki.api.query;

/**
 * Module Parse <b> action=parse </b>.<br/> Parses wikitext pages and
 * returns parser output
 * 
 * Example: <a href="http://en.wikipedia.org/w/api.php?action=parse">api.php
 * ?action=parse</a>
 * 
 */
public class Parse extends RequestBuilder {
	public Parse() {
		super();
		action("parse");
	}

	public static Parse create() {
		return new Parse();
	}
	
	/**
	 * Title of page the text belongs to. Default: API
	 * 
	 * @param title
	 * @return
	 */
	public Parse title(String title) {
		put("title", title);
		return this;
	}

	/**
	 * Wikitext to parse
	 * 
	 * @param text
	 * @return
	 */
	public Parse text(String text) {
		put("text", text);
		return this;
	}

	/**
	 * Parse the content of this page. Cannot be used together with text and title
	 * 
	 * @param page
	 * @return
	 */
	public Parse page(String page) {
		put("page", page);
		return this;
	}

	public Parse redirects(String redirects) {
		put("redirects", redirects);
		return this;
	}

	/**
	 * Parse the content of this revision. Overrides page
	 * 
	 * @param oldid
	 * @return
	 */
	public Parse oldid(String oldid) {
		put("oldid", oldid);
		return this;
	}

	/**
	 * Which pieces of information to get.<br/>
   * NOTE: Section tree is only generated if there are more than 4 sections, or if the __TOC__ keyword is present<br/>
   * Values (separate with '|'): text, langlinks, categories, links, templates, images, externallinks, sections, revid, displaytitle
   *Default: text|langlinks|categories|links|templates|images|externallinks|sections|revid|displaytitle<br/>
	 *
	 * @param props
	 * @return
	 */
	public Parse prop(String... props) {
		putPipedString("prop", props);
		return this;
	}

	/**
	 * Do a pre-save transform on the input before parsing it.
   * Ignored if page or oldid is used.
   * 
	 * @param pst
	 * @return
	 */
	public Parse pst(String pst) {
		put("pst", pst);
		return this;
	}
}
