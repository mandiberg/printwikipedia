package info.bliki.wiki.model;

import info.bliki.wiki.filter.SectionHeader;

import java.util.List;

/**
 * Interface which must be implemented by the tag which renders the &quot;table
 * of content&quot;
 *
 * @see info.bliki.wiki.tags.TableOfContentTag
 */
public interface ITableOfContent {
    /**
     * Enable or disable the rendering of the &quot;table of content&quot;
     *
     * @param showToC
     *          if <code>true</code> render the &quot;table of content&quot;
     */
    public void setShowToC(boolean showToC);

    /**
     * Get a list of the section headers (i.e. &quot;==...==&quot;,
     * &quot;===...===&quot;, &quot;====...====&quot;,...) used in this
     * &quot;table of content&quot;
     *
     * @return a list with the section headers.
     */
    public List<SectionHeader> getSectionHeaders();
}
