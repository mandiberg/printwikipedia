/*
PageHeaderEvent.java
 */
package wikitopdf.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import wikitopdf.utils.WikiLogger;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
//deals with pdf header events.
public class PageHeaderEvent extends PdfPageEventHelper {

    /**
     *
     * @param startPage
     */
    public PageHeaderEvent(int startPage, Document pdfDoc) {
        this.startPage = startPage;
        this.docu = pdfDoc;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        try {//make font stack right off the bat when opening.
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
//                System.out.println(FontFactory.getRegisteredFonts().toString());


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
                as.add(arab1);
                as.add(arab2);
                as.add(arab3);
                as.add(hebrew);
                

            contentPage = writer.getDirectContent();
            theBottom = document.bottom();
            
            try {
                WikiFontSelector wikiFontSelector = new WikiFontSelector();
                wikiFontSelector.getTitleFontSelector().process("");
                bsFont = wikiFontSelector.getCommonFont().getBaseFont();
            } catch (Exception e) {
                WikiLogger.getLogger().severe(e.getMessage());
            }
            System.out.println("done in opendoc");
        } catch (DocumentException ex) {
            Logger.getLogger(PageHeaderEvent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PageHeaderEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document){
//       System.err.println("in onstartpage" + writer.getPageNumber());
       pageNum = writer.getPageNumber() + startPage;
       
       if (writer.getPageNumber()<2)
           return;
       contentPage.saveState();
       if ((writer.getPageNumber() % 2) == 0 ) {
           try {
               writeHeader(currentTitle, document.left(),//-5.5f,
                        document.getPageSize().getHeight() - 27, PdfContentByte.ALIGN_LEFT,document,writer);
           } catch (DocumentException ex) {
               Logger.getLogger(PageHeaderEvent.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
       contentPage.restoreState();    
       System.out.println("Done in start page");
    }
    

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        System.out.println("current page num");
        System.out.println(writer.getPageNumber());
            
        pageNum = writer.getPageNumber() + startPage;
        contentPage.saveState();

        float textBase = document.bottom() +2;
        if(writer.getPageNumber()==1){
            Phrase pnum = hfs.process(String.valueOf(pageNum));
            ColumnText.showTextAligned(contentPage, Element.ALIGN_RIGHT, pnum, document.right(), document.bottom()-23, 0);
        }
        
        currentTitle = currentTitle.length() > 20 ? currentTitle.substring(0, 20) + "..." : currentTitle;
        if ((writer.getPageNumber() % 2) == 1)
        {
                    int sign = 1;
                    contentPage.moveTo(document.right()+13.5f* sign, theBottom -13);
                    contentPage.setColorStroke(new GrayColor(1));
                    contentPage.lineTo(document.right() + 13.5f* sign, theBottom - 24);
                    contentPage.stroke();
            
            if(writer.getPageNumber()>1){

                    try {
                        writeHeader(currentTitle, document.right(),//-5.5f,
                            document.getPageSize().getHeight() - 27, PdfContentByte.ALIGN_RIGHT,document,writer);

                    } catch (DocumentException ex) {
                        Logger.getLogger(PageHeaderEvent.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            contentPage.restoreState();
        }
        else
        {
            contentPage.restoreState();
        }
        System.out.println("done in endpage");
    }

    /**
     *
     * @param line
     */
    public void setCurrentTitle(String line) {
        //Current line for header
        currentTitle = line;
    }

    /**
     *
     * @return
     */
    public int getPageNum(){
        return pageNum;
    }
    public boolean isRTL(ArrayList as, Phrase ph){
        ArrayList chunks = ph.getChunks();
                for(int i=0; i < chunks.size(); i++){
                    Chunk lilchunk = (Chunk) chunks.get(i);
                    String[][] ane = lilchunk.getFont().getBaseFont().getAllNameEntries();
                    
                    if(as.contains(lilchunk.getFont())){
                        return true;
                        
                    }
                }
        return false;
    }
    public PdfPTable arabicHeader(Phrase ph, PdfWriter pdfWriter){
        Paragraph pr = new Paragraph(ph);
        PdfPTable table = new PdfPTable(1);
        PdfPCell celly = new PdfPCell();
        celly.addElement(pr);
        celly.setBorderColor(Color.white);
        celly.setBorderWidth(0);
        celly.setPadding(0);
        celly.setRunDirection(pdfWriter.RUN_DIRECTION_RTL);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
        table.addCell(celly);  
        return table;

    }
    private void writeHeader(String text, float x, float y, int align, Document document,PdfWriter pdfWriter) throws DocumentException{ //place the title and page number appropriately.
        //not liking that this is done using content bytes. should be using pdfpcell/table with phrase.
//        PdfPTable pp = null;
        
        int sign = (align == PdfContentByte.ALIGN_LEFT) ? -1 : 1;
        Phrase ph = hfs.process(text.toUpperCase());
//        if(isRTL(as,ph)){
//            pp = arabicHeader(ph,pdfWriter);
//        }
        
        Phrase pnum = hfs.process(String.valueOf(pageNum));
        pnum.setFont(pnumbers);
        if(document.getPageNumber() % 2 == 0){
            
//            if(pp != null)
//                ColumnText.showTextAligned(contentPage, Element.ALIGN_LEFT, ph, x, y, 0);
//            else
                ColumnText.showTextAligned(contentPage, Element.ALIGN_LEFT, ph, x, y, 0);
            ColumnText.showTextAligned(contentPage, Element.ALIGN_LEFT, pnum, x, document.bottom()-23, 0);
        }
        else{
            ColumnText.showTextAligned(contentPage, Element.ALIGN_RIGHT, ph, x, y, 0);
            ColumnText.showTextAligned(contentPage, Element.ALIGN_RIGHT, pnum, x, document.bottom()-23, 0);
        }
        contentPage.moveTo(x + 13.5f * sign, y + 10);
        contentPage.setColorStroke(new GrayColor(1));
        contentPage.lineTo(x + 13.5f * sign, y - 1);
        contentPage.stroke();
        contentPage.moveTo(x+13.5f* sign, theBottom -13);
        contentPage.setColorStroke(new GrayColor(1));
        contentPage.lineTo(x + 13.5f* sign, theBottom - 24);
        contentPage.stroke();   
    }

    private PdfContentByte contentPage;
    private BaseFont bsFont;
    private int pageNum = 0;
    private int startPage = 0;
    private float theBottom = 0f;
    private Document docu = null;
    private String currentTitle = "";
    public FontSelector hfs = new FontSelector();
    public ArrayList as = new ArrayList();
    public Font pnumbers = null;
}
