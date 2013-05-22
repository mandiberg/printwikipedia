//DEPRECATED MAY 2013

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class TitleParserExec {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileName = "archive/enwiki";
        //WikiTestParser parser = new WikiTestParser();
        WikiTitleParser parser = new WikiTitleParser();

        try {
            parser.parseTxt(fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
