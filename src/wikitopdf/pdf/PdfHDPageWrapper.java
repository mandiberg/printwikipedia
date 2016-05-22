/*
 */

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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class PdfHDPageWrapper {

    /**
     *
     * @param index
     * @throws DocumentException
     * @throws IOException
     */
    public PdfHDPageWrapper(int index) throws DocumentException, IOException {
        //Read settings.
        //'_' - prefix for for temp file. After stamping file would be renamed
        outputFileName = "_" + index + WikiSettings.getInstance().getOutputFileName();

        //72 pixels per inch
        //pdfDocument = new Document(new Rectangle(432, 648));//6" x 9"
        pdfDocument = new Document(new Rectangle(1918, 1018)); //

        //pdfDocument.setMargins(32, 32, 0, 32);
        pdfDocument.setMargins(32, 32, -565, 32);

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream( WikiSettings.getInstance().getOutputFolder() +
                            "/" + outputFileName));

        //header = new PageHeaderEvent(0);
        //pdfWriter.setPageEvent(header);

        pdfDocument.open();
        //pdfDocument.setMarginMirroring(true);
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
        writeTitle(currentTitle);
        writeText(page.getRevision().getText());
    }

    //Write title of article to document
    private void writeTitle(String line) {
        Phrase ph;
        try {
            line = line.replaceAll("_", " ").toUpperCase();
            //header.setCurrentTitle(line);
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
            String html = WikiHtmlConverter.convertToHtml(text);
            convertHtml2Pdf(html);

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
        }
    }

    //Convert html to pdf objects, apply styles
    private void convertHtml2Pdf(String htmlSource) throws DocumentException, IOException {
        StringReader reader = new StringReader(htmlSource);
        StyleSheet styles = WikiStyles.getHDStyles();

        ArrayList objects;
        objects = WHTMLWorker.parseToList(reader, styles,pdfWriter);

        for (int k = 0; k < objects.size(); ++k) {
            Element element = (Element) objects.get(k);

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
                //System.out.println("Overflow");
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
        int columnCount = 5;
        float leftGap = (float) 36;
        float space = (float) 21.8f;
        float columnWidth = (float) 352;
        float left = leftGap;
        float right = left + columnWidth;

        //mct.addRegularColumns(pdfDocument.left(),
        //        pdfDocument.right(), 10, 5);


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
//        try {
//            pdfDocument.add(mct);
//        } catch (DocumentException ex) {
//            Logger.getLogger(PdfHDPageWrapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
        pdfDocument.close();
    }

    /**
     *
     * @return
     */
    public int getCurrentArticleID() {
        return currentArticleID;
    }

    //private PageHeaderEvent header = null;
    private Document pdfDocument = null;
    private PdfWriter pdfWriter;
    private WikiFontSelector _wikiFontSelector = null;
    private MultiColumnText mct = null;
    private String outputFileName = "";
    private String currentTitle = "";
    private int currentArticleID;

}
