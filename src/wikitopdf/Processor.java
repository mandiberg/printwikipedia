
/*
 */

package wikitopdf;

import java.util.ArrayList;
import wikitopdf.pdf.Pdf2ImageConverter;
import wikitopdf.pdf.PdfHDPageWrapper;
import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiLogger;
import wikitopdf.utils.WikiSettings;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */

public class Processor implements Runnable {


    private int pageBunch = WikiSettings.getInstance().getArticleBunch();
    private int pdfPageLimit = WikiSettings.getInstance().getPageLimit();
    private String pageInfo = "";
    private int startPage = 0;
    private int currentPage;
    private SQLProcessor sqlReader = null;
    private boolean isInProggress = true;
    private Runtime runtime = Runtime.getRuntime();

    @SuppressWarnings("static-access")
    public void run() {
        try {

            PdfHDPageWrapper pdfWrapper = new PdfHDPageWrapper(currentPage);

                //while (pdfWrapper.getPageNumb() < pdfPageLimit && isInProggress) {

                    ArrayList<WikiPage> pages = sqlReader.getBunch(currentPage, pageBunch, 1);
                    for (WikiPage page : pages) {
                        pdfWrapper.writePage(page);
                    }

                    //cPageNum +=

                    isInProggress = sqlReader.isInProggres();
                    //TODO change from test value
                    //isInProggress = false;
                    if (isInProggress) {
                        currentPage += pageBunch;
                    }

                    //System.out.println(pdfWrapper.getPageNumb());
                    //pdfWrapper.closeColumn();
                //}

                //cPageNum = pdfWrapper.getPageNumb();
                pdfWrapper.close();

                //Convert pdf to images
                Pdf2ImageConverter converter = new Pdf2ImageConverter();
                converter.convert(pdfWrapper.getOutputFileName());

                //Info
                pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] " +
                        pdfWrapper.getCurrentTitle() + ")";
                WikiLogger.getLogger().fine(Thread.currentThread().getName() + " Retrieved " + startPage + "-" + currentPage  + " articles " + pageInfo);
                WikiLogger.getLogger().info("Free memory: " + ByteFormatter.format(runtime.freeMemory()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    public Processor(int currentPage, SQLProcessor sqlReader) {
        this.startPage = this.currentPage = currentPage;
        this.sqlReader = sqlReader;
        //System.out.println(Thread.currentThread().getName() + " start " + start);

    }
}

