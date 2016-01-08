package info.bliki.wiki.model;

/**
 * This exception will be thrown, if there's a problem in getting the raw wiki
 * text.
 *
 */
public class WikiModelContentException extends Exception {
    public WikiModelContentException(String message, Throwable exception) {
        super(message, exception);

    }
}
