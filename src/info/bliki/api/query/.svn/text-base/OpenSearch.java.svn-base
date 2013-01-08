package info.bliki.api.query;

/**
 * <b> action=opensearch </b> <br/> This module implements OpenSearch protocol
 * 
 * <pre>
 * Parameters: 
 *   search    - Search string 
 *   limit     - Maximum amount of results to return 
 *               No more than 100 (100 for bots) allowed. 
 *               Default: 10
 *   namespace - Namespaces to search 
 *               Values (separate with '|'): 0, 1, 2, 3, 4, 5, 6, 7, 8,
 *               9, 10, 11, 12, 13, 14, 15, 100, 101 Default: 0 
 *   format -
 * 
 * </pre>
 * 
 * Example: <a
 * href="http://en.wikipedia.org/w/api.php?action=opensearch&amp;search=Te"
 * >api.php?action=opensearch&amp;search=Te</a>
 * 
 * 
 */
public class OpenSearch extends RequestBuilder {
	public OpenSearch() {
		super();
		action("opensearch");
	}

	public OpenSearch search(String search) {
		put("search", search);
		return this;
	}
	
	public static OpenSearch create() {
		return new OpenSearch();
	}
	
	public OpenSearch limit(int limit) {
		put("limit", Integer.toString(limit));
		return this;
	}

	/**
	 * namespace - a Namespace to search<br/>Values: 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
	 * 10, 11, 12, 13, 14, 15, 100, 101 Default: 0
	 * 
	 * @param namespaces
	 * @return
	 */
	public OpenSearch namespace(int namespace) {
		put("namespace", Integer.toString(namespace));
		return this;
	}

	/**
	 * namespace - Namespaces to search<br/> Values (internally separated with
	 * '|'): 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 100, 101
	 * Default: 0
	 * 
	 * @param namespaces
	 * @return
	 */
	public OpenSearch namespace(int... namespaces) {
		putPipedString("namespace", namespaces);
		return this;
	}
}
