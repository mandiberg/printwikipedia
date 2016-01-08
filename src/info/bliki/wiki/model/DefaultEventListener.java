package info.bliki.wiki.model;

/**
 * A default wiki event listener implementation which will trigger the
 * <code>on....</code> event methods during the parsing process.
 *
 * This listener does nothing useful, but implementing wrappers for the
 * interface methods.
 *
 */
public class DefaultEventListener implements IEventListener {
    public final static IEventListener CONST = new DefaultEventListener();

    /** {@inheritDoc} */
    @Override
    public void onHeader(char[] src, int startPosition, int endPosition, int rawStart, int rawEnd, int level) {
    }

    /** {@inheritDoc} */
    @Override
    public void onWikiLink(char[] src, int rawStart, int rawEnd, String suffix) {
    }

    /** {@inheritDoc} */
    @Override
    public void onTemplate(char[] src, int rawStart, int rawEnd) {
    }

}
