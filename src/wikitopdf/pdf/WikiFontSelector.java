/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;
import java.io.IOException;
import wikitopdf.utils.WikiSettings;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiFontSelector {

    @SuppressWarnings("static-access")
    private FontSelector getFontSelector() throws DocumentException, IOException {
        //General font, uses for latin chars
        BaseFont bsFontLatin = BaseFont.createFont("fonts/Cardo98s.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        //uses for non-latin chars
        BaseFont bsFontGlyph = BaseFont.createFont("fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        fontLatin = new Font(bsFontLatin);
        //this is where TOC font is set, as well as the H1 font for the full entries
        //set at 10.5  for full entries, 7 for TOC
        fontLatin.setSize(10.5f);   
        fontLatin.setStyle(fontLatin.BOLD);

        //font color is RGB, for example {255,0,0} is RED
//        fontLatin.setColor(wikiFont.getFontColor()[0], wikiFont.getFontColor()[1], wikiFont.getFontColor()[2]);

        fontLatin.setColor(0,0,0);

        Font fontGlyph = new Font(bsFontGlyph);
        //this is where TOC font is set, as well as the H1 font for the full entries
        //set at 10.5  for full entries, 7 for TOC
        fontGlyph.setSize(10.5f); 
        fontGlyph.setStyle(Font.BOLD);
        
        Font helv = new Font(bsHelv);
        helv.setSize(10f);
        helv.setStyle(Font.BOLD);
        //fontHieroglyph.setColor(wikiFont.getFontColor()[0], wikiFont.getFontColor()[1], wikiFont.getFontColor()[2]);

        //Font selector uses to choose proper font for different charsets
        _fontSelector = new FontSelector();
        _fontSelector.addFont(fontLatin);
        _fontSelector.addFont(fontGlyph);
        _fontSelector.addFont(helv);
        

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
        WikiFont titleFont = WikiSettings.getInstance().getTitleFont();

        return getFontSelector();
    }
    
    

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getCommentFontSelector() throws DocumentException, IOException {
        WikiFont commentFont = WikiSettings.getInstance().getCommentFont();
        return getFontSelector();
    }

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getTextFontSelector() throws DocumentException, IOException {
        WikiFont textFont = WikiSettings.getInstance().getTextFont();
        return getFontSelector();
    }

    /**
     *
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public FontSelector getContributorFontSelector() throws DocumentException, IOException {
        WikiFont contributorFont = WikiSettings.getInstance().getContributorFont();
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
