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
import wikitopdf.utils.ThreadPool;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiThreadingProcessor {
    /**
     *
     */
    public void createPdf() {
        int pageBunch = WikiSettings.getInstance().getArticleBunch();
        int pdfPageLimit = WikiSettings.getInstance().getPageLimit();
        String pageInfo = "";
        int startLimit = WikiSettings.getInstance().getStartPage();
        int numThreads = WikiSettings.getInstance().getThreadLimit();
        boolean isInProggress = true;

        SQLProcessor sqlReader = null;
        PdfHDPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {
            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount();
            ThreadPool threadPool = new ThreadPool(numThreads);


            while (startLimit < artCount) {
                threadPool.runTask(new Processor(startLimit, sqlReader));
                startLimit += pageBunch;

                WikiLogger.getLogger().info("Start bunch: " + startLimit);
            }

             threadPool.join();

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
