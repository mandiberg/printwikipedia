package wikitopdf;

import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiSettings;
import wikitopdf.utils.WikiLogger;
import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wikitopdf.pdf.PdfPageWrapper;
import wikitopdf.pdf.PdfStamp;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiProcessor {

    /**
     *
     */
    public void createPdf() {
        int pageBunch = WikiSettings.getInstance().getArticleBunch();
        int pdfPageLimit = WikiSettings.getInstance().getPageLimit();
        String pageInfo = "";
        int startLimit = WikiSettings.getInstance().getStartPage();
        boolean isInProggress = true;
        int cPageNum = 0;

        SQLProcessor sqlReader = null;
        PdfPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {

            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount();


            while (isInProggress) {

                pdfWrapper = new PdfPageWrapper(startLimit);

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

                    System.out.println(pdfWrapper.getPageNumb());
                    //pdfWrapper.closeColumn();
                }

                cPageNum = pdfWrapper.getPageNumb();
                pdfWrapper.close();
                

                //Info
                pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] " +
                        pdfWrapper.getCurrentTitle() + ")";
                WikiLogger.getLogger().fine("Retrieved " + startLimit + "/" + artCount + " articles " + pageInfo);
                WikiLogger.getLogger().info("Free memory: " + ByteFormatter.format(runtime.freeMemory()));
            }

            //Stamping all page numbers
            PdfStamp stamp = new PdfStamp();
            WikiLogger.getLogger().info("Stamping page numbers...");
            stamp.stampDir(cPageNum);
            WikiLogger.getLogger().fine("Stamping done");

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(ex.getMessage() + pageInfo);
        } finally {
            sqlReader.close();
        }

        WikiLogger.getLogger().fine("Finished (" + startLimit + " pages)");
    }
}
