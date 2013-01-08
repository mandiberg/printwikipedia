package info.bliki.wiki.dump;

import info.bliki.api.Connector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A Wikipedia XML dump file parser
 * 
 * Original version with permission from Marco Schmidt. See: <a
 * href="http://schmidt.devlib.org/software/lucene-wikipedia.html">http://schmidt.devlib.org/software/lucene-wikipedia.html</a>
 *  
 * @author Marco Schmidt
 * 
 */
public class WikiXMLParser extends DefaultHandler {
	private static final String WIKIPEDIA_TITLE = "title";

	private static final String WIKIPEDIA_TEXT = "text";

	private static final String WIKIPEDIA_PAGE = "page";

	private static final String WIKIPEDIA_NAMESPACE = "namespace";

	private static final String WIKIPEDIA_TIMESTAMP = "timestamp";

	private WikiArticle fArticle;

	// private Attributes fAttributes;

	private StringBuffer fData;

	private XMLReader fXMLReader;

	private Reader fReader;

	private IArticleFilter fArticleFilter;

	public WikiXMLParser(String filename, IArticleFilter filter) throws SAXException, FileNotFoundException {
		this(new FileInputStream(filename), filter);
	}

	public WikiXMLParser(InputStream inputStream, IArticleFilter filter) throws SAXException {
		super();
		try {
			fArticleFilter = filter;
			fXMLReader = XMLReaderFactory.createXMLReader();
			fXMLReader.setContentHandler(this);
			fXMLReader.setErrorHandler(this);
			fReader = new InputStreamReader(inputStream, Connector.UTF8_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getString() {
		if (fData == null) {
			return null;
		} else {
			String s = fData.toString();
			fData = null;
			return s;
		}
	}

	@Override
	public void startDocument() {
		// System.out.println("START");
	}

	@Override
	public void endDocument() {
		// System.out.println("END");
	}

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
		// fAttributes = atts;

		if (WIKIPEDIA_PAGE.equals(qName)) {
			fArticle = new WikiArticle();
		}
		fData = null;
	}

	@Override
	public void endElement(String uri, String name, String qName) {
		try {
			if (WIKIPEDIA_PAGE.equals(qName)) {
				if (fArticle != null) {
				}
			} else if (WIKIPEDIA_TEXT.equals(qName)) {
				fArticle.setText(getString());
				fArticleFilter.process(fArticle);
				// emit(wikiText);
			} else if (WIKIPEDIA_TITLE.equals(qName)) {
				fArticle.setTitle(getString());
			} else if (WIKIPEDIA_TIMESTAMP.equals(qName)) {
				fArticle.setTimeStamp(getString());
			}

			fData = null;
			// fAttributes = null;

		} catch (RuntimeException re) {
			re.printStackTrace();
		}
	}

	/**
	 * parse an unlimited amount of characters between 2 enclosing XML-Tags
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (fData == null) {
			fData = new StringBuffer(length);
		}
		fData.append(ch, start, length);
	}

	public void parse() throws IOException, SAXException {
		fXMLReader.parse(new InputSource(fReader));
	}

}
