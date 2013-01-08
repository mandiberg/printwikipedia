package info.bliki.wiki.tags.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * WikiTagNode represents a generic tag. If no scanner is registered for a given
 * tag name, this is what you get. This is also the base class for all tags
 * created by the parser.
 */
public class WikiTagNode extends AbstractNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5135255005207719745L;

	/**
	 * An empty set of tag names.
	 */
	private final static String[] NONE = new String[0];

	/**
	 * The tag attributes. Objects of type {@link Attribute}. The first element is
	 * the tag name, subsequent elements being either whitespace or real
	 * attributes.
	 */
	protected List<NodeAttribute> mAttributes;

	/**
	 * Create an empty tag.
	 */
	public WikiTagNode() {
		this(-1, -1, new ArrayList<NodeAttribute>());
	}

	/**
	 * Create a tag with the location and attributes provided
	 * 
	 * @param page
	 *          The page this tag was read from.
	 * @param start
	 *          The starting offset of this node within the page.
	 * @param end
	 *          The ending offset of this node within the page.
	 * @param attributes
	 *          The list of attributes that were parsed in this tag.
	 * @see Attribute
	 */
	public WikiTagNode(int start, int end, ArrayList<NodeAttribute> attributes) {
		super(start, end);

		mAttributes = attributes;
		if ((null == mAttributes) || (0 == mAttributes.size())) {
			String[] names;

			names = getIds();
			if ((null != names) && (0 != names.length))
				setTagName(names[0]);
			else
				setTagName(""); // make sure it's not null
		} else {
			setTagName(((Attribute) mAttributes.get(0)).getName());
		}
	}

	/**
	 * Returns the value of an attribute.
	 * 
	 * @param name
	 *          Name of attribute, case insensitive.
	 * @return The value associated with the attribute or null if it does not
	 *         exist, or is a stand-alone or
	 */
	public String getAttribute(String name) {
		Attribute attribute;
		String ret;

		ret = null;

		attribute = getAttributeEx(name);
		if (null != attribute)
			ret = attribute.getValue();

		return (ret);
	}

	/**
	 * Set attribute with given key, value pair. Figures out a quote character to
	 * use if necessary.
	 * 
	 * @param key
	 *          The name of the attribute.
	 * @param value
	 *          The value of the attribute.
	 */
	public void setAttribute(String key, String value) {
		char ch;
		boolean needed;
		boolean singleq;
		boolean doubleq;
//		String ref;
//		StringBuffer buffer;
		char quote;
		Attribute attribute;

		// first determine if there's whitespace in the value
		// and while we'return at it find a suitable quote character
		needed = false;
		singleq = true;
		doubleq = true;
		if (null != value)
			for (int i = 0; i < value.length(); i++) {
				ch = value.charAt(i);
				if (Character.isWhitespace(ch))
					needed = true;
				else if ('\'' == ch)
					singleq = false;
				else if ('"' == ch)
					doubleq = false;
			}

		// now apply quoting
		if (needed) {
			if (doubleq)
				quote = '"';
			else if (singleq)
				quote = '\'';
			else {
				// uh-oh, we need to convert some quotes into character
				// references
				// convert all double quotes into &#34;
				quote = '"';
				//ref = "&quot;"; // Translate.encode (quote);
				// JDK 1.4: value = value.replaceAll ("\"", ref);
				value = StringUtils.replace(value, "\"", "&quot;");
				// buffer = new StringBuffer(value.length() * 5);
				// for (int i = 0; i < value.length(); i++) {
				// ch = value.charAt(i);
				// if (quote == ch)
				// buffer.append(ref);
				// else
				// buffer.append(ch);
				// }
				// value = buffer.toString();
			}
		} else
			quote = 0;
		attribute = getAttributeEx(key);
		if (null != attribute) { // see if we can splice it in rather than
			// replace it
			attribute.setValue(value);
			if (0 != quote)
				attribute.setQuote(quote);
		} else
			setAttribute(key, value, quote);
	}

	/**
	 * Remove the attribute with the given key, if it exists.
	 * 
	 * @param key
	 *          The name of the attribute.
	 */
	public void removeAttribute(String key) {
		Attribute attribute;

		attribute = getAttributeEx(key);
		if (null != attribute)
			getAttributesEx().remove(attribute);
	}

	/**
	 * Set attribute with given key, value pair where the value is quoted by
	 * quote.
	 * 
	 * @param key
	 *          The name of the attribute.
	 * @param value
	 *          The value of the attribute.
	 * @param quote
	 *          The quote character to be used around value. If zero, it is an
	 *          unquoted value.
	 */
	public void setAttribute(String key, String value, char quote) {
		setAttribute(new NodeAttribute(key, value, quote));
	}

	/**
	 * Returns the attribute with the given name.
	 * 
	 * @param name
	 *          Name of attribute, case insensitive.
	 * @return The attribute or null if it does not exist.
	 */
	public NodeAttribute getAttributeEx(String name) {
		List<NodeAttribute> attributes;
		int size;
		NodeAttribute attribute;
		String string;
		NodeAttribute ret;

		ret = null;

		attributes = getAttributesEx();
		if (null != attributes) {
			size = attributes.size();
			for (int i = 0; i < size; i++) {
				attribute = attributes.get(i);
				string = attribute.getName();
				if ((null != string) && name.equalsIgnoreCase(string)) {
					ret = attribute;
					i = size; // exit fast
				}
			}
		}

		return ret;
	}

	/**
	 * Set an attribute.
	 * 
	 * @param attribute
	 *          The attribute to set.
	 * @see #setAttribute(Attribute)
	 */
	public void setAttributeEx(NodeAttribute attribute) {
		setAttribute(attribute);
	}

	/**
	 * Set an attribute. This replaces an attribute of the same name. To set the
	 * zeroth attribute (the tag name), use setTagName().
	 * 
	 * @param attribute
	 *          The attribute to set.
	 */
	public void setAttribute(NodeAttribute attribute) {
		boolean replaced;
		List<NodeAttribute> attributes;
		int length;
		String name;
		Attribute test;
		String test_name;

		replaced = false;
		attributes = getAttributesEx();
		length = attributes.size();
		if (0 < length) {
			name = attribute.getName();
			for (int i = 1; i < attributes.size(); i++) {
				test = attributes.get(i);
				test_name = test.getName();
				if (null != test_name)
					if (test_name.equalsIgnoreCase(name)) {
						attributes.set(i, attribute);
						replaced = true;
					}
			}
		}
		if (!replaced) {
			// add whitespace between attributes
			if ((0 != length) && !(attributes.get(length - 1)).isWhitespace())
				attributes.add(new NodeAttribute(" "));
			attributes.add(attribute);
		}
	}

	/**
	 * Gets the attributes in the tag.
	 * 
	 * @return Returns the list of {@link Attribute Attributes} in the tag. The
	 *         first element is the tag name, subsequent elements being either
	 *         whitespace or real attributes.
	 */
	public List<NodeAttribute> getAttributesEx() {
		return (mAttributes);
	}

	/**
	 * Return the name of this tag.
	 * <p>
	 * <em>
	 * Note: This value is converted to uppercase and does not
	 * begin with "/" if it is an end tag. Nor does it end with
	 * a slash in the case of an XML type tag.
	 * To get at the original text of the tag name use
	 * {@link #getRawTagName getRawTagName()}.
	 * The conversion to uppercase is performed with an ENGLISH locale.
	 * </em>
	 * 
	 * @return The tag name.
	 */
	public String getTagName() {
		String ret;

		ret = getRawTagName();
		if (null != ret) {
			ret = ret.toLowerCase();
			if (ret.startsWith("/"))
				ret = ret.substring(1);
			if (ret.endsWith("/"))
				ret = ret.substring(0, ret.length() - 1);
		}

		return (ret);
	}

	/**
	 * Return the name of this tag.
	 * 
	 * @return The tag name or null if this tag contains nothing or only
	 *         whitespace.
	 */
	public String getRawTagName() {
		List<NodeAttribute> attributes;
		String ret;

		ret = null;

		attributes = getAttributesEx();
		if (0 != attributes.size())
			ret = attributes.get(0).getName();

		return (ret);
	}

	/**
	 * Set the name of this tag. This creates or replaces the first attribute of
	 * the tag (the zeroth element of the attribute vector).
	 * 
	 * @param name
	 *          The tag name.
	 */
	public void setTagName(String name) {
		NodeAttribute attribute;
		List<NodeAttribute> attributes;
		Attribute zeroth;

		attribute = new NodeAttribute(name, null, (char) 0);
		attributes = getAttributesEx();
		if (null == attributes) {
			attributes = new ArrayList<NodeAttribute>();
			setAttributesEx(attributes);
		}
		if (0 == attributes.size())
			// nothing added yet
			attributes.add(attribute);
		else {
			zeroth = attributes.get(0);
			// check for attribute that looks like a name
			if ((null == zeroth.getValue()) && (0 == zeroth.getQuote()))
				attributes.set(0, attribute);
			else
				attributes.add(0, attribute);
		}
	}

	/**
	 * Return the text contained in this tag.
	 * 
	 * @return The complete contents of the tag (within the angle brackets).
	 */
	@Override
	public String getText() {
		String ret;

		ret = toHtml();
		ret = ret.substring(1, ret.length() - 1);

		return (ret);
	}

	/**
	 * Sets the attributes. NOTE: Values of the extended hashtable are two element
	 * arrays of String, with the first element being the original name (not
	 * uppercased), and the second element being the value.
	 * 
	 * @param attribs
	 *          The attribute collection to set.
	 */
	public void setAttributesEx(List<NodeAttribute> attribs) {
		mAttributes = attribs;
	}

	/**
	 * Sets the nodeBegin.
	 * 
	 * @param tagBegin
	 *          The nodeBegin to set
	 */
	public void setTagBegin(int tagBegin) {
		nodeBegin = tagBegin;
	}

	/**
	 * Gets the nodeBegin.
	 * 
	 * @return The nodeBegin value.
	 */
	public int getTagBegin() {
		return (nodeBegin);
	}

	/**
	 * Sets the nodeEnd.
	 * 
	 * @param tagEnd
	 *          The nodeEnd to set
	 */
	public void setTagEnd(int tagEnd) {
		nodeEnd = tagEnd;
	}

	/**
	 * Gets the nodeEnd.
	 * 
	 * @return The nodeEnd value.
	 */
	public int getTagEnd() {
		return (nodeEnd);
	}

	/**
	 * Get the plain text from this node.
	 * 
	 * @return An empty string (tag contents do not display in a browser). If you
	 *         want this tags HTML equivalent, use {@link #toHtml toHtml()}.
	 */
	@Override
	public String toPlainTextString() {
		return ("");
	}

	/**
	 * Render the tag as HTML. A call to a tag's <code>toHtml()</code> method will
	 * render it in HTML.
	 * 
	 * @param verbatim
	 *          If <code>true</code> return as close to the original page text as
	 *          possible.
	 * @return The tag as an HTML fragment.
	 * @see org.htmlparser.Node#toHtml()
	 */
	@Override
	public String toHtml(boolean verbatim) {
		int length;
		int size;
		List<NodeAttribute> attributes;
		NodeAttribute attribute;
		StringBuilder ret;

		length = 2;
		attributes = getAttributesEx();
		size = attributes.size();
		for (int i = 0; i < size; i++) {
			attribute = attributes.get(i);
			length += attribute.getLength();
		}
		ret = new StringBuilder(length);
		ret.append("<");
		for (int i = 0; i < size; i++) {
			attribute = attributes.get(i);
			attribute.toString(ret);
			if (i < size - 1) {
				ret.append(" ");
			}
		}
		ret.append(">");

		return (ret.toString());
	}

	/**
	 * Print the contents of the tag.
	 * 
	 * @return An string describing the tag. For text that looks like HTML use
	 *         #toHtml().
	 */
	// public String toString ()
	// {
	// String text;
	// String type;
	// Cursor start;
	// Cursor end;
	// StringBuffer ret;
	//
	// text = getText ();
	// ret = new StringBuffer (20 + text.length ());
	// if (isEndTag ())
	// type = "End";
	// else
	// type = "Tag";
	// start = new Cursor (getPage (), getStartPosition ());
	// end = new Cursor (getPage (), getEndPosition ());
	// ret.append (type);
	// ret.append (" (");
	// ret.append (start);
	// ret.append (",");
	// ret.append (end);
	// ret.append ("): ");
	// if (80 < ret.length () + text.length ())
	// {
	// text = text.substring (0, 77 - ret.length ());
	// ret.append (text);
	// ret.append ("...");
	// }
	// else
	// ret.append (text);
	//        
	// return (ret.toString ());
	// }
	/**
	 * Is this an empty xml tag of the form &lt;tag/&gt;.
	 * 
	 * @return true if the last character of the last attribute is a '/'.
	 */
	public boolean isEmptyXmlTag() {
		List<NodeAttribute> attributes;
		int size;
		Attribute attribute;
		String name;
		int length;
		boolean ret;

		ret = false;

		attributes = getAttributesEx();
		size = attributes.size();
		if (0 < size) {
			attribute = attributes.get(size - 1);
			name = attribute.getName();
			if (null != name) {
				length = name.length();
				ret = name.charAt(length - 1) == '/';
			}
		}

		return (ret);
	}

	/**
	 * Set this tag to be an empty xml node, or not. Adds or removes an ending
	 * slash on the tag.
	 * 
	 * @param emptyXmlTag
	 *          If true, ensures there is an ending slash in the node, i.e.
	 *          &lt;tag/&gt;, otherwise removes it.
	 */
	public void setEmptyXmlTag(boolean emptyXmlTag) {
		List<NodeAttribute> attributes;
		int size;
		NodeAttribute attribute;
		String name;
		String value;
		int length;

		attributes = getAttributesEx();
		size = attributes.size();
		if (0 < size) {
			attribute = attributes.get(size - 1);
			name = attribute.getName();
			if (null != name) {
				length = name.length();
				value = attribute.getValue();
				if (null == value)
					if (name.charAt(length - 1) == '/') {
						// already exists, remove if requested
						if (!emptyXmlTag)
							if (1 == length)
								attributes.remove(size - 1);
							else {
								// this shouldn't happen, but covers the case
								// where no whitespace separates the slash
								// from the previous attribute
								name = name.substring(0, length - 1);
								attribute = new NodeAttribute(name, null);
								attributes.remove(size - 1);
								attributes.add(attribute);
							}
					} else {
						// ends with attribute, add whitespace + slash if
						// requested
						if (emptyXmlTag) {
							attribute = new NodeAttribute(" ");
							attributes.add(attribute);
							attribute = new NodeAttribute("/", null);
							attributes.add(attribute);
						}
					}
				else {
					// some valued attribute, add whitespace + slash if
					// requested
					if (emptyXmlTag) {
						attribute = new NodeAttribute(" ");
						attributes.add(attribute);
						attribute = new NodeAttribute("/", null);
						attributes.add(attribute);
					}
				}
			} else {
				// ends with whitespace, add if requested
				if (emptyXmlTag) {
					attribute = new NodeAttribute("/", null);
					attributes.add(attribute);
				}
			}
		} else
		// nothing there, add if requested
		if (emptyXmlTag) {
			attribute = new NodeAttribute("/", null);
			attributes.add(attribute);
		}
	}

	/**
	 * Predicate to determine if this tag is an end tag (i.e. &lt;/HTML&gt;).
	 * 
	 * @return <code>true</code> if this tag is an end tag.
	 */
	public boolean isEndTag() {
		String raw;

		raw = getRawTagName();

		return ((null == raw) ? false : ((0 != raw.length()) && ('/' == raw.charAt(0))));
	}

	/**
	 * Return the set of names handled by this tag. Since this a a generic tag, it
	 * has no ids.
	 * 
	 * @return The names to be matched that create tags of this type.
	 */
	public String[] getIds() {
		return (NONE);
	}

	/**
	 * Return the set of tag names that cause this tag to finish. These are the
	 * normal (non end tags) that if encountered while scanning (a composite tag)
	 * will cause the generation of a virtual tag. Since this a a non-composite
	 * tag, the default is no enders.
	 * 
	 * @return The names of following tags that stop further scanning.
	 */
	public String[] getEnders() {
		return (NONE);
	}

	/**
	 * Return the set of end tag names that cause this tag to finish. These are
	 * the end tags that if encountered while scanning (a composite tag) will
	 * cause the generation of a virtual tag. Since this a a non-composite tag, it
	 * has no end tag enders.
	 * 
	 * @return The names of following end tags that stop further scanning.
	 */
	public String[] getEndTagEnders() {
		return (NONE);
	}

}
