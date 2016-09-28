/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;
import java.io.IOException;
import wikitopdf.utils.WikiSettings;
import wikitopdf.pdf.WikiFont;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiFontSelector {

    @SuppressWarnings("static-access")
    private FontSelector getFontSelector() throws DocumentException, IOException {
        //General font, uses for latin chars
        BaseFont bsFontLatin = BaseFont.createFont("fonts/Cardo_no_hebrew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        //uses for non-latin chars
        BaseFont bsFontGlyph = BaseFont.createFont("fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        
        BaseFont bsChinese1 = BaseFont.createFont("fonts/cwTeXFangSong-zhonly.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsChinese2 = BaseFont.createFont("fonts/cwTeXHei-zhonly.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsChinese3 = BaseFont.createFont("fonts/cwTeXKai-zhonly.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsChinese4 = BaseFont.createFont("fonts/cwTeXMing-zhonly.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsChinese5 = BaseFont.createFont("fonts/cwTeXYen-zhonly.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        BaseFont bsArab1 = BaseFont.createFont("fonts/Amiri-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        BaseFont bsArab2 = BaseFont.createFont("fonts/DroidKufi-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        fontLatin = new Font(bsFontLatin);
        //this is where TOC font is set, as well as the H1 font for the full entries
        //set at 10.5  for full entries, 7 for TOC
        fontLatin.setSize(7f);
//        fontLatin.setStyle(fontLatin.BOLD);

        //font color is RGB, for example {255,0,0} is RED
//        fontLatin.setColor(wikiFont.getFontColor()[0], wikiFont.getFontColor()[1], wikiFont.getFontColor()[2]);

        fontLatin.setColor(0,0,0);

        Font fontGlyph = new Font(bsFontGlyph);
        Font chinese1 = new Font(bsChinese1);
        Font chinese2 = new Font(bsChinese2);
        Font chinese3 = new Font(bsChinese3);
        Font chinese4 = new Font(bsChinese4);
        Font chinese5 = new Font(bsChinese5);
        Font helv = new Font(bsHelv);
        //this is where TOC font is set, as well as the H1 font for the full entries
        //set at 10.5  for full entries, 7 for TOC
        chinese1.setSize(7f);
        chinese2.setSize(7f);
        chinese3.setSize(7f);
        chinese4.setSize(7f);
        chinese5.setSize(7f);
        helv.setSize(7f);
        chinese1.setStyle(Font.DEFAULTSIZE);
        chinese2.setStyle(Font.DEFAULTSIZE);
        chinese3.setStyle(Font.DEFAULTSIZE);
        chinese4.setStyle(Font.DEFAULTSIZE);
        chinese5.setStyle(Font.DEFAULTSIZE);
        fontGlyph.setSize(7f); 
        fontGlyph.setStyle(Font.DEFAULTSIZE);        
        helv.setStyle(Font.DEFAULTSIZE);
        
        _fontSelector = new FontSelector();
        _fontSelector.addFont(fontLatin);
        _fontSelector.addFont(fontGlyph);
        _fontSelector.addFont(helv);
        _fontSelector.addFont(chinese1);
        _fontSelector.addFont(chinese2);
        _fontSelector.addFont(chinese3);
        _fontSelector.addFont(chinese4);
        _fontSelector.addFont(chinese5);
        //        _fontSelector.addFont(arab1);
//        _fontSelector.addFont(arab2);
//        
        

        return _fontSelector;
    }

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getTitleFontSelector() throws DocumentException, IOException {
        //the following line does nothing. variable is not used or returned?
//        WikiFont titleFont = WikiSettings.getInstance().getTitleFont();
        return getFontSelector();
    }
    
    

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getCommentFontSelector() throws DocumentException, IOException {
//        WikiFont commentFont = WikiSettings.getInstance().getCommentFont();
        return getFontSelector();
    }

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getTextFontSelector() throws DocumentException, IOException {
        //WikiFont textFont = WikiSettings.getInstance().getTextFont();
        return getFontSelector();
    }

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getContributorFontSelector() throws DocumentException, IOException {
//        WikiFont contributorFont = WikiSettings.getInstance().getContributorFont();
        return getFontSelector();
    }

    /**
     *
     * @return
     */
    public Font getCommonFont() {
        return fontLatin;
    }
    private Font fontLatin;
    private FontSelector _fontSelector = new FontSelector();
}
