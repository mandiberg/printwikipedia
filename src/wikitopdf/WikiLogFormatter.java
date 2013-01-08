/*
 */

package wikitopdf;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiLogFormatter extends Formatter {

    private MessageFormat messageFormat = new MessageFormat("<span style=\"color:{0}\">{1,date,[dd.MM.yy] HH:mm:ss} : {2}\t{3}\n</span><br />");

    @Override
    public String format(LogRecord record) {
        String levelColor = "";
        
        if(record.getLevel().equals(Level.SEVERE)){
            levelColor = "#AA0000";
        }else if(record.getLevel().equals(Level.FINE)){
            levelColor = "#007700";
        }else{
            levelColor = "#000000";
        }

        Object[] arguments = new Object[4];

        arguments[0] = levelColor;
        arguments[1] = new Date(record.getMillis());
        arguments[2] = (record.getLevel().equals(Level.SEVERE)) ? record.getSourceClassName() +"." + record.getSourceMethodName() : "";
        arguments[3] = record.getMessage();
        
        return messageFormat.format(arguments);
    } 
}
