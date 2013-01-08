package info.bliki.wiki.dump;

/**
 * Interface for a filter which processes all articles from a given wikipedia
 * XML dump file
 * 
 */
public interface IArticleFilter {
	/**
	 * Process a single Wikipedia article
	 *  
	 * @param article a Wikipedia article 
	 * @return <code>false</code>, if no more articles from the dump should
	 *         be parsed
	 */
	public boolean process(WikiArticle article);
}