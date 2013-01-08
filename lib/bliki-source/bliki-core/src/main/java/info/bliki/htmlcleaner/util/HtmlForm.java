package info.bliki.htmlcleaner.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents form data parsed from HTML.
 * 
 * Sources adopted from the article <a
 * href="http://www.javaworld.com/javaworld/jw-08-2008/jw-08-java-wiki-extensions.html">Add
 * Java extensions to your wiki</a> with permission from Randall Scarberry.
 * 
 * @author R.Scarberry
 * 
 */
public class HtmlForm {

	public static final int GET = 0;

	public static final int POST = 1;

	private String mID;

	private String mName;

	private int mMethod = POST;

	private String mAction;

	private String mEncType;

	private List mElements = new ArrayList();

	/**
	 * Constructor.
	 * 
	 * @param id
	 *          element id, which is usually the same as the name when both are
	 *          supplied.
	 * @param name
	 *          the name, which is usually the same as id, when both are supplied.
	 * @param method
	 *          the method, POST or GET.
	 * @param action
	 *          the form action.
	 * @param encType
	 *          form encoding.
	 */
	public HtmlForm(String id, String name, int method, String action, String encType) {
		if (id == null) {
			throw new NullPointerException();
		}
		mID = id;
		mName = name != null ? name : "";
		if (method == GET || method == POST) {
			mMethod = method;
		} else {
			throw new IllegalArgumentException("invalid method: " + method);
		}
		mAction = action != null ? action : "";
		mEncType = encType != null ? encType : "";
	}

	/**
	 * Constructor for which everything is specified but the name. The name
	 * defaults to the empty string.
	 * 
	 * @param id
	 *          element id.
	 * @param method
	 *          the method, POST or GET.
	 * @param action
	 *          the form action.
	 * @param encType
	 *          form encoding.
	 */
	public HtmlForm(String id, int method, String action, String encType) {
		this(id, "", method, action, encType);
	}

	public void addElement(Element element) {
		mElements.add(element);
	}

	public int getElementCount() {
		return mElements.size();
	}

	public Element getElement(int i) {
		return (Element) mElements.get(i);
	}

	public String getID() {
		return mID;
	}

	public String getName() {
		return mName;
	}

	public int getMethod() {
		return mMethod;
	}

	public String getAction() {
		return mAction;
	}

	public String getEncType() {
		return mEncType;
	}

	/**
	 * Class to encapsulate the data found in HTML form elements.
	 * 
	 * @author R. Scarberry
	 * 
	 */
	public static class Element {

		// The element attributes.
		private ElementAttribute[] mAttributes;

		// The element's tag.
		private String mElementTag;

		// Any text embedded between the opening and closing tags.
		private String mEmbeddedText;

		public Element(String tag, ElementAttribute[] attributes) {
			mElementTag = tag;
			int n = attributes != null ? attributes.length : 0;
			mAttributes = new ElementAttribute[n];
			if (n > 0) {
				System.arraycopy(attributes, 0, mAttributes, 0, n);
			}
		}

		public ElementAttribute getElementAttributeByName(String name) {
			for (int i = 0; i < mAttributes.length; i++) {
				if (name.equalsIgnoreCase(mAttributes[i].getName())) {
					return mAttributes[i];
				}
			}
			return null;
		}

		public String getTag() {
			return mElementTag;
		}

		public boolean elementIsOfType(String type) {
			ElementAttribute att = this.getElementAttributeByName("type");
			if (att != null) {
				return type.equalsIgnoreCase(att.getValue());
			}
			return false;
		}

		public boolean isHiddenElement() {
			return mElementTag.equalsIgnoreCase("input") && elementIsOfType("hidden");
		}

		public boolean isTextElement() {
			return mElementTag.equalsIgnoreCase("input") && elementIsOfType("text");
		}

		public boolean isSubmitElement() {
			return mElementTag.equalsIgnoreCase("input") && elementIsOfType("submit");
		}

		public boolean isTextAreaElement() {
			return mElementTag.equalsIgnoreCase("textarea");
		}

		public String getEmbeddedText() {
			return mEmbeddedText != null ? mEmbeddedText : "";
		}

		public void setEmbeddedText(String text) {
			mEmbeddedText = text;
		}

	}

	/**
	 * Class representing an HTML form element attribute.
	 * 
	 * @author R. Scarberry
	 * 
	 */
	public static class ElementAttribute {

		private String mName, mType, mValue;

		/**
		 * Constructor.
		 * 
		 * @param name
		 *          name of the attribute.
		 * @param atype
		 *          the type of the attribute.
		 * @param value
		 *          the attribute value.
		 */
		public ElementAttribute(String name, String atype, String value) {
			mName = name;
			mType = atype;
			mValue = value;
		}

		public String getName() {
			return mName;
		}

		public String getType() {
			return mType;
		}

		public String getValue() {
			return mValue;
		}

		public int hashCode() {
			int hc = mName.hashCode();
			hc = 31 * hc + mType.hashCode();
			hc = 31 * hc + mValue.hashCode();
			return hc;
		}

		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof ElementAttribute) {
				ElementAttribute other = (ElementAttribute) o;
				return this.mName.equals(other.mName) && this.mType.equals(other.mType) && this.mValue.equals(other.mValue);
			}
			return false;
		}
	}

	public void setID(String mid) {
		mID = mid;
	}

	public void setName(String name) {
		mName = name;
	}

	public void setMethod(int method) {
		mMethod = method;
	}

	public void setAction(String action) {
		mAction = action;
	}

	public void setEncType(String encType) {
		mEncType = encType;
	}
}
