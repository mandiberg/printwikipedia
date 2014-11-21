package wikitopdf.pdf;

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
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
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
    public PdfPageWrapper(int index) throws DocumentException, IOException {
        //Read settings.
        //'_' - prefix for for temp file. After stamping file would be renamed
        outputFileName = "_" + index + WikiSettings.getInstance().getOutputFileName();

        //72 pixels per inch
        pdfDocument = new Document(new Rectangle(432, 648));//6" x 9"
        //pdfDocument = new Document(new Rectangle(1918, 1018)); //

        pdfDocument.setMargins(27, 67.5f, -551, 49.5f);
        pdfDocument.setMarginMirroring(true);

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream( WikiSettings.getInstance().getOutputFolder() +
                            "/" + outputFileName));

        header = new PageHeaderEvent(0);
        pdfWriter.setPageEvent(header);

        pdfDocument.open();
        
        pdfDocument.setMarginMirroring(true);
        _wikiFontSelector = new WikiFontSelector();
        pdfDocument.add(new Paragraph(""));
        pdfDocument.newPage();
        fontGet();//start font thing for the new page.

        //PdfContentByte cb = pdfWriter.getDirectContent();
        //ColumnText ct = new ColumnText(cb);
        openMultiColumn();
    }

    /**
     *
     * @param page
     */
    public void fontGet(){
        String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
        FontFactory.register(path_to_fonts+"Cardo104s.ttf","cardo");
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
        Font fontGlyph = FontFactory.getFont("fontGlyph", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);

        fs.addFont(cardo);
        fs.addFont(fontGlyph);
        //fs.addFont(dvs);
        fs.addFont(fser);
        fs.addFont(cjk);
        fs.addFont(russ);
        fs.addFont(armenian);
        fs.addFont(chinese1);
        fs.addFont(chinese2);
        fs.addFont(chinese3);
        fs.addFont(chinese4);
        fs.addFont(chinese5);
        fs.addFont(chinese6);
        fs.addFont(arab1);
        fs.addFont(arab2);
        fs.addFont(arab3);
//                    fs.addFont(ind);
        fs.addFont(hebrew);
        fs.addFont(cherokee);
        fs.addFont(georgian);
        fs.addFont(devanagari);
        fs.addFont(nanum);
        fs.addFont(jap);
        fs.addFont(khmer);
        fs.addFont(thai);
        fs.addFont(tamil);
        fs.addFont(ops);
//        fs.addFont(helv);
        fs.addFont(roboto);
        fs.addFont(sinhala);
        fs.addFont(bengali);
        fs.addFont(punj);
        fs.addFont(fsans);
        fs.addFont(telugu);
        fs.addFont(oriya);
        fs.addFont(fser);
    }
    public void writePage(WikiPage page) {
        currentTitle = page.getTitle();
        currentArticleID = page.getId();
        //System.out.println("Article ID is" + currentArticleID);
        writeTitle(currentTitle);
        writeText(page.getRevision().getText(),fs);
    }


    //Write title of article to document
    //Double paragraph helvetica problem is here other is in WikiHtmlConverter.java
    private void writeTitle(String line) {
        Phrase ph;
        try {
            line = line.replaceAll("_", " ").toUpperCase();
            header.setCurrentTitle(line);
            ph = _wikiFontSelector.getTitleFontSelector().process(line);
            ph.setLeading(8);
            Paragraph pr = new Paragraph(ph);
            pr.setSpacingBefore(2);
            pr.setSpacingAfter(30);

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }
            if (pdfWriter.getCurrentPageNumber() > 1) {
                //Double paragraph helvetica problem is here other is in WikiHtmlConverter.java
//                mct.addElement(new Phrase("\n"));
            }

            mct.addElement(pr);
            pdfDocument.add(mct);
        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
        }
    }

    //Write article text using defined styles
    private void writeText(String text, FontSelector fs) {
        try {
            // text is in BBCode (This is bliki)
            String html = WikiHtmlConverter.convertToHtml(text);
            System.out.println(html + "\n ^^^ html text");
            // text is now html (This is doing iText work)
            convertHtml2Pdf(html);
            // text has been made into pdf.

        } catch (Exception ex) {
            WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
        }
    }

    //Convert html to pdf objects, apply styles
    private void convertHtml2Pdf(String htmlSource) throws DocumentException, IOException {
        StringReader reader = new StringReader(htmlSource);
        StyleSheet styles = WikiStyles.getStyles();
        ArrayList objects;
        objects = WHTMLWorker.parseToList(reader, styles);
        
        for (int k = 0; k < objects.size(); ++k) {

            Element element = (Element) objects.get(k);

            if (mct.isOverflow()) {
                mct.nextColumn();
                pdfDocument.newPage();
            }
            //THIS SHOULD BE CHANGED TO BE PROCESSED WITH THE FONT STACK!!!
            //
            //
            //
            
//            System.out.println(element.toString()+" THIS IS THE ELEMENT");
//            
//            String temp_elem = element.toString();
//            temp_elem = temp_elem.substring(1, temp_elem.length()-1);
//            Phrase ph = fs.process(temp_elem);
            
//                ph.setLeading(8);
//                Paragraph pr = new Paragraph(ph);
//                System.out.println(pr.toString() + " this isthe paragraph");
                mct.addElement(element);
            pdfDocument.add(mct);
        }
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(600);
        int columnCount = 3;
        float gap = (float) 27;
        float space = (float) 8;
        float columnWidth = (float) 107;
        float left = gap;
        float right = left + columnWidth;

        for (int i = 0; i < columnCount; i++) {
            //System.out.println("left:" + left + " right:" + right);
            mct.addSimpleColumn(left, right);
            left = right + space;
            right = left + columnWidth;
        }

        //First page hack
        for (int i = 0; i < 38; i++) {
            try {
                Phrase ph = _wikiFontSelector.getTitleFontSelector().process("\n");

                mct.addElement(ph);
                pdfDocument.add(mct);
            } catch (Exception ex) {
                WikiLogger.getLogger().severe(currentTitle + " - Error: " + ex.getMessage());
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
    public void close() {
        pdfDocument.close();
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
    private PdfWriter pdfWriter;
    private WikiFontSelector _wikiFontSelector = null;
    private FontSelector fs = new FontSelector();
    private MultiColumnText mct = null;
    private String outputFileName = "";
    private String currentTitle = "";
    private int currentArticleID;
}
