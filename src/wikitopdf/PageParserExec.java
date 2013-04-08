package wikitopdf;

import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiLogger;

/**
 * THIS IS THE MAIN.JAVA CLASS - AS IT IS REDEFINED ELSEWHERE TO RUN THIS FIRST
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PageParserExec {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try{
        // in 2013 rev, we have switched back to old unthreaded processor, which is working
        // class is defined in WikiProcessor.java
        WikiProcessor wikiProcessor = new WikiProcessor();
        //WikiHDProcessor wikiProcessor = new WikiHDProcessor();
        //WikiThreadingProcessor wikiProcessor = new WikiThreadingProcessor(); // creates new object from wikithreadingprocessor.java
        wikiProcessor.createPdf(); // calls createPdf method. does not complete this
         }catch(Exception ex){
            WikiLogger.getLogger().severe(ex.getMessage());
        }catch(Error th){
            WikiLogger.getLogger().severe(th.getMessage() + " memory:" + 
               ByteFormatter.format(Runtime.getRuntime().freeMemory()));
        }catch(Throwable ex0){
            WikiLogger.getLogger().severe(ex0.getMessage());
        }
    }
}
