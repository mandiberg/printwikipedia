package info.bliki.wiki.tags.util;

import java.io.Serializable;

/**
 * The concrete base class for all types of nodes.
 */
public abstract class AbstractNode implements Serializable
{
	/**
	 * The beginning position of the tag in the line
	 */
	protected int nodeBegin;

	/**
	 * The ending position of the tag in the line
	 */
	protected int nodeEnd;

	/**
	 * Create an abstract node with the page positions given. Remember the page
	 * and start & end cursor positions.
	 * 
	 * @param page
	 *            The page this tag was read from.
	 * @param start
	 *            The starting offset of this node within the page.
	 * @param end
	 *            The ending offset of this node within the page.
	 */
	public AbstractNode(int start, int end)
	{
		nodeBegin = start;
		nodeEnd = end;
	}

	/**
	 * Clone this object. Exposes java.lang.Object clone as a public method.
	 * 
	 * @return A clone of this object.
	 * @exception CloneNotSupportedException
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return (super.clone());
	}

	/**
	 * Returns a string representation of the node. It allows a simple string
	 * transformation of a web page, regardless of node type.<br>
	 * Typical application code (for extracting only the text from a web page)
	 * would then be simplified to:<br>
	 * 
	 * <pre>
	 * Node node;
	 * for (Enumeration e = parser.elements(); e.hasMoreElements();) {
	 * 	node = (Node) e.nextElement();
	 * 	System.out.println(node.toPlainTextString());
	 * 	// or do whatever processing you wish with the plain text string
	 * }
	 * </pre>
	 * 
	 * @return The 'browser' content of this node.
	 */
	public abstract String toPlainTextString();

	/**
	 * Return the HTML for this node. This should be the sequence of characters
	 * that were encountered by the parser that caused this node to be created.
	 * Where this breaks down is where broken nodes (tags and remarks) have been
	 * encountered and fixed. Applications reproducing html can use this method
	 * on nodes which are to be used or transferred as they were received or
	 * created.
	 * 
	 * @return The sequence of characters that would cause this node to be
	 *         returned by the parser or lexer.
	 */
	public String toHtml()
	{
		return (toHtml(false));
	}

	/**
	 * Return the HTML for this node. This should be the exact sequence of
	 * characters that were encountered by the parser that caused this node to
	 * be created. Where this breaks down is where broken nodes (tags and
	 * remarks) have been encountered and fixed. Applications reproducing html
	 * can use this method on nodes which are to be used or transferred as they
	 * were received or created.
	 * 
	 * @param verbatim
	 *            If <code>true</code> return as close to the original page
	 *            text as possible.
	 * @return The (exact) sequence of characters that would cause this node to
	 *         be returned by the parser or lexer.
	 */
	public abstract String toHtml(boolean verbatim);

	/**
	 * Gets the starting position of the node.
	 * 
	 * @return The start position.
	 */
	public int getStartPosition()
	{
		return (nodeBegin);
	}

	/**
	 * Sets the starting position of the node.
	 * 
	 * @param position
	 *            The new start position.
	 */
	public void setStartPosition(int position)
	{
		nodeBegin = position;
	}

	/**
	 * Gets the ending position of the node.
	 * 
	 * @return The end position.
	 */
	public int getEndPosition()
	{
		return (nodeEnd);
	}

	/**
	 * Sets the ending position of the node.
	 * 
	 * @param position
	 *            The new end position.
	 */
	public void setEndPosition(int position)
	{
		nodeEnd = position;
	}

	/**
	 * Returns the text of the node.
	 * 
	 * @return The text of this node. The default is <code>null</code>.
	 */
	public String getText()
	{
		return null;
	}

	/**
	 * Sets the string contents of the node.
	 * 
	 * @param text
	 *            The new text for the node.
	 */
	public void setText(String text)
	{
	}

}
