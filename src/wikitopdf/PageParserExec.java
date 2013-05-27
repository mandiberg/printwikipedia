package wikitopdf;

import wikiactorprocessor.ActorProcessor; 
import wikitopdf.pdf.PdfStamp;
import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiLogger;

/**
 * THIS IS THE MAIN.JAVA CLASS
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PageParserExec {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int length = args.length;
        String wikiProcess = ""; 
        //String isThreaded = "threaded";
        if (length <= 0) {
            System.out.println("Default is pdf. Options are: pdf, threaded, toc, pagenumbers, covers. e.g.: java -jar \"wikitopdf.jar\" pdf");
            wikiProcess = "pdf";
        } else {
            // setting the process from the command line args
            wikiProcess = args[0]; 
        }
        
        try{

            //based on the wikiProcess, does one of several processes
                if(wikiProcess.equals("threaded")){
                    System.out.println("Running " + wikiProcess);
                    ActorProcessor actor = new ActorProcessor();
                    actor.act();
                } else if(wikiProcess.equals("pdf")){
                    System.out.println("Running " + wikiProcess);
                    WikiProcessor wikiProcessor = new WikiProcessor();
                    wikiProcessor.createPdf();

                } else if(wikiProcess.equals("toc")){
                    System.out.println("Running " + wikiProcess);
                    String fileName = "enwiki-latest-all-titles-in-ns0";
                    WikiTitleParser parser = new WikiTitleParser();

                    try {
                        parser.parseTxt(fileName);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else if(wikiProcess.equals("pagenumbers")){
                    System.out.println("Running " + wikiProcess);
                    
                } else if(wikiProcess.equals("covers")){
                    System.out.println("Running " + wikiProcess);
                    WikiCoverParser parser = new WikiCoverParser();
                    try {
                        parser.parseCover();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    
                }

        
        //Deprecated may 2013
        //WikiHDProcessor wikiProcessor = new WikiHDProcessor();
        //WikiThreadingProcessor wikiProcessor = new WikiThreadingProcessor(); // creates new object from wikithreadingprocessor.java
        //wikiProcessor.createPdf(); // calls createPdf method. does not complete this
        
        }catch(Exception ex){
            WikiLogger.getLogger().severe(ex.getMessage());
        }catch(Error th){
            WikiLogger.getLogger().severe(th.getMessage() + " memory:" + 
               ByteFormatter.format(Runtime.getRuntime().freeMemory()));
        }catch(Throwable ex0){
            WikiLogger.getLogger().severe(ex0.getMessage());
        }
    }
    
    public PageParserExec() {  // do we need this? is this driftwood?
    }

}
