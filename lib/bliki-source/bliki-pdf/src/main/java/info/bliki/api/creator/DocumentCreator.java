package info.bliki.api.creator;

import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.pdf.PDFGenerator;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.PDFConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.List;

/**
 * <p>
 * Create an HTML or PDF file from a Mediawiki wiki which supports the
 * <code>api.php</code> interface from <a
 * href="http://en.wikipedia.org/w/api.php"
 * >http://en.wikipedia.org/w/api.php</a>.
 * </p>
 * 
 */
public class DocumentCreator {
	private final String[] fListOfTitleStrings;

	private final User fUser;

	private final IWikiModel fModel;

	private List<Page> fListOfPages = null;

	private String fHeader;

	private String fFooter;

	public DocumentCreator(IWikiModel model, User user,
			String[] listOfTitleStrings) {
		fListOfTitleStrings = listOfTitleStrings;
		fUser = user;
		fModel = model;
		fHeader = null;
		fFooter = null;
	}

	public void readPages() {
		fUser.login();
		fListOfPages = fUser.queryContent(fListOfTitleStrings);
	}

	/**
	 * Render the given Wikipedia texts into a string for a given converter
	 * 
	 * @param converter
	 *            a text converter. <b>Note</b> the converter may be
	 *            <code>null</code>, if you only would like to analyze the raw
	 *            wiki text and don't need to convert. This speeds up the
	 *            parsing process.
	 * @return <code>null</code> if an IOException occurs or
	 *         <code>converter==null</code>
	 * @return
	 */
	public void render(ITextConverter converter, Appendable appendable)
			throws IOException {
		if (fListOfPages == null) {
			readPages();
		}
		if (fListOfPages != null) {
			if (fHeader != null) {
				appendable.append(fHeader);
			}
			for (Page page : fListOfPages) {
				// print page information
				String rawWikiText = page.getCurrentContent();
				fModel.setPageName(page.getTitle());
				// System.out.println(rawWikiText);
				appendable.append(fModel.render(converter, rawWikiText));
			}
			if (fFooter != null) {
				appendable.append(fFooter);
			}
		}
	}

	/**
	 * Render the given Wikipedia texts into an HTML string and use the default
	 * HTMLConverter.
	 * 
	 */
	public void render(Appendable appendable) throws IOException {
		render(new HTMLConverter(), appendable);
	}

	/**
	 * Render the given Wikipedia texts into an HTML string and use the default
	 * PDFConverter. The resulting XHTML could be used as input for the Flying
	 * Saucer PDF renderer
	 * 
	 */
	public void renderPDF(Appendable appendable) throws IOException {
		render(new PDFConverter(), appendable);
	}

	/**
	 * Render the given Wikipedia texts into an HTML file for the given
	 * converter.
	 * 
	 */
	public void renderToFile(ITextConverter converter, String filename)
			throws IOException {
		File file = new File(filename);
		Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		try {
			render(converter, fw);
		} finally {
            fw.close();
		}
	}

	/**
	 * Render the given Wikipedia texts into an HTML file.
	 * 
	 */
	public void renderToFile(String filename) throws IOException {
		renderToFile(new HTMLConverter(), filename);
	}

	/**
	 * Render the given Wikipedia texts into a PDF file.
	 * 
	 * @param baseDirectoryName
	 *            the base directory, where all files should be stored
	 * @param filename
	 *            the filename relative to the baseDirectory
	 * @param cssStyle
	 *            CSS styles which should be used for rendering the PDF file
	 * @throws IOException
	 */
	public void renderPDFToFile(String baseDirectoryName, String filename,
			String cssStyle) throws IOException {
		StringBuffer buffer = new StringBuffer();
		renderPDF(buffer);
		String renderedXHTML = buffer.toString();
		// System.out.println(renderedXHTML);
		File baseDirectory = new File(baseDirectoryName);
		try {
			URL url = baseDirectory.toURI().toURL();
			PDFGenerator gen = new PDFGenerator(url);
			gen.create(baseDirectoryName + '/' + filename, renderedXHTML,
					PDFGenerator.HEADER_TEMPLATE, PDFGenerator.FOOTER,
					"Big Test", cssStyle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the HTML header of this creator.
	 * 
	 * @return <code>null</code> if no HTML header is set
	 */
	public String getHeader() {
		return fHeader;
	}

	/**
	 * Set the HTML header set of this creator.
	 * 
	 */
	public void setHeader(String header) {
		this.fHeader = header;
	}

	/**
	 * Get the HTML footer of this creator.
	 * 
	 * @return <code>null</code> if no HTML footer is set
	 */
	public String getFooter() {
		return fFooter;
	}

	/**
	 * Set the HTML footer of this creator.
	 * 
	 */
	public void setFooter(String footer) {
		this.fFooter = footer;
	}
}
