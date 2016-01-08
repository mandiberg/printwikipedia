package info.bliki.api;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Base class for reading XML strings.
 */
public abstract class AbstractXMLParser extends DefaultHandler {
    protected static final String TITLE_ID = "title";
    protected static final String NS_ID = "ns";
    protected static final String PAGE_ID = "pageid";
    protected static final String PAGE_TAG2 = "p";
    protected static final String PAGE_TAG1 = "page";

    protected Attributes fAttributes;
    protected StringBuffer fData;
    protected XMLReader fXMLReader;
    protected Reader fReader;

    public AbstractXMLParser(String xmlText) throws SAXException {
        super();
        fXMLReader = XMLReaderFactory.createXMLReader();
        fXMLReader.setContentHandler(this);
        fXMLReader.setErrorHandler(this);
        fReader = new StringReader(xmlText);
    }

    protected String getString() {
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
        InputSource inputSource = new InputSource(fReader);
        inputSource.setEncoding("UTF-8");
        fXMLReader.parse(inputSource);
    }
}
