/*
 */

package wikitopdf;

import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiSettings;
import wikitopdf.utils.WikiLogger;
import java.util.ArrayList;
import wikitopdf.pdf.Pdf2ImageConverter;
import wikitopdf.pdf.PdfHDPageWrapper;
import wikitopdf.pdf.PdfPageWrapper;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiHDProcessor {
    /**
     *
     */
    public void createPdf() {
        int pageBunch = WikiSettings.getInstance().getArticleBunch();
        int pdfPageLimit = WikiSettings.getInstance().getPageLimit();
        String pageInfo = "";
        int startLimit = WikiSettings.getInstance().getStartPage();
        boolean isInProggress = true;
        int cPageNum = 4500;

        SQLProcessor sqlReader = null;
        PdfHDPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {
            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount();

            while (isInProggress) {
                //pdfWrapper = new PdfPageWrapper(startLimit);
                pdfWrapper = new PdfHDPageWrapper(startLimit);


                while (pdfWrapper.getPageNumb() < pdfPageLimit && isInProggress) {

                    ArrayList<WikiPage> pages = sqlReader.getBunch(startLimit, pageBunch, 1);
                    for (WikiPage page : pages) {
                        pdfWrapper.writePage(page);
                    }

                    //cPageNum +=

                    isInProggress = sqlReader.isInProggres();
                    //TODO change from test value
                    //isInProggress = false;
                    if (isInProggress) {
                        startLimit += pageBunch;
                    }

                    //System.out.println(pdfWrapper.getPageNumb());
                    //pdfWrapper.closeColumn();
                }

                cPageNum = pdfWrapper.getPageNumb();
                pdfWrapper.close();

                //Convert pdf to images
                Pdf2ImageConverter converter = new Pdf2ImageConverter();
                converter.convert(pdfWrapper.getOutputFileName());
                
                //Info
                pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] " +
                        pdfWrapper.getCurrentTitle() + ")";
                WikiLogger.getLogger().fine("Retrieved " + startLimit + "/" + artCount + " articles " + pageInfo);
                WikiLogger.getLogger().info("Free memory: " + ByteFormatter.format(runtime.freeMemory()));
            }

            //Stamping all page numbers

            //PdfStamp stamp = new PdfStamp();
            //WikiLogger.getLogger().info("Stamping page numbers...");
            //stamp.stampDir(cPageNum);
            //WikiLogger.getLogger().fine("Stamping done");

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(ex.getMessage() + pageInfo);
        } finally {
            sqlReader.close();
        }

        WikiLogger.getLogger().fine("Finished (" + startLimit + " pages)");
    }
}
