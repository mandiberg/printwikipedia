package info.bliki.wiki.dump;

import org.xml.sax.SAXException;

/**
 * Interface for a filter which processes all articles from a given wikipedia
 * XML dump file
 *
 */
public interface IArticleFilter {
    /**
     * Process a single Wikipedia article
     *
     * @param article
     *          a Wikipedia article
     * @param siteinfo
     *          the site and namespace information found in the header of a
     *          Mediawiki dump. Maybe <code>null</code>
     * @throws SAXException
     *           throw a SAXException for stopping the processing
     */
    public void process(WikiArticle article, Siteinfo siteinfo) throws SAXException;
}
