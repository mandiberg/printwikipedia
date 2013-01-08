/*
 */
package wikitopdf.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import wikitopdf.WikiLogFormatter;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiLogger {

    private static void init() {

        logger = Logger.getLogger("WikiLog");

        FileHandler fh;
        String fileName = "wikilog";
        int fileSize = 500 * 1024;
        int fileCount = 30000;
        String logFolder = WikiSettings.getInstance().getLogFolder();


        try {

            // This block configure the logger with handler and formatter
            //fh = new FileHandler("./printwiki.00mm.org/" + fileName.concat("%g.html"),
            fh = new FileHandler(logFolder + "/" + fileName.concat("%g.html"),
                        fileSize, fileCount, false);

            logger.addHandler(fh);
            logger.setLevel(Level.ALL);
            WikiLogFormatter formatter = new WikiLogFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static Logger getLogger(){
        if(null == logger){
            init();
        }

        return logger;
    }

    private static Logger logger = null;
}
