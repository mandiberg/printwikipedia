package wikitopdf;
/* *************************************************** */
// Included for debuging delete after done
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
/* *************************************************** */
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
    /* *************************************************** */
    
    int length = args.length;
    String wikiProcess = "";
    String prod = "";
    //String isThreaded = "threaded";
    if (length <= 0) {
        System.out.println("Default is pdf. Options are: pdf, threaded, toc, pagenumbers, covers. e.g.: java -jar \"wikitopdf.jar\" pdf");
        wikiProcess = "pdf";
    } else {
    // setting the process from the command line args
        wikiProcess = args[0];
    }
    if(length > 1){
        prod = args[1];
    }
    if(prod.equals("prod")){//is production and won't print out to a file. this saves a bunch of time and doesn't use much memory.
        System.setOut(new PrintStream(new OutputStream() {

        @Override
        public void write(int arg0) throws IOException {
            //do nothing.
        }
        }));
    }
    else if(prod.equals("testing")){ //writes to file so all system.out.println is viewable there.
        // Included for debuging delete after done
        try {
            PrintStream out = new PrintStream(new FileOutputStream("fullOutput.txt"));
            System.setOut(out);
            System.setErr(out);
        } catch (Exception ex){

        }
        /* *************************************************** */
    }
    try{
    //based on the wikiProcess, does one of several processes
        if(wikiProcess.equals("threaded")){
            System.out.println("Running " + wikiProcess);
            ActorProcessor actor = new ActorProcessor();
            actor.act();
        } else if(wikiProcess.equals("pdf")){
                System.out.println("Running " + wikiProcess + " "+ System.getProperty("user.dir"));
                WikiProcessor wikiProcessor = new WikiProcessor();
                wikiProcessor.createPdf();
        } else if(wikiProcess.equals("toc")){
            System.out.println("Running " + wikiProcess + " "+ System.getProperty("user.dir"));
              //change this to be whatever you wish to iterate through to make TOC formatted pdfs
              String fileName = "blahlist.txt";
//            String fileName = "alphabetlzed_nov_usernames_final.txt";
            
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
//        WikiLogger.getLogger().severe(ex.getMessage());
ex.printStackTrace();
    }catch(Error th){
//        WikiLogger.getLogger().severe(th.getMessage() + " memory:" +
//        ByteFormatter.format(Runtime.getRuntime().freeMemory()));
th.printStackTrace();
    }catch(Throwable ex0){
//        WikiLogger.getLogger().severe(ex0.getMessage());
ex0.printStackTrace();
    }
    }
    public PageParserExec() { // do we need this? is this driftwood?
    }
}