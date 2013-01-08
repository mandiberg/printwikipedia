package info.bliki.api;

import info.bliki.htmlcleaner.util.HtmlForm;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO: delete this class file!
 * 
 * Sources adopted from the article <a href=
 * "http://www.javaworld.com/javaworld/jw-08-2008/jw-08-java-wiki-extensions.html"
 * >Add Java extensions to your wiki</a> with permission from Randall Scarberry.
 * 
 * 
 */
public class HtmlUtilities {

	public static final String FORM_TAG = "form";

	public static final String INPUT_TAG = "input";

	public static final String TEXTAREA_TAG = "textarea";

	// Factory used in parseForm().
	private static SAXParserFactory mParserFactory = SAXParserFactory.newInstance();

	/**
	 * Extracts and parses an HTML form from a page of HTML.
	 * 
	 * @param formID
	 * @param html
	 * @return
	 */
	public static HtmlForm extractForm(String formID, String html) {

		String lowerCaseHtml = html.toLowerCase();

		String startTag = "<" + FORM_TAG + " ";
		String endTag = "</" + FORM_TAG + ">";

		int ndx = lowerCaseHtml.indexOf(startTag);
		while (ndx >= 0) {
			int ndx2 = lowerCaseHtml.indexOf(endTag, ndx + startTag.length());
			if (ndx2 > ndx) {
				String formHTML = html.substring(ndx, ndx2 + endTag.length());
				try {
					HtmlForm form = parseForm(formID, formHTML, true);
					if (form != null) {
						return form;
					}
				} catch (IOException ioe) {
				}
				ndx = lowerCaseHtml.indexOf(startTag, ndx2 + endTag.length());
			} else {
				break;
			}
		}

		return null;
	}

