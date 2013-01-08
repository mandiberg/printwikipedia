package info.bliki.wiki.addon.docbook;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
 
/**
 * 
 * Initial source copied from the <a href="https://textile-j.dev.java.net/">Textile-J</a> project
 *
 */
class HtmlToDocbookContentHandler implements ContentHandler {

	private BookEmitter bookEmitter = new BookEmitter();

	private NestingEmitter emitter = bookEmitter;

	private XMLStreamWriter writer;

	private String doctype = "<!DOCTYPE book PUBLIC \"-//OASIS//DTD DocBook XML V4.5//EN\" \"http://www.oasis-open.org/docbook/xml/4.5/docbookx.dtd\">";

	private Map<String, String> acronyms = new HashMap<String, String>();

	/**
	 * 
	 * @param writer
	 *          the writer to which docbook content is created.
	 */
	public HtmlToDocbookContentHandler(XMLStreamWriter writer) {
		super();
		this.writer = writer;
	}

	public HtmlToDocbookContentHandler() {
	}

	private Emitter newEmitter(String elementName) {
		if ("p".equals(elementName)) {
			return new SimpleMappingEmitter("para");
		} else if ("acronym".equals(elementName)) {
			return new GlosstermEmitter();
		} else if ("a".equals(elementName)) {
			return new AnchorEmitter();
		} else if ("img".equals(elementName)) {
			return new ImageEmitter();
		} else if ("ul".equals(elementName)) {
			return new SimpleMappingEmitter("itemizedlist");
		} else if ("ol".equals(elementName)) {
			return new SimpleMappingEmitter("orderedlist");
		} else if ("li".equals(elementName)) {
			return new SimpleMappingEmitter("listitem", "para");
		} else if ("em".equals(elementName)) {
			return new SimpleMappingEmitter("emphasis");
		} else if ("strong".equals(elementName) || "b".equals(elementName)) {
			SimpleMappingEmitter emitter = new SimpleMappingEmitter("emphasis");
			emitter.setAttribute("role", "bold");
			return emitter;
		} else if (elementName.matches("h\\d")) {
			return new HeaderEmitter(Integer.parseInt(elementName.substring(1)));
		} else if ("i".equals(elementName)) {
			SimpleMappingEmitter emitter = new SimpleMappingEmitter("emphasis");
			emitter.setAttribute("role", "italic");
			return emitter;
		} else if ("cite".equals(elementName)) {
			return new SimpleMappingEmitter("citation");
		} else if ("code".equals(elementName)) {
			return new SimpleMappingEmitter("code");
		} else if ("pre".equals(elementName)) {
			return new SimpleMappingEmitter("literallayout");
		} else if ("del".equals(elementName)) {
			SimpleMappingEmitter emitter = new SimpleMappingEmitter("emphasis");
			emitter.setAttribute("role", "del");
			return emitter;
		} else if ("ins".equals(elementName)) {
			SimpleMappingEmitter emitter = new SimpleMappingEmitter("emphasis");
			emitter.setAttribute("role", "ins");
			return emitter;
		} else if ("sup".equals(elementName)) {
			return new SimpleMappingEmitter("superscript");
		} else if ("sub".equals(elementName)) {
			return new SimpleMappingEmitter("subscript");
		}
		return new NestingEmitter();
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		try {
			emitter.content(writer, ch, start, length);
		} catch (XMLStreamException e) {
			throw new SAXException(e);
		}
	}

	public void endDocument() throws SAXException {
		try {
			emitter.close();
			emitter = null;
			writer.writeEndDocument();

			acronyms.clear();
		} catch (XMLStreamException e) {
			throw new SAXException(e);
		}
	}

