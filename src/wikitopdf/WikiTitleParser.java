package wikitopdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import wikitopdf.pdf.PdfTitleWrapper;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfPageEventHelper;
import java.util.ArrayList;
import wikitopdf.pdf.TitlesFooter;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiTitleParser extends PdfPageEventHelper
{
    /**
     *
     * @param inputFileName
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     * @throws ParseException
     */
    public void parseTxt(String inputFileName) throws FileNotFoundException, IOException, DocumentException, ParseException
    {
        //long startDate = new Date().getTime();
        
        FileInputStream inputStream = new FileInputStream(inputFileName);
        BufferedReader bufferReader = null;
        
        bufferReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        String line = "";
        /*
        SQLProcessor sqlProcessor = null;
        try
        {
            sqlProcessor = new SQLProcessor();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(WikiParser.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        
        //start initial/first file up here then readlines to go through the loop.
        int num = 1;
        int pagesCount = 0;
        String firstLine=bufferReader.readLine();//first line will change the title of the file at the end.
        String lastLine="";
        PdfTitleWrapper pdfWrapper = new PdfTitleWrapper(num, pagesCount,firstLine,"");
        String path_to_fonts = "/Users/wiki/repos/printwikipedia/dist/fonts/";
        BaseFont bsFontGlyph = BaseFont.createFont("fonts/msgothic.ttc,0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        BaseFont bsHelv = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        Font fontGlyph = new Font(bsFontGlyph);
        Font helv = new Font(bsHelv);
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
        
        
//        
//        Font arab3 = FontFactory.getFont("arab3", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font hebrew = FontFactory.getFont("hebrew", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        
//        Font cherokee = FontFactory.getFont("cherokee", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font georgian = FontFactory.getFont("georgian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font devanagari = FontFactory.getFont("devanagari", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font nanum = FontFactory.getFont("nanum", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font jap = FontFactory.getFont("jap", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font khmer = FontFactory.getFont("khmer", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font thai = FontFactory.getFont("thai", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font tamil = FontFactory.getFont("tamil", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font armenian = FontFactory.getFont("armenian", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font sinhala = FontFactory.getFont("sinhala", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font ops = FontFactory.getFont("ops", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font bengali = FontFactory.getFont("bengali", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font punj = FontFactory.getFont("punj", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font fsans = FontFactory.getFont("fsans", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font dvs = FontFactory.getFont("dvs", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
//        Font telugu = FontFactory.getFont("telugu", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font cjk = FontFactory.getFont("cjk", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
//        Font ind = FontFactory.getFont("ind", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);                    
//        Font oriya = FontFactory.getFont("oriya", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
//        Font fser = FontFactory.getFont("fser", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 7);
        
        fs.addFont(cardo);
       
        fs.addFont(fontGlyph);
        fs.addFont(arab1);
        fs.addFont(arab2);
        fs.addFont(arab3);
        fs.addFont(hebrew);
//                    fs.addFont(ind);
        
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
        fs.addFont(bengali);
        fs.addFont(punj);
        fs.addFont(fsans);
        fs.addFont(telugu);
        fs.addFont(oriya);
        fs.addFont(fser);
//        fs.addFont(emojiAn);
//        fs.addFont(emojiAp);
//        fs.addFont(garif);
        as.add(arab1);
        as.add(arab2);
        as.add(arab3);
        as.add(hebrew);
        System.out.println("the fonts.");
        System.out.println(arab1);
        System.out.println(arab2);
        System.out.println(arab3);
        System.out.println(hebrew);
        System.out.println(as.size());
        
        if(as.contains(arab3))
            System.out.println("awooo");
        
        
        try
        {
                pdfWrapper.openMultiColumn();
                if(num==1){
                    
                    pdfWrapper.writeTitle(firstLine,fs,as);
                }
                while ((line = bufferReader.readLine()) != null)
                {
                    
                    if(pdfWrapper.getPagesCount()%701==0)//if you're at 701 pages. pdftitlewrapper will have applied the cover(chad) and you can close up here.
                    {
                            //num is the number of files.
                            if(num!=88)//do this once the last volume is known (youwill have to run twice). it will be shorter than other pages and so you don't want to cover the text on the last page.
                            //for our particular settings we ended at volume 88.
                            lastLine = pdfWrapper.coverChad();
                            String newFirst = pdfWrapper.getNewFirst();
                            pdfWrapper.closeMultiColumn();
                            pagesCount += pdfWrapper.getPagesCount();
                            pdfWrapper.close();
                            num++;
                            pdfWrapper = new PdfTitleWrapper(num, pagesCount,newFirst,lastLine); //get started/
                            pdfWrapper.openMultiColumn(); 
                            pdfWrapper.writeTitle(newFirst,fs,as);
                            lastLine = newFirst;
                        
                    }
//                    line = line.replaceAll("[_]"," ");//replace underscores with spaces for TOC pretty.
                    pdfWrapper.writeTitle(line,fs,as);
                    lastLine = line;
                    
                }
            
            pdfWrapper.closeMultiColumn();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {

            pdfWrapper.close();
            bufferReader.close();
        }
    }
                   public FontSelector fs = new FontSelector();
                   public ArrayList as = new ArrayList();

                    
}
