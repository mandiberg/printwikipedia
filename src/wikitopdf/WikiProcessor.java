package wikitopdf;

import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiSettings;
import wikitopdf.utils.WikiLogger;
import com.lowagie.text.DocumentException;
import java.io.IOException;
import java.io.File;
import java.io.FileFilter;
import java.sql.SQLException;
import java.util.*;
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
        int totalPageNum = 0;
        int cPageNum = 0;
        int artWritten = 0;
        int numArt = 0;
        int cVolNum = 1;
        Date newTime = new Date();
        Date oldTime;
        String tempName;
        String outputName = "";
        File oldFile;
        File newFile;
        
        //Added May 28 by CE for Graceful Restart
        /****************************************/
        FileFilter dsFilter = new FileFilter(){
            public boolean accept(File file){
                return (!(file.getName().contains("DS_Store")));
            }
        };
        
        File[] listOfFiles = new File("output").listFiles(dsFilter);
        
        if (listOfFiles.length > 0){
            WikiRestart restartSettings = new WikiRestart(listOfFiles);
            startLimit = restartSettings.getRestartLimit();
            totalPageNum = restartSettings.getRestartPage();
            cVolNum = restartSettings.getRestartVol();
        }
        System.out.println("Starting from Vol: " + cVolNum + " " + "Page: " + 
                totalPageNum + " " + "Article: " + startLimit);
        /****************************************/

        SQLProcessor sqlReader = null;
        PdfPageWrapper pdfWrapper = null;
        Runtime runtime = Runtime.getRuntime();

        //while (isInProggress) {

        try {
             //i want this to get built and broken down with each iteration to ensure no memory leaking in it.
            sqlReader = new SQLProcessor();
            int artCount = sqlReader.getArticlesCount();// Counts total from database
            sqlReader = null;

            while (isInProggress) {
                pdfWrapper = new PdfPageWrapper(startLimit); // Start with page ID indicated in settings
                tempName = "./output/" + pdfWrapper.getOutputFileName(); // Added Wednesday May 22 by CE For file rename
                sqlReader = new SQLProcessor();
                // While still pages in database and still writing pages to this volume 
                // This inner while loop creates a single pdf volume
                while (pdfWrapper.getPageNumb() < pdfPageLimit && isInProggress) { 
                    
                    // Get all article entries from the database
                    ArrayList<WikiPage> pages = sqlReader.getBunch(startLimit, pageBunch, 1);
                    //for (WikiPage page : pages) {
                      //  pdfWrapper.writePage(page);
                    //}
                    
                    // Added May 23 by CE to stop writing articles when page limit is reached
                    outputName = pages.get(0).getTitle();
                    Iterator<WikiPage> i = pages.iterator();
                    for(; i.hasNext() && pdfWrapper.getPageNumb() < pdfPageLimit; ) { 
                        WikiPage page = i.next();
                        pdfWrapper.writePage(page);
                        artWritten++;
                    }
                    outputName = outputName + "-" + pages.get(artWritten - 1).getTitle();

                    isInProggress = sqlReader.isInProggres(); // checks to see if there is still database entries
                    //TODO change from test value
                    //isInProggress = false;
                    if (isInProggress) {
                        //startLimit += pageBunch; //auto-incraments start based on a database query bunch
                        startLimit += artWritten; // Added May 23 by CE incraments by the number of articles written
                    }
                    
                    numArt = artWritten;
                    artWritten = 0; // Added May 23 by CE resets incramentor so that it doesn't skip articles next loop through
                    pages = null;
                    //System.out.println("starting PDF, page number: " + pdfWrapper.getPageNumb()); // the number that is being printed
                    //pdfWrapper.closeColumn();
                } // End of Volume
                
                
                cPageNum = pdfWrapper.getPageNumb();
                pdfWrapper.close();
                System.out.println("trying to read " + pdfWrapper.getOutputFileName() );               
                PdfStamp stamp = new PdfStamp();
                //stamp.stampDir(cPageNum);
                stamp.writeFooter(pdfWrapper.getOutputFileName(), totalPageNum++);
                totalPageNum += cPageNum;
                System.out.println("after stamping outputName is " + outputName);
                
                //Renaming Added May 24 by CE, renames outputfile
                tempName = tempName.replaceAll("_", "");
                outputName = "./output/Vol-" + String.format("%05d", cVolNum) + "-" + 
                        outputName + "-" + startLimit + "-" +  totalPageNum + ".pdf";
                //System.out.println("after stamping outputName is " + outputName +" and tempName is " + tempName );
                oldFile = new File(tempName);
                //String tempNameNoUnderscore = tempName.replace("_","");
                //File oldFileNoUnderscore = new File(tempName);
                newFile = new File(outputName);
                if(newFile.exists()){
                    newFile.delete();
                }
                if(!(oldFile.renameTo(newFile))){
                    System.out.println("File not renamed first time");
                    //if (!(oldFileNoUnderscore.renameTo(newFile))){
                      //  System.out.println("File not renamed second time, matching" + tempNameNoUnderscore);
                    //}
                }
             
                //Timing
                oldTime = newTime;
                newTime = new Date();
                //Print time for last volume in seconds, as well as the number of articles used to write it.
                System.out.println("Time for Vol_" + cVolNum + ": " + ((newTime.getTime() - oldTime.getTime()) / 1000)
                        + ", articles written: " + numArt);
                //Print out current time
                //System.out.println("Current Time: " + newTime.getTime());
                cVolNum++;

                //Info
                pageInfo = "([" + pdfWrapper.getCurrentArticleID() + "] " + 
                        pdfWrapper.getCurrentTitle() + ")";
                WikiLogger.getLogger().fine("Retrieved " + startLimit + "/" + artCount + " articles " + pageInfo);
                WikiLogger.getLogger().info("Free memory: " + ByteFormatter.format(runtime.freeMemory()));
                //here's some closing code at the end of each volume
                pdfWrapper = null;
                sqlReader = null;
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
