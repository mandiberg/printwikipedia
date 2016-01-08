package info.bliki.wiki.filter;

/**
 * The <code>WikipediaPreTagParser</code> throws this exception if the text line
 * which should be converted contains a tag which could not be rendered in a
 * <b>PRE</b> tag environment.
 *
 */
public class InvalidPreWikiTag extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -3851294684136165109L;

    public InvalidPreWikiTag(String message) {
        super(message);
    }
}
