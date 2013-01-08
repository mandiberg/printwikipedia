/*
 */

package wikitest;

import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.model.WikiModel;



/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        String source = "===Header=== \r\n" +
                        "{{Infobox VG \r\n" +
                        "|title = Bubble Bobble\r\n" +
                        "|image\r\n" +
                        "}}";
        
        source = "{{Infobox Software fg \n " + "|name = JAMWiki\n" + "|logo = \n"
                    + "|caption =\n" + "|developer = \n" + "|latest_release_version = 0.6.5\n"
                    + "|latest_release_date = [[March 16]], [[2008]]\n" + "|latest preview version = 0.6.5 \n"
                    + "|latest preview date = \n" + "|operating_system = [[Cross-platform]]\n"
                    + "|genre = [[wiki software|Wiki software]]\n" + "|license = [[GNU Lesser General Public License|LGPL]]\n"
                    + "|website = [http://www.jamwiki.org/ JAMWiki wiki]\n"
                    + "}}\n";

         WikiModel wikiModel = new WikiModel("", "");
        //txt = clearText(text);
        //String output = wikiModel.render(source);
        String output = wikiModel.render(new HTMLConverter(), source);
        wikiModel.setUp();
        System.out.println(source);
        System.out.println(output);
    }
}
