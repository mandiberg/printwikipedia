//pdfpagewrapper

package wikitopdf.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wikitopdf.utils.WikiLogger;
import wikitopdf.utils.WikiSettings;
import wikitopdf.html.WHTMLWorker;
import wikitopdf.html.WikiHtmlConverter;
import wikitopdf.html.WikiStyles;
import wikitopdf.wiki.WikiPage;

/**
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PdfPageWrapper {

    /**
     *
     * @param index
     * @throws DocumentException
     * @throws IOException
     */
    public PdfPageWrapper(int index, int cVolNum, int pageNum, ArrayList previous_objects, String previous_title, int incoming_hard_page_limit) throws DocumentException, IOException { 
        //Read settings.
        //'_' - prefix for for temp file. After stamping file would be renamed
        hard_page_limit = incoming_hard_page_limit;
        System.out.println(previous_objects.toString());
        System.out.println("above is arraylist of objects");
        System.out.println(previous_title + " previous title $$$$");
        
//        System.out.println("i am start page " + pageNum);
        outputFileName = "_" + index + "-" + cVolNum + "-" + pageNum +"-"+ WikiSettings.getInstance().getOutputFileName();
        System.out.println(outputFileName);
        outputFileName = outputFileName.replace("/","\\");
        prefn = "/../copyright/pre"+String.format("%04d", cVolNum)+".pdf";
//      WHTMLWorker.fontGet();//start font thing for the new page.
        as.clear();
        tFontGet();//title/entryheading font
        fontGet();//regular font
        preFontGet();//smaller font for quotes/<pre> tags. -- not sure if this is working or being rendered.
//        System.out.println("\n\n\n\n\nturkey");
//        System.out.println(previous_objects.size());
//        System.out.println("\n\n\n\n\nturkey");
        
        pdfDocument = new Document(new Rectangle(432, 648));//6" x 9"

        preDoc = new Document(new Rectangle(432,648));
        
        pdfDocument.setMargins(67, 47.5f, -551, 49.5f); //old margins w/error.
//        pdfDocument.setMargins(66.3f, 47f, 5.5f, 62.5f);//toc margins 
        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream( WikiSettings.getInstance().getOutputFolder() +
                            "/" + outputFileName));
        preWriter = PdfWriter.getInstance(preDoc,
                new FileOutputStream( WikiSettings.getInstance().getOutputFolder() +
                            "/" + prefn));

        
        header = new PageHeaderEvent(pageNum,pdfDocument);
        pdfWriter.setPageEvent(header);
        
        
        pdfDocument.open();

        _wikiFontSelector = new WikiFontSelector();
        
        
        pdfDocument.setMarginMirroring(true);//for alternating margins for alternate pages

          addPrologue(cVolNum, preDoc, preWriter); //creates copyright two pages.
        
        openMultiColumn();
        addPreviousObjects(previous_title,previous_objects);
        
    }

    public void addPreviousObjects(String last_title, ArrayList previous_objects) throws DocumentException    {
        if(previous_objects.size()>0){

            header.setCurrentTitle(last_title);
            writeTitle(last_title+" (CONT.)");
            for (int k = 0; k < previous_objects.size(); ++k) {
            
                Element element = (Element) previous_objects.get(k);
    //            System.out.println(element.toString());
                //add objects

                if (mct.isOverflow()) {
                    mct.nextColumn();
                    pdfDocument.newPage();
                }

                    mct.addElement(element);


                    pdfDocument.add(mct);


        }
        
        }
     }
    
    public void openMultiColumn() {

        mct = new MultiColumnText(600);
        int columnCount = 3;
        float space = (float) 8;
        float columnWidth = (float) 103;
        float left = 67;
        float right = left + columnWidth;

        mct.addRegularColumns(pdfDocument.left(), pdfDocument.right(), 6f, 3);

        //First page hack
        for (int i = 0; i < 38; i++) {//same as the TOC -- for some reason the first entry always wants to start at the top of the page. This moves it down. Should be a better fix.
            try {
                Phrase ph = _wikiFontSelector.getTitleFontSelector().process("\n");
                mct.addElement(ph);
                pdfDocument.add(mct);
            } catch (Exception ex) {
                WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
            }
        }
    }
    
    public final void addPrologue(int cVolNum, Document docu, PdfWriter writ) throws DocumentException {
        //adds copyright document.
        if(docu.equals(preDoc)){
            preDoc.open();
            preDoc.setMarginMirroring(true);
        }
        PdfContentByte cb = writ.getDirectContent();
        BaseFont times = null;
        try {
            _wikiFontSelector.getTitleFontSelector().process("");
            times = _wikiFontSelector.getCommonFont().getBaseFont();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            bflib = BaseFont.createFont("fonts/LinLibertine_Rah.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (IOException ex) {
            Logger.getLogger(PdfPageWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        int c = 57391;
        String wiki_w = Character.toString((char)c);

        //wikipedia on the first inside page.--right facing
        cb.beginText();
        cb.setFontAndSize(bflib, 42);
        cb.setTextMatrix(docu.right() - 182, 425);
        cb.showText(wiki_w+"ikipedia");
        cb.endText();
        PdfPTable tocTable = new PdfPTable(1);
        
        try {
            //setting volume number position and adding to page.
            _wikiFontSelector.getTitleFontSelector().process("");
            times = _wikiFontSelector.getCommonFont().getBaseFont();
            Font pght = new Font(bflib,16);
            Paragraph pgh = new Paragraph("\nVolume "+String.valueOf(cVolNum),pght);
            PdfPCell cell = new PdfPCell(pgh);
            cell.setBorderWidth(0f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            tocTable.addCell(cell);
            ColumnText column = new ColumnText(writ.getDirectContent());
            column.addElement(tocTable);
            column.setSimpleColumn(docu.left()+15, docu.bottom()+20, docu.right()+27.5f, docu.bottom()-100);
            column.go();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        docu.newPage();//get second page for the copyright text
//        jknewPage(docu);

        String copyrightText = "CC BY-SA 3.0 2016, Wikipedia contributors; see Appendix for a complete list of contributors. Please see http://creativecommons.org/licenses/by-sa/3.0/ for full license.\r\r"+
                "Edited, compiled and designed by Michael Mandiberg (User:Theredproject).\r\r"+ 
                "This work is legally categorized as an artistic work. As such, this qualifies for trademark use under clause 3.6.3 (Artistic, scientific, literary, political, and other non-commercial uses) as denoted at wikimediafoundation.org/wiki/Trademark_policy\r\r"+
                "Wikipedia is a trademark of the Wikimedia Foundation and is used with the permission of the Wikimedia Foundation. This work is not endorsed by or affiliated with the Wikimedia Foundation.\r\r"+
                "Cover set in Linux Libertine. Book set in Cardo, with the following 36 typefaces added to handle the many languages contained within: Alef, Amiri, Android Emoji, Bitstream CyberCJK, Casy EA, cwTeXFangSong, cwTeXHei, cwTeXKai, cwTeXMing, cwTeXYen, DejaVu Sans, Droid Arabic Kufi, FreeSans, FreeSerif, GurbaniAkharSlim, IndUni-N, Junicode, Lohit Gujarati, Lohit Oriya, MAC C Times, MS Gothic, NanumGothic, Noto Kufi Arabic, Noto Sans, Noto Sans Bengali, Noto Sans Cherokee, Noto Sans Devanagari, Noto Sans Georgian, Noto Sans Japanese, Noto Sans Sinhala, Noto Sans Tamil UI, Noto Sans Telugu, Noto Sans Thai, Noto Serif Armenian, Open Sans, Roboto.\r\r"+
                "Produced with support from Eyebeam, The Banff Centre, the City University of New York, and Lulu.com, The Wikimedia Foundation, and Denny Gallery. Designed and built with assistance from Denis Lunev, Jonathan Kirtharan, Kenny Lozowski, Patrick Davison, Colin Elliot and Danara Sarıoğlu.\r\r"+
                "PrintWikipedia.com\r\rGitHub.com/mandiberg/printwikipedia\r\rPrinted by Lulu.com";
        PdfPTable cpTable = new PdfPTable(1);
        try { //setting copyright text and adding to page
            times = _wikiFontSelector.getCommonFont().getBaseFont();
            Font cpt = new Font(bflib,8);
            Paragraph cpp = new Paragraph(copyrightText,cpt);
            PdfPCell cell2 = new PdfPCell(cpp);
            cell2.setLeading(10,0);
            cell2.setBorderWidth(0f);
            cell2.setHorizontalAlignment(Element.ALIGN_TOP);
            cell2.setVerticalAlignment(Element.ALIGN_LEFT);
            cell2.setColspan(1);
            cpTable.addCell(cell2);
            ColumnText column2 = new ColumnText(writ.getDirectContent());
            column2.addElement(cpTable);
            column2.setSimpleColumn (docu.left()-30, 10, docu.right(), docu.top()-15);
            column2.go();
            
            docu.newPage();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        if(docu.equals(preDoc)){
            preDoc.close();
            File savedithink = new File(prefn);
        }
        
    }

    /**
     *
     * @param page
     */
    public void tFontGet(){
        String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
        FontFactory.register(path_to_fonts+"Cardo_no_hebrew.ttf","cardo");
        FontFactory.register(path_to_fonts+"msgothic.tcc.0","fontGlyph");
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
        FontFactory.register(path_to_fonts+"AppleColorEmoji.ttf","emojiAp");
        FontFactory.register(path_to_fonts+"android-emoji.ttf","emojiAn");
        FontFactory.register(path_to_fonts+"casy_ea.ttf","garif");
        
        

        int font_size = 13;
        Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fontGlyph = FontFactory.getFont("fontGlyph", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAn = FontFactory.getFont("emojiAn", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAp = FontFactory.getFont("emojiAp", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font garif = FontFactory.getFont("garif", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        

        tfs.addFont(cardo);
        tfs.addFont(fontGlyph);
        //fs.addFont(dvs);
        tfs.addFont(fser);
        tfs.addFont(cjk);
        tfs.addFont(arab1);
        tfs.addFont(arab2);
        tfs.addFont(arab3);
//                    fs.addFont(ind);
        tfs.addFont(hebrew);
        tfs.addFont(russ);
        tfs.addFont(armenian);
        tfs.addFont(chinese1);
        tfs.addFont(chinese2);
        tfs.addFont(chinese3);
        tfs.addFont(chinese4);
        tfs.addFont(chinese5);
        tfs.addFont(chinese6);
        
        tfs.addFont(cherokee);
        tfs.addFont(georgian);  
        tfs.addFont(devanagari);
        tfs.addFont(nanum);
        tfs.addFont(jap);
        tfs.addFont(khmer);
        tfs.addFont(thai);
        tfs.addFont(tamil);
        tfs.addFont(ops);
//        fs.addFont(helv);
        tfs.addFont(roboto);
        tfs.addFont(sinhala);
        tfs.addFont(bengali);
        tfs.addFont(punj);
        tfs.addFont(fsans);
        tfs.addFont(telugu);
        tfs.addFont(oriya);
        tfs.addFont(fser);
//        tfs.addFont(emojiAn);
//        tfs.addFont(emojiAp);
        tfs.addFont(garif);
        if(!as.contains(arab1)){
            as.add(arab1);
            as.add(arab2);
            as.add(arab3);
            as.add(hebrew);
        }
    }
    public void preFontGet(){
        String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
        FontFactory.register(path_to_fonts+"Cardo_no_hebrew.ttf","cardo");
        FontFactory.register(path_to_fonts+"msgothic.tcc.0","fontGlyph");
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
        FontFactory.register(path_to_fonts+"AppleColorEmoji.ttf","emojiAp");
        FontFactory.register(path_to_fonts+"android-emoji.ttf","emojiAn");
        FontFactory.register(path_to_fonts+"casy_ea.ttf","garif");



        int font_size = 6;
        Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fontGlyph = FontFactory.getFont("fontGlyph", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAn = FontFactory.getFont("emojiAn", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAp = FontFactory.getFont("emojiAp", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font garif = FontFactory.getFont("garif", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);

        pfs.addFont(cardo);
        pfs.addFont(fontGlyph);
        //fs.addFont(dvs);
        pfs.addFont(fser);
        pfs.addFont(cjk);
        pfs.addFont(arab1);
        pfs.addFont(arab2);
        pfs.addFont(arab3);
//                    fs.addFont(ind);
        pfs.addFont(hebrew);
        pfs.addFont(russ);
        pfs.addFont(armenian);
        pfs.addFont(chinese1);
        pfs.addFont(chinese2);
        pfs.addFont(chinese3);
        pfs.addFont(chinese4);
        pfs.addFont(chinese5);
        pfs.addFont(chinese6);
        
        pfs.addFont(cherokee);
        pfs.addFont(georgian);  
        pfs.addFont(devanagari);
        pfs.addFont(nanum);
        pfs.addFont(jap);
        pfs.addFont(khmer);
        pfs.addFont(thai);
        pfs.addFont(tamil);
        pfs.addFont(ops);
//        fs.addFont(helv);
        pfs.addFont(roboto);
        pfs.addFont(sinhala);
        pfs.addFont(bengali);
        pfs.addFont(punj);
        pfs.addFont(fsans);
        pfs.addFont(telugu);
        pfs.addFont(oriya);
        pfs.addFont(fser);
//        pfs.addFont(emojiAn);
//        pfs.addFont(emojiAp);
        pfs.addFont(garif);
        
        if(!as.contains(arab1)){
            as.add(arab1);
            as.add(arab2);
            as.add(arab3);
            as.add(hebrew);
        }
        
    }
            public void fontGet() throws DocumentException, IOException{
        String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
        FontFactory.register(path_to_fonts+"Cardo_no_hebrew.ttf","cardo");
        FontFactory.register(path_to_fonts+"msgothic.tcc.0","fontGlyph");
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
        FontFactory.register(path_to_fonts+"AppleColorEmoji.ttf","emojiAp");
        FontFactory.register(path_to_fonts+"android-emoji.ttf","emojiAn");
        FontFactory.register(path_to_fonts+"casy_ea.ttf","garif");
        System.out.println(FontFactory.getRegisteredFonts());

        int font_size = 8;
        
        
        BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font helv = new Font(bsHelv);
        Font cardo = FontFactory.getFont("cardo", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font roboto = FontFactory.getFont("roboto", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font russ = FontFactory.getFont("russ", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese1 = FontFactory.getFont("chinese1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese2 = FontFactory.getFont("chinese2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese3 = FontFactory.getFont("chinese3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese4 = FontFactory.getFont("chinese4", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese5 = FontFactory.getFont("chinese5", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font chinese6 = FontFactory.getFont("chinese6", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab1 = FontFactory.getFont("arab1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab2 = FontFactory.getFont("arab2", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);                    
        Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font fontGlyph = FontFactory.getFont("fontGlyph", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAn = FontFactory.getFont("emojiAn", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font emojiAp = FontFactory.getFont("emojiAp", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        Font garif = FontFactory.getFont("garif", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, font_size);
        

        fs.addFont(cardo);
        fs.addFont(arab1);
        fs.addFont(arab2);
        fs.addFont(arab3);
        //fs.addFont(dvs);
        fs.addFont(fser);
        fs.addFont(cjk);
        
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
        fs.addFont(helv);
        fs.addFont(roboto);
        fs.addFont(sinhala);
        fs.addFont(fontGlyph);
        fs.addFont(bengali);
        fs.addFont(punj);
        fs.addFont(fsans);
        fs.addFont(telugu);
        fs.addFont(oriya);
        fs.addFont(fser);
//        fs.addFont(emojiAn);
//        fs.addFont(emojiAp);
        fs.addFont(garif);
        
        System.out.println(arab1);
        System.out.println(arab2);
        System.out.println(arab3);
        System.out.println(hebrew);
        if(!as.contains(arab1)){
                as.add(arab1);
                as.add(arab2);
                as.add(arab3);
                as.add(hebrew);
        }

    }
    
    public void writePage(WikiPage page) {
        currentTitle = page.getTitle();
        currentArticleID = page.getId();
        String x = page.getRevision().getText().toLowerCase();
        if(x.contains("wiktionary redirect"))//breaks on wiktionary redirect. just ommit it. COULD REMOVE IF AFTER UPDATING wiki parse library!
            return;
        System.out.println("curren title" + currentTitle);
        writeTitle(currentTitle);
        writeText(page.getRevision().getText());
//        System.out.println("i did it.");
    }
    public PdfPTable arabicHeader(Phrase ph, PdfWriter pdfWriter){
        Paragraph pr = new Paragraph(ph);
        pr.setSpacingBefore(8);//changes spacing before title
        pr.setSpacingAfter(4);//changes spacing after title.
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
    //Write title of article to document
    private void writeTitle(String line) {
        PdfPTable pp = null;
        Phrase ph = null;
        Paragraph pr = null;
        try {
             if(pdfWriter.getPageNumber() >= hard_page_limit )
                return;
            line = line.replaceAll("_", " ").toUpperCase();
            header.setCurrentTitle(line);

            ph = tfs.process(line);
            

            ph.setLeading(14);//changes leading between spaces in titles

            if(isRTL(as,ph)){
                pp = arabicHeader(ph,pdfWriter);
            }
            pr = new Paragraph(ph);
//            System.out.println(pr.getFont().getSize()+" paragraph size");
            pr.setSpacingBefore(8);//changes spacing before title
            pr.setSpacingAfter(4);//changes spacing after title.

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }

            
            if(pp!=null){
             System.out.println("this is not null pp");
             mct.addElement(pp);   
            }
            else{
                mct.addElement(pr);
            }
            pdfDocument.add(mct);
        } 
        catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
            ex.printStackTrace(System.out);
            System.out.println("AWOOGA");
        }
    }

    //Write article text using defined styles
    private void writeText(String text) {
        boolean append_refs = false;
        boolean is_refs = false;
        try {
            //review the below removed/replaced items to see if you want to include. -- no good or easy way to add gallery though. probably don't want that.
            text = text.replaceAll("<gallery[\\s\\S]*?</gallery>",""); //no gallery
            text = text.replaceAll("\\s#########.+","");
            text = text.replaceAll("\\s\\*\\*\\*\\*\\*\\*\\*\\*\\*.+","");
            text = text.replaceAll("(?i)\\[\\[Categorie.+",""); //remove kategorie at the end.
            text = text.replaceAll("(?i)\\[\\[Datei.+","");
            text = text.replaceAll("(?i)\\[\\[bild.+","");
            text = text.replaceAll("(?i)\\[\\[file.+","");
            text = text.replaceAll("(?s)(?i)==\\s*zie\\s*ook\\s*==\\s(.+?)==", "==");
            text = text.replaceAll("(?s)(?i)==\\s*literatuur\\s*==\\s(.+?)==", "==");
            text = text.replaceAll("(?s)(?i)==\\s*externe\\s*link\\s*==\\s(.+?)==", "==");
            text = text.replaceAll("(?s)(?i)==\\s*quellen\\s*==\\s(.+?)==", "==");
            String strPatternDE = "#WEITERLEITUNG\\s*\\[\\[(.*)\\]\\]";
            String strPatternENG = "#redirect\\s*\\[\\[(.*)\\]\\]";
            String strPatternNL = "#DOORVERWIJZING\\s*\\[\\[(.*)\\]\\]";
            Pattern pde = Pattern.compile(strPatternDE, Pattern.CASE_INSENSITIVE);
            Matcher mde = pde.matcher(text);
            Pattern peng = Pattern.compile(strPatternENG, Pattern.CASE_INSENSITIVE);
            Matcher meng = peng.matcher(text);
            Pattern pnl = Pattern.compile(strPatternNL, Pattern.CASE_INSENSITIVE);
            Matcher mnl = pnl.matcher(text);

            if (mde.find()) {
                text = "Zie: " + mde.group(1);
            }
            else if(meng.find()){
                text = "Zie: " + meng.group(1);
            }
            else if(mnl.find()){
                text = "Zie: " + mnl.group(1);
            }

            
//            System.out.println(text);
//            System.out.println("\n\n\n\n\n");
            
            
            

            String html = WikiHtmlConverter.convertToHtml(text);
            
            
            if(WikiHtmlConverter.getModelReferences() == null){
                is_refs=false;
            }
            else{
                is_refs=true;
            }
            
//            System.out.println(html);


            if(!is_refs)
                html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"EINZELNACHWEISE\">EINZELNACHWEISE</SPAN></H\\d>).*","<<b>_____________________</b><br /><br />");
            html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"EXTERNE_LINK(s?)\">EXTERNE LINK(s?)</SPAN></H\\d>).*","<b>_____________________</b><br /><br />");
////            html = html.replaceAll("(?s)(\\s+<a id=\"Footnotes\" name=\"Footnotes\"></a><H2>FOOTNOTES</H2>).*","<b>_____________________</b><br /><br />");
            html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"ZIE_OOK\">ZIE OOK</SPAN></H\\d>).*","<b>_____________________</b><br /><br />");
//            
//            html = html.replaceAll("(?s)(\\s+<a id=\"Bibliography\" name=\"Bibliography\"></a><H2>BIBLIOGRAPHY</H2>).*","<b>_____________________</b><br /><br />");
            html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"LITERATUUR\">LITERATUUR</SPAN></H\\d>).*","<b>_____________________</b><br /><br />");
            html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"QUELLEN\">QUELLEN</SPAN></H\\d>).*","<b>_____________________</b><br /><br />");
            html = html.replaceAll("(?s)(?i)((\\s*?)<H\\d><SPAN CLASS=\"MW-HEADLINE\" ID=\"VOETNOTEN\">VOETNOTEN</SPAN></H\\d>).*","<b>_____________________</b><br /><br />");
           
            
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Zie_ook\">Zie ook</a>\n</li>","");
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Literatuur\">Literatuur</a>\n</li>", "");
//            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Anmerkungen\">Anmerkungen</a>\n</li>", "");
            if(!is_refs)
                html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Einzelnachweise\">Einzelnachweise</a>\n</li>", "");
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Externe_link(s?)\">Externe link(s?)</a>\n</li>", "");
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Bronvermelding\">Bronvermelding</a>\n</li>", "");
            
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Quellen\">Quellen</a>\n</li>","");
            html = html.replaceAll("<li class=\"toclevel-\\d\"><a href=\"#Voetnoten\">Voetnoten</a>\n</li>","");


            convertHtml2Pdf(html);
            // text has been made into pdf.
            
        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }

    //Convert html to pdf objects, apply styles
    private void convertHtml2Pdf(String htmlSource) throws DocumentException, IOException {
        StringReader reader = new StringReader(htmlSource);
        StyleSheet styles = WikiStyles.getStyles();
        ArrayList objects;
        
        //parse that text!
        objects = WHTMLWorker.parseToList(reader, styles, pdfWriter);
        
        for (int k = 0; k < objects.size(); ++k) {
            
            Element element = (Element) objects.get(k);
//            System.out.println(element.toString());
            //add objects
            
            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }

            try {
                System.out.println("i am in pagewrapper getpn   " + pdfWriter.getPageNumber() );
                if(pdfWriter.getPageNumber() >= hard_page_limit ){
//                    pdfWriter.setPageEmpty(true);
//                    mct.resetCurrentColumn();
                    System.out.println("in converthtmlwo2nbdfud return");

                    System.out.println(pdfWriter.getVerticalPosition(true));
                    for (int y=0; y < objects.size()-k; y++){
                        remaining_objects.add(objects.get(k+y));
                        Element d = (Element) objects.get(k+y);
                        System.out.println(d.toString());
                        
                    }
                    
                    return;
                }
                
                mct.addElement(element);
                
                
                
                
                pdfDocument.add(mct);
                
            }
            catch(Exception e) {
//                System.out.println("ELEM CAUSING ERROR: \n\n\n");
//                System.out.println(element);
                e.printStackTrace();
}
        }
    }

    

    /**
     *
     */
    public void closeColumn() {
        try {
            pdfDocument.add(mct);
        } catch (DocumentException ex) {
            WikiLogger.getLogger().severe(ex.getMessage());
            //throw new Exception(ex);
        }
    }

    /**
     *
     * @return
     */
    public String getOutputFileName() {
        return outputFileName;
    }

    /**
     *
     * @return
     */
    public int getPageNumb() {
        return pdfWriter.getCurrentPageNumber() - 1;
    }

    /**
     *
     * @return
     */
    public String getCurrentTitle(){
        return currentTitle;
    }

    /**
     *
     */
    public void close() throws DocumentException, IOException {
        System.out.println("i close!");
        
        pdfDocument.close();
        
    }
    public boolean checkOpen(){
        return pdfDocument.isOpen();
    }

    /**
     *
     * @return
     */
    public int getCurrentArticleID() {
        return currentArticleID;
    }

    private PageHeaderEvent header = null;
    private Document pdfDocument = null;
    private Document preDoc = null;
    private PdfWriter pdfWriter;
    private PdfWriter preWriter;
    private WikiFontSelector _wikiFontSelector = null;
    public static FontSelector fs = new FontSelector();
    private FontSelector tfs = new FontSelector();
    public static FontSelector pfs = new FontSelector();
    private MultiColumnText mct = null;
    private String outputFileName = "";
    private String prefn = "";
    private String currentTitle = "";
    private int currentArticleID;
    public ArrayList remaining_objects = new ArrayList();
    private BaseFont bflib;
    public static ArrayList as = new ArrayList();
    private int hard_page_limit;
    
}   
