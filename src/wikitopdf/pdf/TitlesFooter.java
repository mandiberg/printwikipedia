/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//deals with TOC header events.
package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wikitopdf.WikiTitleParser;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class TitlesFooter extends PdfPageEventHelper
{
    public int lineC = 0;
    
    /**
     *
     * @param startPage
     */
    public TitlesFooter(int startPage)
    {
        if(startPage>1)
            this.startPage = startPage-1;
        else
            this.startPage = startPage;
    }
        
        
        
    

    @Override
    public void onStartPage(PdfWriter writer, Document document)
    {     
        System.out.println("im start");
        if(writer.getPageNumber()==701)
            return;
        //header doesnt print for first page only
        pageNum = writer.getPageNumber() + startPage;
      
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document)
    {
        try
        {
            System.out.println("im open");
            String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
            BaseFont bsFontGlyph=null;
            bsFontGlyph = BaseFont.createFont("fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            Font fontGlyph = new Font(bsFontGlyph);
            Font helv = new Font(bsHelv);
            Font pnumbers = new Font(bsHelv,8);
            fontGlyph.setSize(7f); 
            helv.setSize(7f);

            FontFactory.register(path_to_fonts+"Cardo_no_hebrew.ttf","cardo");
            FontFactory.register(path_to_fonts+"cwTeXFangSong-zhonly.ttf","chinese1");
            FontFactory.register(path_to_fonts+"cwTeXHei-zhonly.ttf","chinese2");
            FontFactory.register(path_to_fonts+"cwTeXKai-zhonly.tff","chinese3");
            FontFactory.register(path_to_fonts+"cwTeXMing-zhonly.ttf","chinese4");
            FontFactory.register(path_to_fonts+"cwTeXYen-zhonly.ttf","chinese5");
            FontFactory.register(path_to_fonts+"G5LISL1B.TTF","chinese6");
            FontFactory.register(path_to_fonts+"Amiri-Regular.ttf","arab1");
            FontFactory.register(path_to_fonts+"DroidKufi-Regular.ttf","arab2");
            FontFactory.register(path_to_fonts+"Alef-Regular.ttf","hebrew");
            FontFactory.register(path_to_fonts+"NotoSansCherokee-Regular.ttf","cherokee");
            FontFactory.register(path_to_fonts+"NotoSansGeorgian-Regular.ttf","georgian");
            FontFactory.register(path_to_fonts+"NotoSansDevanagari-Regular.ttf","devanagari");
            FontFactory.register(path_to_fonts+"NanumGothic-Regular.ttf","nanum");
            FontFactory.register(path_to_fonts+"NotoKufiArabic-Regular.ttf","arab3");
            FontFactory.register(path_to_fonts+"NotoSansJP-Regular.otf","jap");
            FontFactory.register(path_to_fonts+"NotoSansKhmer-Regular.ttf","khmer");
            FontFactory.register(path_to_fonts+"NotoSansThai-Regular.ttf","thai");
            FontFactory.register(path_to_fonts+"NotoSerifArmenian-Regular.ttf","armenian");
            FontFactory.register(path_to_fonts+"NotoSansTamilUI-Regular.ttf","tamil");
            FontFactory.register(path_to_fonts+"DejaVuSans.ttf","dvs");
            FontFactory.register(path_to_fonts+"Roboto-Regular.ttf","roboto");
            FontFactory.register(path_to_fonts+"OpenSans-Light.ttf","ops");
            FontFactory.register(path_to_fonts+"MCTIME.TTF","russ");
            FontFactory.register(path_to_fonts+"FreeSerif.ttf","fser");
            FontFactory.register(path_to_fonts+"NotoSansSinhala-Regular.ttf","sinhala");
            FontFactory.register(path_to_fonts+"NotoSansBengali-Regular.ttf","bengali");
            FontFactory.register(path_to_fonts+"lohit_gu.ttf","punj");
            FontFactory.register(path_to_fonts+"FreeSans.ttf","fsans");
            FontFactory.register(path_to_fonts+"NotoSansTelugu-Regular.ttf","telugu");
            FontFactory.register(path_to_fonts+"Cybercjk.ttf","cjk");
            FontFactory.register(path_to_fonts+"IndUni-N-Roman.otf","ind");
            FontFactory.register(path_to_fonts+"lohit_or.ttf","oriya");
    //      System.out.println(FontFactory.getRegisteredFonts().toString());//show all fonts being used.




            Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
            Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
            Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
            Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
            Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);

            hfs.addFont(cardo);
            //fs.addFont(dvs);
            hfs.addFont(fser);
            hfs.addFont(cjk);
            hfs.addFont(arab1);
            hfs.addFont(arab2);
            hfs.addFont(arab3);
    //                    fs.addFont(ind);
            hfs.addFont(hebrew);
            hfs.addFont(russ);
            hfs.addFont(armenian);
            hfs.addFont(chinese1);
            hfs.addFont(chinese2);
            hfs.addFont(chinese3);
            hfs.addFont(chinese4);
            hfs.addFont(chinese5);
            hfs.addFont(chinese6);
            hfs.addFont(cherokee);
            hfs.addFont(georgian);
            hfs.addFont(devanagari);
            hfs.addFont(nanum);
            hfs.addFont(jap);
            hfs.addFont(khmer);
            hfs.addFont(thai);
            hfs.addFont(tamil);
            hfs.addFont(ops);
            hfs.addFont(fontGlyph);
            hfs.addFont(helv);
            hfs.addFont(roboto);
            hfs.addFont(sinhala);
            hfs.addFont(bengali);
            hfs.addFont(punj);
            hfs.addFont(fsans);
            hfs.addFont(telugu);
            hfs.addFont(oriya);
            hfs.addFont(fser);
            total = writer.getDirectContent().createTemplate(100, 100);
            total.setBoundingBox(new Rectangle(-20, -20, 100, 100));
            try
            {
                WikiFontSelector wikiFontSelector = new WikiFontSelector();
                wikiFontSelector.getTitleFontSelector().process("");
                bsFont = wikiFontSelector.getCommonFont().getBaseFont();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        catch (DocumentException ex)
        {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(startPage>1)
            pageNum = startPage - 1;//i think the logic here is screwy...
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document)
    {  
        System.out.println(writer.getPageNumber());
        
        //footer doesnt print for first and second pages
        pageNum = writer.getPageNumber() + startPage;

        if(writer.getPageNumber()>700){
            System.out.println(writer.getPageNumber() + " page too far" + pageNum);
            return;
        }

        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        cb.setFontAndSize(bsFont, 8);
        if(writer.getPageNumber()==1){
            Phrase pnum = hfs.process(String.valueOf(pageNum));
            pnum.setFont(pnumbers);            
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pnum, document.right(), document.bottom() - 34, 0);
        }
        cb.restoreState();
        writeHeader(writer, document);

        
        
    }

    @Override
    public void onCloseDocument(PdfWriter writer, Document document)
    {
        total.beginText();
        total.setFontAndSize(bsFont, 8);
        total.setTextMatrix(0, 0);
        total.showText(String.valueOf(writer.getPageNumber() - 1));
        total.endText();
        
    }

    /**
     *
     * @return
     */
    public int getPageNum()
    {
        return pageNum;
    }

    /**
     *
     * @param line
     */
    public void setCurrentLine(String line)
    {
        this.lineList.add(line);
    }

    private void writeHeader(PdfWriter writer, Document document)
    {
        pageNum = writer.getPageNumber() + startPage;
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();//if you save state you gotta restore afterwards. savesate only for content bytes.
        if(writer.getPageNumber()>700){
            cb.restoreState();
            return;
        }

        String text;
        float textBase = document.top() - 43;

        Phrase pnum = hfs.process(String.valueOf(pageNum));
        pnum.setFont(pnumbers);            
            

        if((writer.getPageNumber() % 2) ==0)//even#page --right top corner
        {
            
//            cb.setFontAndSize(bsFont, 8);
            if(writer.getPageNumber() == 2){
                text = lineList.get(pg3+1);
            }
            if(writer.getPageNumber()==700){
                text=lineList.get(lineList.size()-1);
            }
            else{
                text = lineList.get(lineC);
            }
            //Cut if title very long
            text = text.length() > 20 ? text.substring(0, 20) + "..." : text;
            Phrase ph = hfs.process(text.toUpperCase());
//            System.out.println(text + " in even page.");
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, ph, document.left(), document.top()-43, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, pnum, document.left(), document.bottom() - 34, 0);
            cb.restoreState();
            return;
        }
        if ((writer.getPageNumber() % 2) == 1)//odd # page --Left top corner
        {
            lineC = lineList.size();
            text = lineList.get(lineC - 1);
            text = text.length() > 20 ? text.substring(0, 20) + "..." : text;
            Phrase ph = hfs.process(text.toUpperCase());
//            System.out.println(text + " in odd page.");
            if(writer.getPageNumber()!=1){
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, ph, document.right(), document.top()-43, 0);
                ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pnum, document.right(), document.bottom() - 34, 0);
            }
            cb.restoreState();
            return;
        }
        
        cb.restoreState();
    }
    

    /**
     *
     * @return
     */
    public int getLineCount()
    {
        return this.lineList.size();
    }
    private PdfContentByte contentPage;
    private PdfTemplate total;
    private BaseFont bsFont;
    private int pageNum = 0;
    private int pageNumber = 0;
    private int startPage = 0;
    private int pg3 = 0;
    public boolean pageFull;
    public Font pnumbers = null;
    public ArrayList <String> lineList = new ArrayList<String>();
    public FontSelector hfs = new FontSelector();
}
