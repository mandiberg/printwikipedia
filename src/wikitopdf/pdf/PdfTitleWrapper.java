package wikitopdf.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PdfTitleWrapper {

    private ColumnText ct;
    private int column = 0;
    private int status = 0;
    float[] right = {70, 320};
    float[] left = {300, 550};
    public static String lastLine= "";
    public static String firstLine ="";
    public static int num=0;
    public boolean pageFull = false;

    /**
     *
     * @param num
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     */
 
    public PdfTitleWrapper(int num, int startPage,String firstLine, String lastLine) throws FileNotFoundException, DocumentException, IOException {
        //Read settings
        int curPage = startPage;
        pageFull = false;
        PdfTitleWrapper.lastLine = lastLine;
        if(PdfTitleWrapper.lastLine!=""){
            PdfTitleWrapper.lastLine = PdfTitleWrapper.lastLine.replace("/", "\\");
            System.out.println("temp/"+String.format("%04d",PdfTitleWrapper.num)+"&&&"+PdfTitleWrapper.firstLine+"&&&"+PdfTitleWrapper.lastLine+"&&&.pdf");
            new File("temp/tocVol-"+PdfTitleWrapper.num+"-"+PdfTitleWrapper.firstLine+".pdf").renameTo(new File(
                    "temp/"+String.format("%04d",PdfTitleWrapper.num)+"&&&"+PdfTitleWrapper.firstLine+"&&&"+PdfTitleWrapper.lastLine+"&&&.pdf"));
        }
        firstLine = firstLine.replace("[_]"," ");
        firstLine = firstLine.replace("/","\\");//gets confused on filenames with / as is supposed to be directory separator
        String outputFileName = "temp/tocVol-" + num + "-"+firstLine+".pdf";
        String prefn = "temp/pre"+String.format("%04d",(num))+".pdf";
        pdfDocument = new Document(new Rectangle(432, 648));
        preToc = new Document(new Rectangle(432,648));

        pdfDocument.setMargins(66.3f, 47f, 5.5f, 62.5f);
        preToc.setMargins(66.3f, 47f, 5.5f, 62.5f);
        
        

        

        pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream(outputFileName));
        preWrite = PdfWriter.getInstance(preToc, new FileOutputStream(prefn));//for copyright page.
        
        headerFooter = new TitlesFooter(startPage);
        pdfWriter.setPageEvent(headerFooter);
        pdfDocument.open();
        pdfDocument.setMarginMirroring(true);
        wikiFontSelector = new WikiFontSelector();
        preToc.open();
        addPrologue();
        preToc.close();
        jknewPage(pdfDocument);
        
        PdfTitleWrapper.firstLine = firstLine;
        PdfTitleWrapper.num=num;

        PdfContentByte cb = pdfWriter.getDirectContent();
        ct = new ColumnText(cb);

        
    }
    
    public boolean isRTL(ArrayList as, Phrase ph){
        ArrayList chunks = ph.getChunks();
                for(int i=0; i < chunks.size(); i++){
                    Chunk lilchunk = (Chunk) chunks.get(i);
//                    System.out.println(chunks.get(i).toString());
                    String[][] ane = lilchunk.getFont().getBaseFont().getAllNameEntries();
//                    System.out.println(lilchunk.getFont());
                    if(as.contains(lilchunk.getFont())){
//                        System.out.println(lilchunk.getFont());
//                        System.out.println()
                        return true;
                        
                    }
                }
        return false;
    }

    /**
     *
     * @param line
     * @throws DocumentException
     */

    public void writeTitle(String line,FontSelector fs, ArrayList as) throws DocumentException { 
        
        try {
            if(pdfWriter.getCurrentPageNumber()> 420 && pdfWriter.getCurrentPageNumber() < 430){
//                System.out.println(line);
            }
                
                int breaker = 0;//shortens title based on length of string so as not to go over the page headers.
                String[] str_len_check = line.split(" ");
                
                if(line.toUpperCase() == line)
                    breaker = 72;
                else
                    breaker = 72;
                if(line.length()>breaker){
                    line = line.substring(0, breaker)+"...";

                }
                PdfPTable table = new PdfPTable(1);
                PdfPCell celly = new PdfPCell();
                
                Phrase ph = fs.process(line);
                boolean run_right = isRTL(as,ph);
                
                
                ph.setLeading(8);
                Paragraph pr = new Paragraph(ph);
                pr.setAlignment("Left");
                pr.setFirstLineIndent(-5);
                pr.setIndentationLeft(5);
                
                celly.addElement(pr);
                celly.setBorderColor(Color.white);
                celly.setBorderWidth(0);
                celly.setPadding(0);
                if(run_right){
                    celly.setRunDirection(pdfWriter.RUN_DIRECTION_RTL);
                }
                
                table.setSpacingAfter(0);
                table.setSpacingBefore(0);
                table.addCell(celly);
                
            mct.addElement(table);
            boolean add = pdfDocument.add(mct);
            if(add == false){
//                System.out.println(line);
//                System.out.println(line);
//                System.out.println(line);
//                System.out.println(line);
//                System.out.println(line);
                System.exit(1);
                
            }
            headerFooter.setCurrentLine(line);//add to linelist to be handled by TitlesFooter.java           

            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        currentTitleNum++;
    }


   
    public void close() {
        try {
            pdfDocument.close();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public String coverChad(){//meant to just cover the hanging last title with rect (on page 701) to maintain proper headers. no better way to do this.
         PdfContentByte ecanvas = pdfWriter.getDirectContent();
         ecanvas.saveState();
        ecanvas.rectangle(10, 10, pdfDocument.right()+20, pdfDocument.top()-10);
        ecanvas.setColorFill(Color.WHITE);
        ecanvas.fill();

        int lc = headerFooter.getLineCount();
        String newLastLine = headerFooter.lineList.get(lc-2);
        ecanvas.restoreState();
        return newLastLine;
        
    }
    public String getNewFirst(){
        int lc = headerFooter.getLineCount();
        String newFirstLine = headerFooter.lineList.get(lc-1);
        return newFirstLine;
    }
    /*
    Prologue at the second page
     */
    /**
     *
     * @throws DocumentException
     */
    public final void jknewPage(Document doc) throws DocumentException{
      doc.add(new Paragraph(""));
      doc.newPage();
      doc.add(new Paragraph(""));
    }
    public final void addPrologue() throws DocumentException, IOException {
        PdfContentByte cb = preWrite.getDirectContent();
        BaseFont times = null;
        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bflib = BaseFont.createFont("fonts/LinLibertine_Rah.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font lib = new Font(bflib);

        cb.beginText();
        
        cb.setFontAndSize(bflib,42);
        cb.setTextMatrix(preToc.right() - 182, 425);
        int c = 57391;
        String s = Character.toString((char)c);
        cb.showText(s+"ikipedia");
        cb.endText();
        PdfPTable tocTable = new PdfPTable(1);

        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
            Font pght = new Font(bflib,16);
            Paragraph pgh = new Paragraph("Contributor Appendix\n\nVolume "+String.valueOf((PdfTitleWrapper.num+1)),pght);
            PdfPCell cell = new PdfPCell(pgh);
            cell.setBorderWidth(0f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            tocTable.addCell(cell);
            ColumnText column = new ColumnText(preWrite.getDirectContent());
            column.addElement(tocTable);
            column.setSimpleColumn (preToc.left()+20, preToc.bottom()+20, preToc.right()+27.5f, preToc.bottom()-100);
            column.go();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        preToc.newPage();//get second page for the copyright text

        String copyrightText = "CC BY-SA 3.0 2015, Wikipedia contributors; see Appendix for a complete list of contributors. Please see http://creativecommons.org/licenses/by-sa/3.0/ for full license.\r\r"+
                "Edited, compiled and designed by Michael Mandiberg (User:Theredproject).\r\r"+ 
                "This work is legally categorized as an artistic work. As such, this qualifies for trademark use under clause 3.6.3 (Artistic, scientific, literary, political, and other non-commercial uses) as denoted at wikimediafoundation.org/wiki/Trademark_policy\r\r"+
                "Wikipedia is a trademark of the Wikimedia Foundation and is used with the permission of the Wikimedia Foundation. This work is not endorsed by or affiliated with the Wikimedia Foundation.\r\r"+
                "Produced with support from Eyebeam, The Banff Centre, the City University of New York, and Lulu.com. Designed and built with assistance from Denis Lunev, Jonathan Kirtharan, Kenny Lozowski, Patrick Davison, and Colin Elliot.\r\r"+
                "PrintWikipedia.com\r\rGitHub.com/mandiberg/printwikipedia\r\rPrinted by Lulu.com";
        PdfPTable cpTable = new PdfPTable(1);
        try {
            times = wikiFontSelector.getCommonFont().getBaseFont();
            Font cpt = new Font(bflib,8);
            Paragraph cpp = new Paragraph(copyrightText,cpt);
            PdfPCell cell2 = new PdfPCell(cpp);
            cell2.setBorderWidth(0f);
            cell2.setHorizontalAlignment(Element.ALIGN_TOP-50);
            cell2.setVerticalAlignment(Element.ALIGN_LEFT);
            cell2.setColspan(1);
            cpTable.addCell(cell2);
            ColumnText column2 = new ColumnText(preWrite.getDirectContent());
            column2.addElement(cpTable);
            column2.setSimpleColumn (preToc.left()-60, preToc.bottom()-10, preToc.right(), preToc.top()-33);
            column2.go();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(60);
        mct.addRegularColumns(pdfDocument.left(),
                pdfDocument.right(), 15f, 4);
        
            try { //This is necessary but i'm sure a better solution can be found. 
                //for some reason the first title always starts up near the top of the page. this forces it down to be aligned with the other columns.
                Phrase ph = wikiFontSelector.getTitleFontSelector().process("\n\n");
                ph.setLeading(30f);
                mct.addElement(ph);
                pdfDocument.add(mct);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        
    }

    /**
     *
     */
    public void closeMultiColumn() {
        try {
            //pdfDocument.add(mct);
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
    public boolean getPageFull(){
        return headerFooter.pageFull;
    }
    private Document pdfDocument = null;
    private Document preToc = null;
    private FontSelector _fontSelector = null;
    private MultiColumnText mct = null;
    private PdfWriter pdfWriter;
    private PdfWriter preWrite;
    private int currentPageNum = 1;
    private int currentTitleNum = 0;
    public BaseFont bflib;
    private TitlesFooter headerFooter;
    private WikiFontSelector wikiFontSelector;
}
