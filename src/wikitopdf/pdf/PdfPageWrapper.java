package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import wikitopdf.utils.WikiLogger;
import wikitopdf.utils.WikiSettings;
import wikitopdf.html.WHTMLWorker;
import wikitopdf.html.WikiHtmlConverter;
import wikitopdf.html.WikiStyles;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PdfPageWrapper {

    /**
     *
     * @param index
     * @throws DocumentException
     * @throws IOException
     */
    public PdfPageWrapper(int index) throws DocumentException, IOException {
        //Read settings.
        //'_' - prefix for for temp file. After stamping file would be renamed
        outputFileName = "_" + index + WikiSettings.getInstance().getOutputFileName();

        //72 pixels per inch
        pdfDocument = new Document(new Rectangle(432, 648));//6" x 9"
        //pdfDocument = new Document(new Rectangle(1918, 1018)); //

        pdfDocument.setMargins(27, 67.5f, -551, 49.5f);

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream( WikiSettings.getInstance().getOutputFolder() +
                            "/" + outputFileName));

        header = new PageHeaderEvent(0);
        pdfWriter.setPageEvent(header);

        pdfDocument.open();
        pdfDocument.setMarginMirroring(true);
        _wikiFontSelector = new WikiFontSelector();

        //PdfContentByte cb = pdfWriter.getDirectContent();
        //ColumnText ct = new ColumnText(cb);
        openMultiColumn();
    }

    /**
     *
     * @param page
     */
    public void writePage(WikiPage page) {
        currentTitle = page.getTitle();
        currentArticleID = page.getId();
        //System.out.println("Article ID is" + currentArticleID);
        writeTitle(currentTitle);
        writeText(page.getRevision().getText());
    }

    //Write title of article to document
    private void writeTitle(String line) {
        Phrase ph;
        try {
            line = line.replaceAll("_", " ").toUpperCase();
            header.setCurrentTitle(line);
            ph = _wikiFontSelector.getTitleFontSelector().process(line);
            ph.setLeading(10);
            Paragraph pr = new Paragraph(ph);
            //pr.setSpacingBefore(20);
            pr.setSpacingAfter(9);

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }
            if (pdfWriter.getCurrentPageNumber() > 1) {
                //Double paragraph helvetica problem is here other is in WikiHtmlConverter.java
                mct.addElement(new Phrase("\n"));
            }

            mct.addElement(pr);
            pdfDocument.add(mct);
        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
        }
    }

    //Write article text using defined styles
    private void writeText(String text) {
        try {
            // text is in BBCode (This is bliki)
            String html = WikiHtmlConverter.convertToHtml(text);
            // text is now html (This is doing iText work)
            convertHtml2Pdf(html);
            // text has been made into pdf.

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
        }
    }

    //Convert html to pdf objects, apply styles
    private void convertHtml2Pdf(String htmlSource) throws DocumentException, IOException {
        StringReader reader = new StringReader(htmlSource);
        StyleSheet styles = WikiStyles.getStyles();

        ArrayList objects;
        objects = WHTMLWorker.parseToList(reader, styles);

        for (int k = 0; k < objects.size(); ++k) {

            Element element = (Element) objects.get(k);

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }

            mct.addElement(element);
            pdfDocument.add(mct);
        }
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(600);
        int columnCount = 3;
        float gap = (float) 27;
        float space = (float) 8;
        float columnWidth = (float) 107;
        float left = gap;
        float right = left + columnWidth;

        for (int i = 0; i < columnCount; i++) {
            //System.out.println("left:" + left + " right:" + right);
            mct.addSimpleColumn(left, right);
            left = right + space;
            right = left + columnWidth;
        }

        //First page hack
        for (int i = 0; i < 38; i++) {
            try {
                Phrase ph = _wikiFontSelector.getTitleFontSelector().process("\n");

                mct.addElement(ph);
                pdfDocument.add(mct);
            } catch (Exception ex) {
                WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
            }
        }
    }

    /**
     *
     */
    public void closeColumn() {
        try {
            pdfDocument.add(mct);
        } catch (DocumentException ex) {
            WikiLogger.getLogger().severe(ex.getMessage());
            //throw new Exception(ex);
        }
    }

    /**
     *
     * @return
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     *
     * @return
     */
    public int getPageNumb() {
        return pdfWriter.getCurrentPageNumber() - 1;
    }

    /**
     *
     * @return
     */
    public String getCurrentTitle(){
        return currentTitle;
    }

    /**
     *
     */
    public void close() {
        pdfDocument.close();
    }

    /**
     *
     * @return
     */
    public int getCurrentArticleID() {
        return currentArticleID;
    }

    private PageHeaderEvent header = null;
    private Document pdfDocument = null;
    private PdfWriter pdfWriter;
    private WikiFontSelector _wikiFontSelector = null;
    private MultiColumnText mct = null;
    private String outputFileName = "";
    private String currentTitle = "";
    private int currentArticleID;
}
