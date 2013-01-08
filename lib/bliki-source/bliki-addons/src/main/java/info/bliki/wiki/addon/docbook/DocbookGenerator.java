package info.bliki.wiki.addon.docbook;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Create a <a href="http://en.wikipedia.org/wiki/DocBook">DocBook</a> document
 * from a wiki string
 * 
 */
public class DocbookGenerator {
	public final static String HEADER_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
			+ "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
			+ "<head>\n" + "        <title>{0}</title>\n   </head>\n" + "    <body>";

	public final static String FOOTER = "    </body>\n</html>";

	private HtmlToDocbookContentHandler htmlToDocbookContentHandler;

	public DocbookGenerator() {
		htmlToDocbookContentHandler = new HtmlToDocbookContentHandler();
	}

	/**
	 * Create a <a href="http://en.wikipedia.org/wiki/DocBook">DocBook</a> string
	 * from a given rendered XHTML text.
	 * 
	 * @param xhtmlContent
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 * @throws XMLStreamException
	 * @throws SAXException
	 */
	public String create(String xhtmlContent,String headerTemplate, String footer, String titleText) throws IOException, ParserConfigurationException, XMLStreamException, SAXException {
		Object[] objects = new Object[2];
		objects[0] = titleText;
		String header = MessageFormat.format(HEADER_TEMPLATE, objects);

		StringBuffer buffer = new StringBuffer();
		buffer.append(header);
		buffer.append(xhtmlContent);
		buffer.append(footer);

		InputSource inputSource = new InputSource(new StringReader(buffer.toString()));

		SAXParserFactory factory = SAXParserFactory.newInstance();

		factory.setNamespaceAware(true);
		factory.setValidating(false);
		SAXParser saxParser;

		saxParser = factory.newSAXParser();

		XMLReader parser = saxParser.getXMLReader();
		parser.setEntityResolver(IgnoreDtdEntityResolver.getInstance());

		StringWriter out = new StringWriter();
		XMLStreamWriter writer = createXMLStreamWriter(out);

		htmlToDocbookContentHandler.setWriter(writer);

		parser.setContentHandler(htmlToDocbookContentHandler);
		parser.parse(inputSource);

		writer.close();

		return out.toString();

	}

	protected XMLStreamWriter createXMLStreamWriter(Writer out) {
		XMLStreamWriter writer;
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
		} catch (XMLStreamException e1) {
			throw new IllegalStateException(e1);
		} catch (FactoryConfigurationError e1) {
			throw new IllegalStateException(e1);
		}
		return new FormattingXMLStreamWriter(writer);
	}
}
