package info.bliki.api;

/**
 * Manages search result data from the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
 */
public class SearchResult extends PageInfo {
    String snippet;
    String size;
    String wordCount;
    String timestamp;

    public SearchResult() {
        super();
        this.snippet = "";
        this.size = "";
        this.wordCount = "";
        this.timestamp = "";
    }

    /**
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * @return the snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @return the wordCount
     */
    public String getWordCount() {
        return wordCount;
    }

    /**
     * @param size the size to set
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @param snippet the snippet to set
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param wordCount the wordCount to set
     */
    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public String toString() {
        return "NS: " + ns + "; Title: " + title + "; \nSize: " + size + " word count: " + wordCount + " timestamp: " + timestamp
                + "\nSnippet:\n" + snippet;
    }
}