	public void endElement(String uri, String localName, String name) throws SAXException {
		try {
			if (emitter.end(writer, localName)) {
				emitter.close();
				emitter = new NestingEmitter();
			}
		} catch (XMLStreamException e) {
			throw new SAXException(e);
		}
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	public void processingInstruction(String target, String data) throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void startDocument() throws SAXException {
		try {
			writer.writeStartDocument();
			writer.writeDTD(doctype);
		} catch (XMLStreamException e) {
			throw new SAXException(e);
		}
	}

	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
		try {
			if (!emitter.start(writer, localName, atts)) {
				throw new IllegalStateException();
			}
		} catch (XMLStreamException e) {
			throw new SAXException(e);
		}
	}

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	public String getDoctype() {
		return doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	public String getBookTitle() {
		return bookEmitter.getBookTitle();
	}

	public void setBookTitle(String bookTitle) {
		bookEmitter.setBookTitle(bookTitle);
	}

	public XMLStreamWriter getWriter() {
		return writer;
	}

	public void setWriter(XMLStreamWriter writer) {
		this.writer = writer;
	}

	private interface Emitter {
		/**
		 * 
		 * @return true if the emitter started successfully, otherwise false
		 */
		public boolean start(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException;

		public void content(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException;

		/**
		 * 
		 * @return true if the emitter is done
		 */
		public boolean end(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException;

		public void close() throws XMLStreamException;

	}

	private class SimpleMappingEmitter extends NestingEmitter {

		private final String[] tags;

		private Map<String, String> attributes;

		public SimpleMappingEmitter(String... docbookTagNames) {
			tags = docbookTagNames;
		}

		/**
		 * Add an attribute to the last of the tags
		 * 
		 * @param name
		 *          the attribute name
		 * @param value
		 *          the attribute value
		 */
		public void setAttribute(String name, String value) {
			if (attributes == null) {
				attributes = new TreeMap<String, String>();
			}
			attributes.put(name, value);
		}

		@Override
		protected boolean localEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			for (int x = 0; x < tags.length; ++x) {
				writer.writeEndElement();
			}
			return true;
		}

		@Override
		protected boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			for (String tag : tags) {
				writer.writeStartElement(tag);
			}
			boolean hasId = false;
			if (attributes != null) {
				for (Map.Entry<String, String> attr : attributes.entrySet()) {
					writer.writeAttribute(attr.getKey(), attr.getValue());
				}
				if (attributes.containsKey("id")) {
					hasId = true;
				}
			}
			if (!hasId) {
				String elementId = atts.getValue("id");
				if (elementId != null) {
					writer.writeAttribute("id", elementId);
				}
			}
			return true;
		}
	}

	private class NestingEmitter implements Emitter {

		private Emitter next = null;

		private String elementName = null;

		private int nestLevel = 0;

		public void content(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException {
			if (next != null) {
				next.content(writer, ch, start, length);
			} else {
				localContent(writer, ch, start, length);
			}
		}

		public final boolean end(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			--nestLevel;
			if (nestLevel > 0 && next != null) {
				if (next.end(writer, htmlElementName)) {
					next.close();
					next = null;
				}
				return false;
			} else {
				if (next != null) {
					next.close();
					next = null;
				}
				return localEnd(writer, htmlElementName);
			}
		}

		protected boolean localEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			return true;
		}

		protected boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			return true;
		}

		protected void localContent(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException {
			writer.writeCharacters(ch, start, length);
		}

		public final boolean start(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			++nestLevel;
			if (elementName == null) {
				elementName = htmlElementName;
				return localStart(writer, htmlElementName, atts);
			} else {
				if (next == null) {
					next = newEmitter(htmlElementName);
				}
				if (!next.start(writer, htmlElementName, atts)) {
					next.close();
					next = null;
					if (permitsMultipleNexting()) {
						next = newEmitter(htmlElementName);
						if (!next.start(writer, htmlElementName, atts)) {
							throw new IllegalStateException();
						}
						return true;
					} else {
						return false;
					}
				}
				return true;
			}
		}

		protected boolean permitsMultipleNexting() {
			return true;
		}

		public void close() throws XMLStreamException {
			if (next != null) {
				next.close();
				next = null;
			}
		}
	}

	private class BookEmitter extends SimpleMappingEmitter {
		private String bookTitle;

		private BookEmitter() {
			super("book");
		}

		@Override
		public boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			boolean ok = super.localStart(writer, htmlElementName, atts);
			if (ok && bookTitle != null) {
				writer.writeStartElement("title");
				writer.writeCharacters(bookTitle);
				writer.writeEndElement();
			}
			return ok;
		}

		@Override
		protected boolean localEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			if (!acronyms.isEmpty()) {
				writer.writeStartElement("appendix");
				writer.writeAttribute("id", "glossary");
				writer.writeStartElement("title");
				writer.writeAttribute("id", "glossary-end");
				writer.writeCharacters("Glossary");
				writer.writeEndElement(); // title
				writer.writeStartElement("glosslist");

				for (Map.Entry<String, String> glossEntry : new TreeMap<String, String>(acronyms).entrySet()) {

					writer.writeStartElement("glossentry");

					writer.writeStartElement("glossterm");
					writer.writeCharacters(glossEntry.getKey());
					writer.writeEndElement(); // glossterm

					writer.writeStartElement("glossdef");
					writer.writeStartElement("para");
					writer.writeCharacters(glossEntry.getValue());
					writer.writeEndElement(); // para
					writer.writeEndElement(); // glossdef

					writer.writeEndElement(); // glossentry
				}
				writer.writeEndElement(); // glosslist
				writer.writeEndElement(); // appendix
			}
			return super.localEnd(writer, htmlElementName);
		}

		public String getBookTitle() {
			return bookTitle;
		}

		public void setBookTitle(String bookTitle) {
			this.bookTitle = bookTitle;
		}

		@Override
		protected boolean permitsMultipleNexting() {
			return true;
		}
	}

