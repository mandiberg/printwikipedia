package wikitopdf.html;

import info.bliki.wiki.model.Reference;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiHtmlConverter {
  
    /**
     *
     * @param text
     * @return
     */
    public static String convertToHtml(String text) throws IOException{

        WikiPdfModel wikiModel = new WikiPdfModel();
        //txt = clearText(text);
        String output = wikiModel.render(text);
        wpm = wikiModel;
        
        // The other peice of Helvetica text for double paragraph other in PDFPageWrapper
        //replace some of these thigns to see if they are causing the problem
        output = headerToUppercase(output) + "<b>_____________________</b><br /><br />";
        output = output.replace("<hr/>", "");
        String whitespacePattern = "(<p>\\s+)(</p>)";//strange empty <p> tags at the end of articles.
        output = output.replaceAll(whitespacePattern,"");
        String blankHrefPattern = "(<li class=\"toclevel-\\d+\"><a href=\"#\"></a>)";//blank unrenderable href tags in the toc of the article.
        output = output.replaceAll(blankHrefPattern,"");
        String hangingTemplate = "\\s*<p>.+?(}})\\s*</p>";
        output = output.replaceAll(hangingTemplate,"");
        String htSecond = "\\n\\W.+?(}})";
        output = output.replaceAll(htSecond,"");
//        String defaultSort = "(<p>+.*}}\\s*</p>)";
//        output = output.replaceAll(defaultSort,"");

        return output;
    }

    public static List<Reference> getModelReferences(){
        return wpm.getReferences();
    }
    private static String clearText(String source){
        String imagePattern = "\\[\\[Image:(.|[\r\n])*\\]\\]";

        //source.replaceAll(imagePattern, "");
        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(source);
//        if(matcher.find()){
//            System.out.println(matcher.find(1));
//            source = matcher.replaceAll("IMGGG");
//        }

        source = matcher.replaceAll("IMGGG");

        return source;
    }

    private static String headerToUppercase(String source){
        StringBuffer result = new StringBuffer();
        String imagePattern = "<h[1-7]>(.*?)</h[1-7]>";

        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            matcher.appendReplacement(result, matcher.group(0).toUpperCase());
//            System.out.println(matcher.group(0) + " < match group");
        }

        matcher.appendTail(result);

        return result.toString();
    }



    /**
     *
     * @return
     */
    public static String getText(){
        return txt;
    }

    private static String txt = "";
    private static WikiPdfModel wpm = null;
}
