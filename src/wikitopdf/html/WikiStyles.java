package wikitopdf.html;

import com.lowagie.text.FontFactory;
import com.lowagie.text.html.simpleparser.StyleSheet;
import java.util.HashMap;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */

public class WikiStyles {

    /**
     *
     * @return
     */
    public static StyleSheet getStyles(){

        FontFactory.register("fonts/Cardo_no_hebrew.ttf");
        //FontFactory.register("fonts/Verdana.ttf");
        //FontFactory.register("fonts/Junicode-Regular.ttf");
        //FontFactory.register("fonts/msgothic.ttc");

        HashMap defaultStyle = new HashMap();
        defaultStyle.put("face", "Cardo");
        defaultStyle.put("size", getPointSize(8));
        defaultStyle.put("color", "black");
        //defaultStyle.put("encoding", "Identity-H");

        HashMap defaultFont = new HashMap();
        defaultFont.put("face", "Cardo");
        defaultFont.put("font-weight", "bold");

        //defaultFont.put("encoding", "Identity-H");

        StyleSheet styles = new StyleSheet();
        styles.loadTagStyle("body", defaultStyle);
        styles.loadTagStyle("p", defaultStyle);
        styles.loadTagStyle("li", "after", "1");
        styles.loadTagStyle("ul", defaultStyle);
        styles.loadTagStyle("a", defaultStyle);
        styles.loadTagStyle("dt", defaultStyle);
        styles.loadTagStyle("dl", defaultStyle);
        styles.loadTagStyle("th", defaultStyle);
        styles.loadTagStyle("td", defaultStyle);
        styles.loadTagStyle("pre", defaultStyle);
        styles.loadTagStyle("font", defaultStyle);
        styles.loadTagStyle("span", defaultStyle);
        styles.loadTagStyle("b", defaultStyle);
        styles.loadTagStyle("i", defaultStyle);
        styles.loadTagStyle("h1", defaultFont);
        styles.loadTagStyle("h1", "size", getPointSize(10));
        styles.loadTagStyle("h1", "before", "110");
        styles.loadTagStyle("h1", "after", "10");
        styles.loadTagStyle("h1", "font-weight", "bold");
        styles.loadTagStyle("H2", defaultFont);
        styles.loadTagStyle("h2", "size", getPointSize(8));
        styles.loadTagStyle("h2", "before", "20");
        styles.loadTagStyle("h2", "after", "120");
        styles.loadTagStyle("h2", "font-weight", "bold");
        styles.loadTagStyle("h3", defaultFont);
        styles.loadTagStyle("h3", "size", getPointSize(6));
        styles.loadTagStyle("h3", "before", "126");
        styles.loadTagStyle("h3", "after", "6");
        styles.loadTagStyle("h4", defaultFont);
        styles.loadTagStyle("h4", "size", getPointSize(6));
        styles.loadTagStyle("h4", "before", "126");
        styles.loadTagStyle("h4", "after", "6");
        

        return styles;
    }

    /**
     *
     * @return
     */
    public static StyleSheet getHDStyles(){

        //FontFactory.register("fonts/Cardo98s.ttf");
        FontFactory.register("fonts/Verdana.ttf");
        //FontFactory.register("fonts/Junicode-Regular.ttf");
        //FontFactory.register("fonts/msgothic.ttc");

        HashMap defaultStyle = new HashMap();
        defaultStyle.put("face", "Verdana");
        defaultStyle.put("size", getPointSize(12));
        defaultStyle.put("color", "black");
        //defaultStyle.put("encoding", "Identity-H");

        HashMap defaultFont = new HashMap();
        defaultFont.put("face", "Verdana");
        defaultFont.put("font-weight", "bold");

        //defaultFont.put("encoding", "Identity-H");

        StyleSheet styles = new StyleSheet();
        styles.loadTagStyle("body", defaultStyle);
        styles.loadTagStyle("p", "before","3");
        styles.loadTagStyle("p", "after","3");
        styles.loadTagStyle("li", defaultStyle);
        styles.loadTagStyle("a", defaultStyle);
        styles.loadTagStyle("dt", defaultStyle);
        styles.loadTagStyle("dl", defaultStyle);
        styles.loadTagStyle("th", defaultStyle);
        styles.loadTagStyle("td", defaultStyle);
        styles.loadTagStyle("pre", defaultStyle);
        styles.loadTagStyle("font", defaultStyle);
        styles.loadTagStyle("span", defaultStyle);
        styles.loadTagStyle("b", defaultStyle);
        styles.loadTagStyle("i", defaultStyle);
        styles.loadTagStyle("h1", defaultFont);
        styles.loadTagStyle("h1", "size", getPointSize(24));
        styles.loadTagStyle("h1", "before", "10");
        styles.loadTagStyle("h1", "after", "10");
        styles.loadTagStyle("h2", defaultFont);
        styles.loadTagStyle("h2", "size", getPointSize(18));
        styles.loadTagStyle("h2", "before", "10");
        styles.loadTagStyle("h2", "after", "10");
        styles.loadTagStyle("h2", "font-weight", "bold");

        styles.loadTagStyle("h3", defaultFont);
        styles.loadTagStyle("h3", "size", getPointSize(12));
        styles.loadTagStyle("h3", "before", "6");
        styles.loadTagStyle("h3", "after", "6");
        styles.loadTagStyle("h4", defaultFont);
        styles.loadTagStyle("h4", "size", getPointSize(10));
        styles.loadTagStyle("h4", "before", "6");
        styles.loadTagStyle("h4", "after", "6");

        return styles;
    }

    private static String getPointSize(float size){
        String pointSize = String.valueOf(size / 12);
        return pointSize;
    }
}