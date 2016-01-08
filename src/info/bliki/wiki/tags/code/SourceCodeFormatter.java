package info.bliki.wiki.tags.code;

/**
 * Displays source code with syntax highlighting
 *
 */
public interface SourceCodeFormatter {
    /**
     * Convert the given <code>content</code> string to HTML synatx highlighted
     * string.
     *
     * @param content
     * @return
     */
    public String filter(String content);
}