	private class HeaderEmitter implements Emitter {

		private Pattern HEADER_ELEM_NAME_PATTERN = Pattern.compile("h(\\d)");

		private Emitter next;

		private int nestLevel = 0;

		private final int headerLevel;

		private String sectionName;

		private boolean dispatching = false;

		private boolean sectionOpen = false;

		public HeaderEmitter(int headerLevel) {
			super();
			this.headerLevel = headerLevel;
			if (headerLevel == 1) {
				sectionName = "chapter";
			} else {
				sectionName = "section";
			}
		}

		public void content(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException {
			if (!dispatching && nestLevel == 1) {
				writer.writeCharacters(ch, start, length);
			} else {
				dispatchContent(writer, ch, start, length);
			}
		}

		public boolean end(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			--nestLevel;
			if (!dispatching && nestLevel == 0) {
				closeTitle();
				dispatching = true;
			} else {
				dispatchEnd(writer, htmlElementName);
			}
			return false;
		}

		public boolean start(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			++nestLevel;
			if (!dispatching && nestLevel == 1) {
				openSection(atts);
			} else {
				Matcher matcher = HEADER_ELEM_NAME_PATTERN.matcher(htmlElementName);
				if (matcher.matches()) {
					int localLevel = Integer.parseInt(matcher.group(1));
					if (localLevel <= headerLevel) {
						closeSection();
						return false;
					}
				}
				dispatchStart(writer, htmlElementName, atts);
			}
			return true;
		}

		private void dispatchStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			if (next == null) {
				next = newEmitter(htmlElementName);
			}

			if (!next.start(writer, htmlElementName, atts)) {
				next.close();
				next = newEmitter(htmlElementName);
				if (!next.start(writer, htmlElementName, atts)) {
					throw new IllegalStateException();
				}
			}
		}

		private void dispatchEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			if (next != null) {
				if (next.end(writer, htmlElementName)) {
					next.close();
					next = null;
				}
			} else {
				throw new IllegalStateException();
			}
		}

