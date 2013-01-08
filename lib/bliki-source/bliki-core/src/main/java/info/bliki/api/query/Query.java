package info.bliki.api.query;

/**
 * <b> action=query </b><br/> Query API module allows applications to get needed
 * pieces of data from the MediaWiki databases, and is loosely based on the
 * Query API interface currently available on all MediaWiki servers. All data
 * modifications will first have to use query to acquire a token to prevent
 * abuse from malicious sites.
 * 
 * <pre>
 * Parameters:
 *   titles         - A list of titles to work on
 *   pageids        - A list of page IDs to work on
 *   revids         - A list of revision IDs to work on
 *   prop           - Which properties to get for the titles/revisions/pageids
 *                    Values (separate with '|'): info, revisions, links, langlinks, images, imageinfo, templates, categories, extlinks, categoryinfo, duplicatefiles
 *   list           - Which lists to get
 *                    Values (separate with '|'): allimages, allpages, alllinks, allcategories, allusers, backlinks, blocks, categorymembers, deletedrevs, embeddedin, imageusage, logevents, recentchanges, search, usercontribs, watchlist, exturlusage, users, random
 *   meta           - Which meta data to get about the site
 *                    Values (separate with '|'): siteinfo, userinfo, allmessages
 *   generator      - Use the output of a list as the input for other prop/list/meta items
 *                    One value: links, images, templates, categories, duplicatefiles, allimages, allpages, alllinks, allcategories, backlinks, categorymembers, embeddedin, imageusage, search, watchlist, exturlusage, random
 *   redirects      - Automatically resolve redirects
 *   indexpageids   - Include an additional pageids section listing all returned page IDs.
 * </pre>
 * 
 * Example: <a href="http://en.wikipedia.org/w/api.php?action=query&amp;prop=revisions&amp;meta=siteinfo&amp;titles=Main%20Page&amp;rvprop=user%7Ccomment"
 * >api.php?action=query&amp;prop=revisions&amp;meta=siteinfo&amp;titles=Main%20
 * Page&amp;rvprop=user|comment</a>
 * 
 * 
 * 
 */
public class Query extends RequestBuilder {
	public Query() {
		super();
		action("query");
	}

	public static Query create() {
		return new Query();
	}

	/**
	 * One title to work on
	 * 
	 * @param titles
	 * @return
	 */
	public Query titles(String title) {
		put("titles", title);
		return this;
	}

	/**
	 * A list of titles to work on
	 * 
	 * @param titles
	 * @return
	 */
	public Query titles(String... titles) {
		putPipedString("titles", titles);
		return this;
	}

	/**
	 * A list of page IDs to work on
	 * 
	 * @param pageids
	 * @return
	 */
	public Query pageids(int... pageids) {
		putPipedString("pageids", pageids);
		return this;
	}

	/**
	 * A list of revision IDs to work on
	 * 
	 * @param revids
	 * @return
	 */
	public Query revids(int... revids) {
		putPipedString("revids", revids);
		return this;
	}

	/**
	 * Which property to get for the titles/revisions/pageids.<br/> Values info,
	 * revisions, links, langlinks, images, imageinfo, templates, categories,
	 * extlinks, categoryinfo, duplicatefiles
	 * 
	 * @param prop
	 * @return
	 */
	public Query prop(String prop) {
		put("prop", prop);
		return this;
	}

	/**
	 * Which properties to get for the titles/revisions/pageids.<br/> Values
	 * (internally separated with '|'): info, revisions, links, langlinks, images,
	 * imageinfo, templates, categories, extlinks, categoryinfo, duplicatefiles
	 * 
	 * @param prop
	 * @return
	 */
	public Query prop(String... prop) {
		putPipedString("prop", prop);
		return this;
	}

	/**
	 * Which lists to get.<br/> Values (separate with '|'): allimages, allpages,
	 * alllinks, allcategories, allusers, backlinks, blocks, categorymembers,
	 * deletedrevs, embeddedin, imageusage, logevents, recentchanges, search,
	 * usercontribs, watchlist, exturlusage, users, random
	 * 
	 * @param lists
	 * @return
	 */
	public Query list(String... lists) {
		putPipedString("list", lists);
		return this;
	}

	/**
	 * Which meta data to get about the site.<br/> Values (separate with '|'):
	 * siteinfo, userinfo, allmessages
	 * 
	 * @param metadata
	 * @return
	 */
	public Query meta(String... metadata) {
		putPipedString("meta", metadata);
		return this;
	}

	/**
	 * Use the output of a list as the input for other prop/list/meta items.<br/>
	 * One value: links, images, templates, categories, duplicatefiles, allimages,
	 * allpages, alllinks, allcategories, backlinks, categorymembers, embeddedin,
	 * imageusage, search, watchlist, exturlusage, random
	 * 
	 * @param generator
	 * @return
	 */
	public Query generator(String generator) {
		put("generator", generator);
		return this;
	}

	/**
	 * Automatically resolve redirects.
	 * 
	 * @return
	 */
	public Query redirects() {
		put("redirects", null);
		return this;
	}

	/**
	 * Include an additional pageids section listing all returned page IDs.
	 * 
	 * @return
	 */
	public Query indexpageids() {
		put("indexpageids", null);
		return this;
	}

	/**
	 * Which additional property to get:<br/> "protection" - List the protection
	 * level of each page "talkid" - The page ID of the talk page for each
	 * non-talk page "subjectid" - The page ID of the parent page for each talk
	 * page Values (separate with '|'): protection, talkid, subjectid, url,
	 * readable
	 * 
	 * @param prop
	 * @return
	 */
	public Query inprop(String prop) {
		put("inprop", prop);
		return this;
	}

	/**
	 * Which additional properties to get: "protection" - List the protection
	 * level of each page "talkid" - The page ID of the talk page for each
	 * non-talk page "subjectid" - The page ID of the parent page for each talk
	 * page Values (separate with '|'): protection, talkid, subjectid, url,
	 * readable
	 * 
	 * @param prop
	 * @return
	 */
	public Query inprop(String... prop) {
		putPipedString("inprop", prop);
		return this;
	}

	/**
	 * The page title to start enumerating from.
	 * 
	 * @param title
	 * @return
	 */
	public Query apfrom(String title) {
		put("apfrom", title);
		return this;
	}

	/**
	 * How many total pages to return. No more than 500 (5000 for bots) allowed.
	 * Default: 10
	 * 
	 * @param limit
	 * @return
	 */
	public Query aplimit(int limit) {
		put("apfrom", Integer.toString(limit));
		return this;
	}

    public Query intoken(String intoken) {
        put("intoken", intoken);
        return this;
    }

}
