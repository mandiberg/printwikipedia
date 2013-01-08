package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

/**
 * Interface for HTML to Wiki Tag conversion
 * 
 */
public interface HTMLTag {

	/**
	 * Write the closing wiki syntax for this tag to the result buffer
	 * 
	 * @param node
	 * @param resultBuffer
	 * 
	 * @see HTMLTag#open(TagNode, StringBuilder)
	 */
	public void close(TagNode node, StringBuilder resultBuffer);

	/**
	 * Convert the current HTML node, which has no children nodes into wiki text.
	 * 
	 * @param html2WikiConverter
	 *          the converter which contains the special wiki tag conversion
	 *          rules.
	 * @param node
	 *          the current HTML node which should be converted to HTML
	 * @param resultBuffer
	 *          the resulting HTML buffer
	 * @param showWithoutTag
	 *          if <code>true</code> don't call the open() and close() method for
	 *          this tag
	 */
	public void emptyContent(AbstractHTMLToWiki html2WikiConverter, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag);

	/**
	 * Convert the current HTML node into wiki text.
	 * 
	 * @param html2WikiConverter
	 *          the converter which contains the special wiki tag conversion
	 *          rules.
	 * @param node
	 *          the current HTML node which should be converted to HTML
	 * @param resultBuffer
	 *          the resulting HTML buffer
	 * @param showWithoutTag
	 *          if <code>true</code> don't call the open() and close() method for
	 *          this tag
	 */
	public void content(AbstractHTMLToWiki html2WikiConverter, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag);

	/**
	 * Write the opening wiki syntax for this tag to the result buffer
	 * 
	 * @param node
	 * @param resultBuffer
	 * 
	 * @see HTMLTag#close(TagNode, StringBuilder)
	 */
	public void open(TagNode node, StringBuilder resultBuffer);

}