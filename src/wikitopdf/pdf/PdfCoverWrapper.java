/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.VerticalText;
import java.awt.Color;
import com.lowagie.text.Chunk;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
//import com.itextpdf.text.BaseColor;


/**
 *
 * @author Home
 */
public class PdfCoverWrapper {
    
    private ColumnText ct;
    private int column = 0;
    private int status = 0;
    float[] right = {70, 320};
    float[] left = {300, 550};
    int hc_width = 1103;
    int hc_height = 774;
    int sc_width = 979;
    int sc_height = 666;
    int spine_width = 106;//1.4 = 101, 1.43 = 103, 1.47 = 106, 1.5 = 108
    int spine_height = 649;
    boolean make_spine = true; //for making spines . change this to false if you do not want to generate them.
    //w = 1129 h = 774 for hc 670 pg book
    public int width = hc_width;//spine = 119 points beginning at 495 points.
    //meaning that each page is 495 points wide.
    public int height = hc_height;
    BaseFont times = null;
    Font spine_vol_font = null;
    Font spine_abbr_font = null;
    Font spine_to_font = null;
    Font main_title_font = null;
    Font sc_main_title_font = null;
    Font dis_font = null;
        /**
     *
     * @param num
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public PdfCoverWrapper(int num, int startPage) throws FileNotFoundException, DocumentException {
        //Read settings
        num-=1;
        String outputFileName = "covers/volume&&&" + String.format("%04d",num+1) + ".pdf";
        String spine_out = "covers/spines/spine&&&" + String.format("%04d",num+1) + ".pdf";
        try {
            bflib = BaseFont.createFont("fonts/LinLibertine_Rah.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (IOException ex) {
            Logger.getLogger(PdfCoverWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        Font lib = new Font(bflib);
        
        //start setting up spine and cover doc.
        if(make_spine){
            spineDoc = new Document(new Rectangle(spine_width, spine_height));
            spineWriter = PdfWriter.getInstance(spineDoc, new FileOutputStream(spine_out));
            spineSelector = new WikiFontSelector();
            spineDoc.open();
        }
        pdfDocument = new Document(new Rectangle(width, height));

        pdfDocument.setMargins(25, 25, -35, 25);

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream(outputFileName));

        pdfDocument.open();

        wikiFontSelector = new WikiFontSelector();
        PdfContentByte cb = pdfWriter.getDirectContent();
         
       try
        { //need font stack in order to process different titles and all their characters. new fs for eachc different font size.
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

            FontFactory.register(path_to_fonts+ "LinLibertine_Rah.ttf","liber");
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
    //      System.out.println(FontFactory.getRegisteredFonts().toString());



            Font liber = FontFactory.getFont("liber", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);                    
            Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);                    
            Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);                    
            Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 17);
            
            fs.addFont(liber);
            fs.addFont(cardo);
            //fs.addFont(dvs);
            fs.addFont(fser);
            fs.addFont(cjk);
            fs.addFont(arab1);
            fs.addFont(arab2);
            fs.addFont(arab3);
    //                    fs.addFont(ind);
            fs.addFont(hebrew);
            fs.addFont(russ);
            fs.addFont(armenian);
            fs.addFont(chinese1);
            fs.addFont(chinese2);
            fs.addFont(chinese3);
            fs.addFont(chinese4);
            fs.addFont(chinese5);
            fs.addFont(chinese6);
            
            fs.addFont(cherokee);
            fs.addFont(georgian);
            fs.addFont(devanagari);
            fs.addFont(nanum);
            fs.addFont(jap);
            fs.addFont(khmer);
            fs.addFont(thai);
            fs.addFont(tamil);
            fs.addFont(ops);
            fs.addFont(fontGlyph);
            fs.addFont(helv);
            fs.addFont(roboto);
            fs.addFont(sinhala);
            fs.addFont(bengali);
            fs.addFont(punj);
            fs.addFont(fsans);
            fs.addFont(telugu);
            fs.addFont(oriya);
            fs.addFont(fser);

     
        }
        catch (DocumentException ex)
        {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        }
       try
        {
            String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
            BaseFont bsFontGlyph=null;
            bsFontGlyph = BaseFont.createFont("fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
            Font fontGlyph = new Font(bsFontGlyph);
            Font helv = new Font(bsHelv);
            Font pnumbers = new Font(bsHelv,8);
            fontGlyph.setSize(7f); 
            helv.setSize(7f);
            
            FontFactory.register(path_to_fonts+ "LinLibertine_Rah.ttf","liber");
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
    //      System.out.println(FontFactory.getRegisteredFonts().toString());



            Font liber = FontFactory.getFont("liber", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);                    
            Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);                    
            Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);                    
            Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);

            fs_abbr.addFont(liber);
            fs_abbr.addFont(cardo);
            //fs.addFont(dvs);
            fs_abbr.addFont(fser);
            fs_abbr.addFont(cjk);
            fs_abbr.addFont(arab1);
            fs_abbr.addFont(arab2);
            fs_abbr.addFont(arab3);
    //                    fs.addFont(ind);
            fs_abbr.addFont(hebrew);
            fs_abbr.addFont(russ);
            fs_abbr.addFont(armenian);
            fs_abbr.addFont(chinese1);
            fs_abbr.addFont(chinese2);
            fs_abbr.addFont(chinese3);
            fs_abbr.addFont(chinese4);
            fs_abbr.addFont(chinese5);
            fs_abbr.addFont(chinese6);
            fs_abbr.addFont(cherokee);
            fs_abbr.addFont(georgian);
            fs_abbr.addFont(devanagari);
            fs_abbr.addFont(nanum);
            fs_abbr.addFont(jap);
            fs_abbr.addFont(khmer);
            fs_abbr.addFont(thai);
            fs_abbr.addFont(tamil);
            fs_abbr.addFont(ops);
            fs_abbr.addFont(fontGlyph);
            fs_abbr.addFont(helv);
            fs_abbr.addFont(roboto);
            fs_abbr.addFont(sinhala);
            fs_abbr.addFont(bengali);
            fs_abbr.addFont(punj);
            fs_abbr.addFont(fsans);
            fs_abbr.addFont(telugu);
            fs_abbr.addFont(oriya);
            fs_abbr.addFont(fser);

     
        }
        catch (DocumentException ex)
        {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TitlesFooter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void spineOnly(PdfContentByte cb, String coverType, String fileName, boolean eurotation) throws DocumentException{ //for generating the spines.
        
        int c = 57391;
        String wiki_w = Character.toString((char)c);//generating the wiki W. different from the normal capitalized w
        float half_spine = spine_width/2;
        String main_spine;
        int hc_title_size = 40;
        int spine_y = 0;
        //different spines for different types of output.
        if(coverType == "temp"){
            main_spine = wiki_w+"ikipedia Table of Contents";
            hc_title_size = 32;
        }
        else if(coverType =="output" || coverType == "output-old"||coverType == "output-long"){
            main_spine = wiki_w+"ikipedia";
            hc_title_size=40;
        }
        else if(coverType == "contrib"){
            main_spine = wiki_w+"ikipedia Contributor Appendix";
            hc_title_size = 29;
        }
        else{
            main_spine = "";
        }
        spine_y = spine_height-35;
        int sc_title_size = 35;
        int rotate = 270;
        cb.beginText();
        cb.setFontAndSize(bflib,hc_title_size);
        cb.setTextMatrix(50, 595);
//        float x_spine = 544f;
        float x_spine = 1344;
        if(eurotation){
            rotate=90;
            x_spine = half_spine+9;
//            x_spine = x_spine+207;
          //  if(coverType == "output"){
           //     spine_y = 498;
                spine_y = 438;
           // }
            if(coverType == "contrib"){
                spine_y = 242;
            }
        }
//        cb.showTextAligned(0,main_spine,half_spine-9,spine_y,rotate);
        cb.showTextAligned(0,main_spine,x_spine,spine_y,rotate);
        
        //^rotates the string to show up at 270 degree angle.
        cb.endText();

        String[] titleArr = fileName.split("&&&");
        
        String beginTitle = titleArr[1];
        String endTitle = titleArr[2];
        String volNumber = titleArr[0];
        //****replace the leading zeroes**
        volNumber = volNumber.replaceFirst("^0+(?!$)", "");        

        String lSpineTitle = "";
        String rSpineTitle = "";
        if(beginTitle.length()>3){
            lSpineTitle = beginTitle.substring(0,3);
        }
        else{
            lSpineTitle=beginTitle;
        }
        if(endTitle.length()>3){
            rSpineTitle = endTitle.substring(0,3);
        }
        else{
            rSpineTitle = endTitle;
        }
        
        //using tables to take advantage of their horizontal and vertical alignment 
        PdfPTable tabled = new PdfPTable(1);
        PdfPTable table1 = new PdfPTable(1);
        PdfPTable table2 = new PdfPTable(1);
        PdfPTable table3 = new PdfPTable(1);
        PdfPTable table4 = new PdfPTable(1);

        Paragraph vol_num;
        Paragraph first_abbr;
        Paragraph spine_to;
        Paragraph scnd_abbr;
        Paragraph dis_text;
                
        vol_num = new Paragraph(volNumber, spine_vol_font);
        first_abbr = new Paragraph(fs_abbr.process(lSpineTitle.toUpperCase()));
        scnd_abbr = new Paragraph(fs_abbr.process(rSpineTitle.toUpperCase()));
        spine_to = new Paragraph("TO",spine_to_font);
        
        //spine section for volume number
        PdfPCell cell;
        cell = new PdfPCell(vol_num);
        cell.setBorderWidth(0f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setColspan(1);
        cell.setMinimumHeight(spineDoc.top()+(spineDoc.bottom()-191.8f));
        table1.addCell(cell);
        ColumnText column = new ColumnText(spineWriter.getDirectContent());
        column.addElement(table1);

        float llx_hc_volnum = spineDoc.left()-36;
        float urx_hc_volnum = spineDoc.right()+36;
        float llx_sc_volnum = 391f;
        float urx_sc_volnum = 506f;
        float lly_sc_volnum = 830f;
        column.setSimpleColumn (llx_hc_volnum, spineDoc.bottom()-170, urx_hc_volnum, spineDoc.top()-50f);
        column.go();
        
        //spine section for first abbreviated title
        PdfPCell cell2;    
        cell2 = new PdfPCell(first_abbr);
        cell2.setBorderWidth(0f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell2.setColspan(1);
        cell2.setMinimumHeight(spineDoc.top()+(spineDoc.bottom()-144.8f));
        table2.addCell(cell2);
        ColumnText column2 = new ColumnText(spineWriter.getDirectContent());
        column2.addElement(table2);
        //llx, lly,urx,ury 
        float llx_hc_fa = spineDoc.left()-36;
        float urx_hc_fa = spineDoc.right()+36;
        float llx_sc_fa = 395f;
        float urx_sc_fa  = 497f;
        column2.setSimpleColumn (llx_hc_fa, spineDoc.bottom()-170f, urx_hc_fa, spineDoc.top()-50f);
        column2.go();
        
        //third table for TO
        PdfPCell cell3;    
        cell3 = new PdfPCell(spine_to);
        cell3.setBorderWidth(0f);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell3.setColspan(1);
        cell3.setMinimumHeight(spineDoc.top()+(spineDoc.bottom()-130.8f));
        table3.addCell(cell3);
        ColumnText column3 = new ColumnText(spineWriter.getDirectContent());
        column3.addElement(table3);
        //llx, lly,urx,ury 
        column3.setSimpleColumn(llx_hc_fa, spineDoc.bottom()-170f, urx_hc_fa, spineDoc.top()-50f);
        column3.go();
        
        //4th table for second abbreviated title!
        PdfPCell cell4;    
        cell4 = new PdfPCell(scnd_abbr);
        cell4.setBorderWidth(0f);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell4.setColspan(1);
        cell4.setMinimumHeight(spineDoc.top()+(spineDoc.bottom()-112.8f));
        table4.addCell(cell4);
        ColumnText column4 = new ColumnText(spineWriter.getDirectContent());
        column4.addElement(table4);
        //llx, lly,urx,ury 
        column4.setSimpleColumn (llx_hc_fa, spineDoc.bottom()-170f, urx_hc_fa, spineDoc.top()-50f);
        column4.go();
        try {
            spineDoc.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
        public void addCover(String fileName, String coverType) throws DocumentException {
        int c = 57391;
        String wiki_w = Character.toString((char)c);
        PdfContentByte scb = spineWriter.getDirectContent();
        PdfContentByte cb = pdfWriter.getDirectContent();
        boolean eurotation = true;
        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
            BaseFont spine_base = bflib;
            spine_vol_font = new Font(spine_base, 35);
            spine_abbr_font = new Font(spine_base,20);
            spine_to_font = new Font(spine_base,13);
            main_title_font = new Font(spine_base,17);
            sc_main_title_font = new Font(spine_base, 20);
            dis_font = new Font(spine_base,6);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if(make_spine)
            spineOnly(scb,coverType,fileName,eurotation);
        cb.beginText();
        cb.setFontAndSize(bflib, 73f);
        cb.setTextMatrix(711.8f, pdfDocument.top()-280);
        cb.showText(wiki_w+"ikipedia");
        cb.endText();
         String main_spine;
         int hc_title_size = 40;
         int spine_y = 0;
        if(coverType == "temp"){
            main_spine = wiki_w+"ikipedia Table of Contents";
            hc_title_size = 34;
        }
        else if(coverType =="output" || coverType == "output-old"){
            main_spine = wiki_w+"ikipedia";
            hc_title_size=40;
            //again add in other parameters here.
        }
        else if(coverType == "contrib"){
            main_spine = wiki_w+"ikipedia Contributor Appendix";
            hc_title_size = 31;
//            spine_y = 698;
        }
        else{
            main_spine = "";
        }
        spine_y = 677;//always start at same position.
        
        int sc_title_size = 35;
        cb.beginText();
        cb.setFontAndSize(bflib,hc_title_size);
        cb.setTextMatrix(50, 595);
        int rotate = 270;
        float x_spine = 544f;
        if(eurotation){
            rotate = 90;
            x_spine = x_spine+17;
            if(coverType == "output"){
                spine_y = 498;
            }
            if(coverType == "contrib"){
                spine_y = 242;
            }
        }
        cb.showTextAligned(0,main_spine,x_spine,spine_y,rotate);
        // ^rotate text.
        cb.endText();
        String[] titleArr = fileName.split("&&&");
        String beginTitle = titleArr[1];
        String endTitle = titleArr[2];
        String volNumber = titleArr[0];
        //****replace the leading zeroes**
        volNumber = volNumber.replaceFirst("^0+(?!$)", "");        

        String lSpineTitle = "";
        String rSpineTitle = "";
        if(beginTitle.length()>3){
            lSpineTitle = beginTitle.substring(0,3);
        }
        else{
            lSpineTitle=beginTitle;
        }
        if(endTitle.length()>3){
            rSpineTitle = endTitle.substring(0,3);
        }
        else{
            rSpineTitle = endTitle;
        }
        PdfPTable tabled = new PdfPTable(1);
        PdfPTable table1 = new PdfPTable(1);
        PdfPTable table2 = new PdfPTable(1);
        PdfPTable table3 = new PdfPTable(1);
        PdfPTable table4 = new PdfPTable(1);

        Paragraph vol_num;
        Paragraph first_abbr;
        Paragraph spine_to;
        Paragraph scnd_abbr;
        Paragraph dis_text;
                
        vol_num = new Paragraph(volNumber, spine_vol_font);
        first_abbr = new Paragraph(fs_abbr.process(lSpineTitle.toUpperCase()));
        scnd_abbr = new Paragraph(fs_abbr.process(rSpineTitle.toUpperCase()));
        spine_to = new Paragraph("TO",spine_to_font);
//        dis_text = new Paragraph("This work is not endorsed by the "+wiki_w+"ikimedia Foundation",dis_font); //for the ISBN
        cb.beginText();
        BaseFont spine_base = bflib;
        cb.setFontAndSize(spine_base, 6);
        cb.showTextAligned(0,"This work is not endorsed by the "+wiki_w+"ikimedia Foundation",444f,105f,90f);
        cb.endText();
//        PdfPCell disclaim;
//        disclaim = new PdfPCell(dis_text);
//        disclaim.setBorderWidth(0f); // if you need to get a bearing on where this is being placed: change this border width to 1 or more.
//        disclaim.setHorizontalAlignment(Element.ALIGN_LEFT);
//        disclaim.setVerticalAlignment(Element.ALIGN_BOTTOM);
//        disclaim.setColspan(1);
//        disclaim.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-173f));
//        tabled.addCell(disclaim);
//        ColumnText columnd = new ColumnText(pdfWriter.getDirectContent());
//        columnd.addElement(tabled);
        
        //llx, lly,urx,ury 
        float llx_hc_volnum = 510.64f;
        float urx_hc_volnum = 620.88f;
        float llx_sc_volnum = 391f;
        float urx_sc_volnum = 506f;
        float lly_sc_volnum = 830f;
//        columnd.setSimpleColumn (llx_hc_volnum-241, pdfDocument.bottom()-122, urx_hc_volnum, pdfDocument.top()-45f);
//        columnd.go();
        
        
        //spine section for volume number
        PdfPCell cell;
        cell = new PdfPCell(vol_num);
        cell.setBorderWidth(0f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setColspan(1);
        cell.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-229.5f));
        table1.addCell(cell);
        ColumnText column = new ColumnText(pdfWriter.getDirectContent());
        column.addElement(table1);
        //llx, lly,urx,ury 
//        float llx_hc_volnum = 510.64f;
//        float urx_hc_volnum = 620.88f;
//        float llx_sc_volnum = 391f;
//        float urx_sc_volnum = 506f;
//        float lly_sc_volnum = 830f;
        column.setSimpleColumn (llx_hc_volnum-14, pdfDocument.bottom()-170, urx_hc_volnum-14, pdfDocument.top()-50f);
        column.go();
        
        //spine section for first abbreviated title
        PdfPCell cell2;    
        cell2 = new PdfPCell(first_abbr);
        cell2.setBorderWidth(0f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell2.setColspan(1);
        cell2.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-182.5f));
        table2.addCell(cell2);
        ColumnText column2 = new ColumnText(pdfWriter.getDirectContent());
        column2.addElement(table2);
        //llx, lly,urx,ury 
        float llx_hc_fa = 492.64f;
        float urx_hc_fa = 611.88f;
        float llx_sc_fa = 395f;
        float urx_sc_fa  = 497f;
        column2.setSimpleColumn (llx_hc_fa, pdfDocument.bottom()-170f, urx_hc_fa, pdfDocument.top()-50f);
        column2.go();
        
        //third table for TO
        PdfPCell cell3;    
        cell3 = new PdfPCell(spine_to);
        cell3.setBorderWidth(0f);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell3.setColspan(1);
        cell3.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-168.5f));
        table3.addCell(cell3);
        ColumnText column3 = new ColumnText(pdfWriter.getDirectContent());
        column3.addElement(table3);
        //llx, lly,urx,ury 
        column3.setSimpleColumn(llx_hc_fa, pdfDocument.bottom()-170f, urx_hc_fa, pdfDocument.top()-50f);
        column3.go();
        
        //4th table for second abbreviated title!
        PdfPCell cell4;    
        cell4 = new PdfPCell(scnd_abbr);
        cell4.setBorderWidth(0f);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell4.setColspan(1);
        cell4.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-150.5f));
        table4.addCell(cell4);
        ColumnText column4 = new ColumnText(pdfWriter.getDirectContent());
        column4.addElement(table4);
        //llx, lly,urx,ury 
        column4.setSimpleColumn (llx_hc_fa, pdfDocument.bottom()-170f, urx_hc_fa, pdfDocument.top()-50f);
        column4.go();
        
        //printing the title underneath Wikipedia on right cover
        
        beginTitle = beginTitle + " — ";
        String mainTitle = beginTitle + endTitle;//title as it appears on the front cover
        Phrase phmt = fs.process(mainTitle);
        PdfPTable tableTitle = new PdfPTable(1);
        tableTitle.setSpacingBefore(400);
        tableTitle.setSplitRows(true);
        tableTitle.setLockedWidth(false);
        Paragraph vol_title;
        vol_title = new Paragraph(phmt);
        
        
        PdfPCell cell_title = new PdfPCell(vol_title);   
        cell_title.setFollowingIndent(40);
        cell_title.setBorderWidth(0f);
        cell_title.setLeading(20f, .3f);
        cell_title.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell_title.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell_title.setColspan(1);
        cell_title.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-144f));
        tableTitle.addCell(cell_title);
        
        ColumnText columnTitle = new ColumnText(pdfWriter.getDirectContent());
        columnTitle.addElement(tableTitle);
        //llx, lly,urx,ury 
        float llx_hc_vol_title = 631f;
        float urx_hc_vol_title= 1060.5f;
        float urx_sc_vol_title = 937f;
        float llx_sc_vol_title  =  531f;
        columnTitle.setSimpleColumn(llx_hc_vol_title, pdfDocument.bottom()+53f, urx_hc_vol_title, 344f);
        columnTitle.go();
        
    }   
    
        public void close() {
            try {
                pdfDocument.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

        
    /**
     *
     * @return
     */
    public int getCurrentTitleNum() {
        return currentTitleNum;
    }

    /**
     *
     * @return
     */
    public int getPagesCount() {
        //return headerFooter.getPageNum();
        return pdfWriter.getCurrentPageNumber();
    }

    /**
     *
     * @return
     */
    public int getTitlesCount() {
        return headerFooter.getLineCount();
    }
    
    
    
    private Document pdfDocument = null;
    private Document spineDoc = null;
    private FontSelector _fontSelector = null;
    private MultiColumnText mct = null;
    private PdfWriter pdfWriter;
    private PdfWriter spineWriter;
    private int currentPageNum = 3;
    private int currentTitleNum = 0;
    private BaseFont bflib = null;
    private TitlesFooter headerFooter;
    private WikiFontSelector wikiFontSelector;
    private WikiFontSelector spineSelector;
    FontSelector fs_abbr = new FontSelector();
    FontSelector fs = new FontSelector();
    

    
}

