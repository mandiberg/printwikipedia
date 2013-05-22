package wikitopdf;

import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiSettings;
import wikitopdf.utils.WikiLogger;
import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
        int cVolNum = 1;
        Date newTime = new Date();
        Date oldTime;
        //String tempName = "";
        //String outputName = "";
        //File oldName;
        //File newName;

        SQLProcessor sqlReader = null;
        PdfPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {

            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount();// Counts total from database


            while (isInProggress) {

                pdfWrapper = new PdfPageWrapper(startLimit); // Start with page ID indicated in settings
                //tempName = pdfWrapper.getOutputFileName(); // Added Wednesday May 22 by CE For file rename
                
                // While still pages in database and still writing pages to this volume 
                // This inner while loop creates a single pdf volume
                while (pdfWrapper.getPageNumb() < pdfPageLimit && isInProggress) { 
                    
                    // Get all article entries from the database
                    ArrayList<WikiPage> pages = sqlReader.getBunch(startLimit, pageBunch, 1); 
                    for (WikiPage page : pages) {
                        pdfWrapper.writePage(page);
                    }

                    //cPageNum +=

                    isInProggress = sqlReader.isInProggres(); // checks to see if there is still database entries
                    //TODO change from test value
                    //isInProggress = false;
                    if (isInProggress) {
                        startLimit += pageBunch; //auto-incraments start based on a database query bunch  
                    }

                    System.out.println("sarting PDF, page number" + pdfWrapper.getPageNumb()); // the number that is being printed
                    //pdfWrapper.closeColumn();
                } // End of Volume

                cPageNum = pdfWrapper.getPageNumb();
                pdfWrapper.close();
                
                //Renaming Added Wendesday May 22 by CE
                //oldName = new File(tempName);
                //newName = new File(outputName);
                
                //Time
                oldTime = newTime;
                newTime = new Date();
                //Print time for last volume in seconds
                System.out.println("Time for vol" + cVolNum + ": " + (newTime.getTime() - oldTime.getTime())/1000);
                //Print out current time
                System.out.println("Current Time: " + newTime.getTime());
                cVolNum++;

                //Info
                pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] " +
                        pdfWrapper.getCurrentTitle() + ")";
                WikiLogger.getLogger().fine("Retrieved " + startLimit + "/" + artCount + " articles " + pageInfo);
                WikiLogger.getLogger().info("Free memory: " + ByteFormatter.format(runtime.freeMemory()));
            } //End of all volumes/PDFs

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



/*
 * THIS IS RAM'S CODE FROM THE AKKA BRANCH
 * IS THIS A GENERAL PURPOSE WIKIPROCESSOR?
 * OR IS IT SPECIFICALLY REWRITTEN FOR THE AKKA BRANCH?
 * IF AKKA BRANCH, IT SHOULD BE GIVEN A NEW NAME
 * E.G. AkkaWikiProcessor 
 * SO THE TWO CAN COEXIST. 

public class WikiProcessor {



public int createPdf(int startLimit) {
String pageInfo = "";
int cPageNum = 0;
int pageBunch = WikiSettings.getInstance().getArticleBunch();
SQLProcessor sqlReader = null;
int pdfPageLimit = WikiSettings.getInstance().getPageLimit();
Runtime runtime = Runtime.getRuntime();
int artCount = 0;

try {
PdfPageWrapper pdfWrapper = new PdfPageWrapper(startLimit);
sqlReader = new SQLProcessor();
artCount = sqlReader.getArticlesCount();
while (pdfWrapper.getPageNumb() < pdfPageLimit) {
ArrayList<WikiPage> pages = sqlReader.getBunch(startLimit,
pageBunch, 1);
for (WikiPage page : pages) {
pdfWrapper.writePage(page);
}
startLimit += pageBunch;
}

cPageNum = pdfWrapper.getPageNumb();
pdfWrapper.close();
pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] "
+ pdfWrapper.getCurrentTitle() + ")";
WikiLogger.getLogger().fine(
"Retrieved " + startLimit + "/" + artCount + " articles "
+ pageInfo);
WikiLogger.getLogger().info(
"Free memory: "
+ ByteFormatter.format(runtime.freeMemory()));
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
return artCount;
}

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

try {

sqlReader = new SQLProcessor();
int artCount = sqlReader.getArticlesCount();

while (isInProggress) {

pdfWrapper = new PdfPageWrapper(startLimit);

while (pdfWrapper.getPageNumb() < pdfPageLimit && isInProggress) {

ArrayList<WikiPage> pages = sqlReader.getBunch(startLimit,
pageBunch, 1);
for (WikiPage page : pages) {
pdfWrapper.writePage(page);
}

// cPageNum +=

isInProggress = sqlReader.isInProggres();
// TODO change from test value
// isInProggress = false;
if (isInProggress) {
startLimit += pageBunch;

}

System.out.println(pdfWrapper.getPageNumb());
// pdfWrapper.closeColumn();
}

cPageNum = pdfWrapper.getPageNumb();
pdfWrapper.close();

// Info
pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] "
+ pdfWrapper.getCurrentTitle() + ")";
WikiLogger.getLogger().fine(
"Retrieved " + startLimit + "/" + artCount
+ " articles " + pageInfo);
WikiLogger.getLogger().info(
"Free memory: "
+ ByteFormatter.format(runtime.freeMemory()));
}

// Stamping all page numbers
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



* 
 */
