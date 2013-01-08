/*
 */

package wikitopdf.utils;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class ByteFormatter {
    /**
     *
     * @param bytes
     * @return
     */
    public static String format(long bytes){

        String result = "";
        String b = "b";
        int kilo = 1024;
        int mega = 1024 * 1024;
        int giga = 1024 * 1024 * 1024;


        if(bytes < kilo){
            result = bytes + b;
        }else if(bytes < mega){
            result = bytes/kilo + "K" + b;
        }else if(bytes < giga){
            result = bytes/mega + "M" + b;
        }else {
            result = bytes/giga + "G" + b;
        }

        return result;
    }
}