	public static HtmlForm parseForm(String formID, String html, boolean trapExceptions) throws IOException {

		HtmlFormHandler handler = new HtmlFormHandler(formID);
		try {

			// &nbsp; screws up the parsing. What a delicate flower SAX is.
			// html = html.replaceAll("&nbsp;", " ");
			html = StringUtils.replace(html, "&nbsp;", " ");
			SAXParser parser = mParserFactory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(new StringReader(html)));
			return handler.getForm();

		} catch (Exception e) {
			e.printStackTrace();
			if (trapExceptions) {
				return handler.getForm();
			}
			String errMsg = e.getMessage();
			String ioeMsg = null;
			if (errMsg != null && errMsg.length() > 0) {
				ioeMsg = "invalid HTML (" + errMsg + ")";
			} else {
				ioeMsg = "invalid HTML";
			}
			throw new IOException(ioeMsg);
		}
	}

	public static class HtmlFormHandler extends DefaultHandler {

		private String mFormID;

		private boolean mInForm;

		private HtmlForm mForm;

		private HtmlForm.Element mCurrentElement;

		private StringBuffer mCurrentElementBuffer;

		public HtmlFormHandler(String formID) {
			if (formID == null) {
				throw new NullPointerException();
			}
			mFormID = formID;
		}

		public HtmlForm getForm() {
			return mForm;
		}

		/**
		 * Receive notification of the start of an element.
		 * 
		 * <p>
		 * By default, do nothing. Application writers may override this method in a
		 * subclass to take specific actions at the start of each element (such as
		 * allocating a new tree node or writing output to a file).
		 * </p>
		 * 
		 * @param uri
		 *          The Namespace URI, or the empty string if the element has no
		 *          Namespace URI or if Namespace processing is not being performed.
		 * @param localName
		 *          The local name (without prefix), or the empty string if
		 *          Namespace processing is not being performed.
		 * @param qName
		 *          The qualified name (with prefix), or the empty string if
		 *          qualified names are not available.
		 * @param attributes
		 *          The attributes attached to the element. If there are no
		 *          attributes, it shall be an empty Attributes object.
		 * @exception org.xml.sax.SAXException
		 *              Any SAX exception, possibly wrapping another exception.
		 * @see org.xml.sax.ContentHandler#startElement
		 */
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			String elementName = localName;
			if (elementName.length() == 0) {
				elementName = qName;
			}
			if (elementName.equalsIgnoreCase("form")) {
				String id = null, name = null, action = null, encType = null;
				int method = HtmlForm.POST;
				int sz = attributes.getLength();
				for (int i = 0; i < sz; i++) {
					String lname = attributes.getLocalName(i);
					if (lname.length() == 0)
						lname = attributes.getQName(i);
					String value = attributes.getValue(i);
					if (lname.equalsIgnoreCase("id")) {
						if (!mFormID.equals(value)) {
							// Not interested in this form.
							return;
						} else {
							id = value;
							mInForm = true;
						}
					} else if (lname.equalsIgnoreCase("name")) {
						name = value;
					} else if (lname.equalsIgnoreCase("method")) {
						if ("get".equalsIgnoreCase(value)) {
							method = HtmlForm.GET;
						}
					} else if (lname.equalsIgnoreCase("action")) {
						action = value;
					} else if (lname.equalsIgnoreCase("enctype")) {
						encType = value;
					}
				} // for
				if (mInForm) {
					mForm = new HtmlForm(id, name, method, action, encType);
				}
			} else if (mInForm) { // It's an element nested within the desired form.

				if (elementName.equalsIgnoreCase(INPUT_TAG) || elementName.equalsIgnoreCase(TEXTAREA_TAG)) {

					int sz = attributes.getLength();
					HtmlForm.ElementAttribute[] elementAttributes = new HtmlForm.ElementAttribute[sz];
					for (int i = 0; i < sz; i++) {
						String aname = attributes.getLocalName(i);
						if (aname.length() == 0)
							aname = attributes.getQName(i);
						elementAttributes[i] = new HtmlForm.ElementAttribute(aname, attributes.getType(i), attributes.getValue(i));
					}

					mCurrentElement = new HtmlForm.Element(elementName, elementAttributes);
					mForm.addElement(mCurrentElement);
				}
			}
		}

		/**
		 * Receive notification of the end of an element.
		 * 
		 * <p>
		 * By default, do nothing. Application writers may override this method in a
		 * subclass to take specific actions at the end of each element (such as
		 * finalising a tree node or writing output to a file).
		 * </p>
		 * 
		 * @param uri
		 *          The Namespace URI, or the empty string if the element has no
		 *          Namespace URI or if Namespace processing is not being performed.
		 * @param localName
		 *          The local name (without prefix), or the empty string if
		 *          Namespace processing is not being performed.
		 * @param qName
		 *          The qualified name (with prefix), or the empty string if
		 *          qualified names are not available.
		 * @exception org.xml.sax.SAXException
		 *              Any SAX exception, possibly wrapping another exception.
		 * @see org.xml.sax.ContentHandler#endElement
		 */
		public void endElement(String uri, String localName, String qName) throws SAXException {
			String elementName = localName;
			if (elementName.length() == 0) {
				elementName = qName;
			}
			if (mInForm) {
				if (elementName.equalsIgnoreCase("form")) {
					mInForm = false;
				} else if (mCurrentElement != null && elementName.equalsIgnoreCase(mCurrentElement.getTag())) {
					if (mCurrentElementBuffer != null) {
						mCurrentElement.setEmbeddedText(mCurrentElementBuffer.toString());
						mCurrentElementBuffer = null;
					}
					mCurrentElement = null;
				}
			}
		}

		/**
		 * Receive notification of character data inside an element.
		 * 
		 * <p>
		 * By default, do nothing. Application writers may override this method to
		 * take specific actions for each chunk of character data (such as adding
		 * the data to a node or buffer, or printing it to a file).
		 * </p>
		 * 
		 * @param ch
		 *          The characters.
		 * @param start
		 *          The start position in the character array.
		 * @param length
		 *          The number of characters to use from the character array.
		 * @exception org.xml.sax.SAXException
		 *              Any SAX exception, possibly wrapping another exception.
		 * @see org.xml.sax.ContentHandler#characters
		 */
		public void characters(char ch[], int start, int length) throws SAXException {
			if (mCurrentElement != null && length > 0) {
				if (mCurrentElementBuffer == null) {
					mCurrentElementBuffer = new StringBuffer();
				}
				mCurrentElementBuffer.append(ch, start, length);
			}
		}
	}

}
