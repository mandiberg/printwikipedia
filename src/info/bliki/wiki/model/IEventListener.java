package info.bliki.wiki.model;

/**
 * A wiki event listener which will trigger the <code>on....</code> event
 * methods during the parsing process.
 *
 */
public interface IEventListener {

    /**
     * Notify the listener about a parsed header.
     *
     * @param src
     *          the currently parsed raw wikitext character array
     * @param startPosition
     *          the start offset of the wiki head including the wiki head start
     * @param endPosition
     *          the end offset of the wiki head including the wiki head end tags +
     *          1.
     * @param rawStart
     *          the start offset of the wiki head excluding the wiki head start
     * @param rawEnd
     *          the end offset of the wiki head excluding the wiki head end tags
     * @param level
     *          the header level (i.e. <code>==</code> gives level 2;
     *          <code>===</code> gives level 3;<code>====</code> gives level 4...)
     */
    public void onHeader(char[] src, int startPosition, int endPosition, int rawStart, int rawEnd, int level);

    /**
     * Notify the listener about a parsed wiki link.
     *
     * @param src
     *          the currently parsed raw wikitext character array
     * @param rawStart
     *          the start offset of the wiki link excluding the wiki link start
     *          tags '[['
     * @param rawEnd
     *          the end offset of the wiki link excluding the wiki link end tags
     *          ']]'
     * @param suffix
     *          a suffix string eventually written directly behind the wiki link
     *          (useful for plurals).
     *
     *          Example:
     *
     *          <pre>
     * Dolphins are [[aquatic mammal]]s that are closely related to [[whale]]s and [[porpoise]]s.
     * </pre>
     */
    public void onWikiLink(char[] src, int rawStart, int rawEnd, String suffix);

    /**
     * Notify the listener about a parsed template.
     *
     * @param src
     *          the currently parsed raw wikitext character array
     * @param rawStart
     *          the start offset of the wiki link excluding the wiki template
     *          start tags '{{'
     * @param rawEnd
     *          the end offset of the wiki link excluding the wiki template end
     *          tags '}}'
     */
    public void onTemplate(char[] src, int rawStart, int rawEnd);
}
