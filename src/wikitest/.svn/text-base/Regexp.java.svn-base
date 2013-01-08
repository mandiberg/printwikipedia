/*
 */

package wikitest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class Regexp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String source = "fdfdsf <h1>heAder1</h1> fgfsgs sgsdg sdgsggd  <h3>heAder3</h3> sdfsdf";// +
//                           "1. outer membrane<br />" +
//                           "2. intermembrane space<br />" +
//                           "3. inner membrane (1+2+3: envelope)<br />" +
//                           "4. stroma (aqueous fluid)<br />" +
//
//                            "]] dfdfdc sdgsdg";
        
        String imagePattern = "<h[1-7]>(.*?)</h[1-7]>";

        //source.replaceAll(imagePattern, "!!");
        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(source);
        StringBuffer result = new StringBuffer();

        while ( matcher.find() ) {
            matcher.appendReplacement(result, matcher.group(0).toUpperCase());
        }

        matcher.appendTail(result);
        //System.out.println(matcher.group(0));
        source = matcher.replaceAll(new String("q$1q").toUpperCase());
        
        System.out.println(result);


    }

}