		private void dispatchContent(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException {
			if (next != null) {
				next.content(writer, ch, start, length);
			} else {
				writer.writeCharacters(ch, start, length);
			}
		}

		private void openSection(Attributes atts) throws XMLStreamException {
			sectionOpen = true;
			writer.writeStartElement(sectionName);
			writer.writeStartElement("title");

			String elementId = atts.getValue("id");
			if (elementId != null) {
				writer.writeAttribute("id", elementId);
			}
		}

		private void closeSection() throws XMLStreamException {
			if (next != null) {
				next.close();
				next = null;
			}
			writer.writeEndElement(); // section
			next = null;
			dispatching = false;
			sectionOpen = false;
		}

		private void closeTitle() throws XMLStreamException {
			writer.writeEndElement(); // title
		}

		public void close() throws XMLStreamException {
			if (next != null) {
				next.close();
				next = null;
			}
			if (sectionOpen) {
				closeSection();
			}
		}

	}

	private class GlosstermEmitter extends SimpleMappingEmitter {

		private String acronymTitle;

		private StringBuilder acronym = new StringBuilder();

		public GlosstermEmitter() {
			super("glossterm");
		}

		@Override
		protected void localContent(XMLStreamWriter writer, char[] ch, int start, int length) throws XMLStreamException {
			if (length > 0) {
				acronym.append(ch, start, length);
			}
			super.localContent(writer, ch, start, length);
		}

		@Override
		public boolean localEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			if (acronym.length() > 0) {
				String acronym = this.acronym.toString().trim();
				if (acronym.length() > 0) {
					String previousTitle = acronyms.put(acronym, acronymTitle);
					if (previousTitle != null) {
						if (acronymTitle == null || previousTitle.length() > acronymTitle.length()) {
							acronyms.put(acronym, previousTitle);
						}
					}
				}
			}
			return super.localEnd(writer, htmlElementName);
		}

		@Override
		public boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			if (htmlElementName.equals("acronym")) {
				acronymTitle = atts.getValue("title");
			}
			return super.localStart(writer, htmlElementName, atts);
		}

	}

	private class AnchorEmitter extends NestingEmitter {

		private boolean openTag = false;

		public AnchorEmitter() {
		}

		@Override
		public boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			String href = null;
			String name = null;
			if (htmlElementName.equals("a")) {
				href = atts.getValue("href");
				name = atts.getValue("name");
			}
			if (name != null && name.length() > 0) {
				openTag = true;
				writer.writeStartElement("phrase");
				writer.writeAttribute("id", name);
			} else if (href != null) {
				if (href.startsWith("#")) {
					if (href.length() > 1) {
						openTag = true;
						writer.writeStartElement("link");
						writer.writeAttribute("linkend", href.substring(1));
					}
				} else {
					openTag = true;
					writer.writeStartElement("ulink");
					writer.writeAttribute("url", href);
				}
			}
			return true;
		}

		@Override
		protected boolean localEnd(XMLStreamWriter writer, String htmlElementName) throws XMLStreamException {
			closeTag(writer);
			return true;
		}

		private void closeTag(XMLStreamWriter writer) throws XMLStreamException {
			if (openTag) {
				writer.writeEndElement();
				openTag = false;
			}
		}

		@Override
		public void close() throws XMLStreamException {
			super.close();
			closeTag(writer);
		}

	}

	private class ImageEmitter extends SimpleMappingEmitter {

		public ImageEmitter() {
			super("mediaobject", "imageobject", "imagedata");
		}

		@Override
		public boolean localStart(XMLStreamWriter writer, String htmlElementName, Attributes atts) throws XMLStreamException {
			String src = null;
//			String width = null;
//			String height = null;
			if (htmlElementName.equals("img")) {
				src = atts.getValue("src");
			}
			boolean ok = super.localStart(writer, htmlElementName, atts);
			if (ok && src != null) {
				writer.writeAttribute("fileref", src);
//				if (width != null) {
//					writer.writeAttribute("contentwidth", width);
//					writer.writeAttribute("width", width);
//				}
//				if (height != null) {
//					writer.writeAttribute("contentdepth", width);
//					writer.writeAttribute("depth", width);
//				}
			}
			return ok;
		}

	}
}
