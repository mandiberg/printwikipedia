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
        int startLimit = WikiSettings.getInstance().getStartPage();  // why is startLimit not 0, as defined in settings file 
            // we assume it isn't zero b/c it starts from 41000
        int numThreads = WikiSettings.getInstance().getThreadLimit();
        boolean isInProggress = true;

        SQLProcessor sqlReader = null;  // from SQLProcessor.java
        PdfHDPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {
            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount(); // getting total articles in db
            ThreadPool threadPool = new ThreadPool(numThreads);


            while (startLimit < artCount) { 
                threadPool.runTask(new Processor(startLimit, sqlReader));  // the action that starts the threads 
                startLimit += pageBunch;
//we get to here, and then it hangs in the middle(?)-- not true
                WikiLogger.getLogger().info("Start bunch: " + startLimit);
            }

             threadPool.join();
             
                WikiLogger.getLogger().info("Bunches joined");

            //Stamping all page numbers

            //PdfStamp stamp = new PdfStamp();
            //WikiLogger.getLogger().info("Stamping page numbers...");
            //stamp.stampDir(cPageNum);
            //WikiLogger.getLogger().fine("Stamping done");

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(ex.getMessage() + pageInfo);
        } finally {
            sqlReader.close();
                            WikiLogger.getLogger().info(" in finally");

        }
       WikiLogger.getLogger().info("Finished (" + startLimit + " pages)");

  //      WikiLogger.getLogger().fine("Finished (" + startLimit + " pages)");
    }
}
