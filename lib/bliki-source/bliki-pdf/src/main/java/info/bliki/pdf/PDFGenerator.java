package info.bliki.pdf;

import info.bliki.api.Connector;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lowagie.text.DocumentException;

/**
 * Create a PDF document from a given rendered XHTML text.
 * 
 * Uses the <a href="https://xhtmlrenderer.dev.java.net/">Flying Saucer Project</a>
 * and <a href="http://en.wikipedia.org/wiki/IText">iText</a> Open Source
 * libraries for creating and manipulating PDF.
 * 
 */
public class PDFGenerator {
	public final static String HEADER_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
			+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n"
			+ "   \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
			+ "<head>\n" + "        <title>{0}</title>\n{1}\n    </head>\n" + "    <body>";

	public final static String FOOTER = "    </body>\n</html>";

	public final String fURLExternalForm;

	public PDFGenerator(URL url) {
		fURLExternalForm = url.toExternalForm();
	}

	/**
	 * Create a PDF document from a given rendered XHTML text.
	 * 
	 * @param outputFileName
	 *          the PDF output file name (example: c:/temp/test.pdf)
	 * @param xhtmlText
	 * @param headerTemplate
	 *          where <code>titleText</code> and <code>cssStyleText</code>
	 *          could be inserted (see <code>PDFGenerator.HEADER_TEMPLATE</code>
	 *          as an example / use <code>MessageFormat#format()</code> format)
	 * @param titleText
	 *          the title of the rendered wiki text
	 * @param cssStyleText
	 *          CSS style text for manipulating the PDF rendering
	 * @throws IOException
	 * @throws DocumentException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 */
	public void create(String outputFileName, String xhtmlText, String headerTemplate, String footer, String titleText,
			String cssStyleText) throws IOException, DocumentException, ParserConfigurationException, FactoryConfigurationError,
			SAXException {
		Object[] objects = new Object[2];
		objects[0] = titleText;
		objects[1] = cssStyleText;
		String header = MessageFormat.format(headerTemplate, objects);

		StringBuffer buffer = new StringBuffer();
		buffer.append(header);
		buffer.append(xhtmlText);
		buffer.append(footer);

		StringReader contentReader = new StringReader(buffer.toString());
		InputSource source = new InputSource(contentReader);
		source.setEncoding(Connector.UTF8_CHARSET);
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
		Document xhtmlContent = domBuilder.parse(source);
		contentReader.close();

		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(xhtmlContent, fURLExternalForm);
		renderer.layout();

		OutputStream os = new FileOutputStream(outputFileName);
		renderer.createPDF(os);
		os.close();
	}
}
