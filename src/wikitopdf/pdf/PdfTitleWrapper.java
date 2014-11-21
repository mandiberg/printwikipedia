package wikitopdf.pdf;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
 
    public PdfTitleWrapper(int num, int startPage,String firstLine, String lastLine) throws FileNotFoundException, DocumentException {
        //Read settings
        System.out.println("line in pdftitlewrap  "+firstLine);
        int curPage = startPage;
        pageFull = false;
        startPage = 0;
        PdfTitleWrapper.lastLine = lastLine;
        if(PdfTitleWrapper.lastLine!=""){
            PdfTitleWrapper.lastLine = PdfTitleWrapper.lastLine.replace("/","\\");
            new File("temp/tocVol-"+PdfTitleWrapper.num+"-"+PdfTitleWrapper.firstLine+".pdf").renameTo(new File(
                    "temp/"+String.format("%04d",PdfTitleWrapper.num)+"&&&"+PdfTitleWrapper.firstLine+"&&&"+PdfTitleWrapper.lastLine+"&&&.pdf"));
        }
        firstLine = firstLine.replaceAll("[_]"," ");
        firstLine = firstLine.replace("/","\\");//gets confused on filenames with / as is supposed to be directory separator
        String outputFileName = "temp/tocVol-" + num + "-"+firstLine+".pdf";

        pdfDocument = new Document(new Rectangle(432, 648));
        //(l,r,t,b)
        //going to switch the margins here because in the lulu printout it seems to have the margins going the opposite sides
        //originally 27f, 67.5f
        
        pdfDocument.setMargins(74.3f, 27f, 5.5f, 62.5f);
        
        

        

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream(outputFileName));
        
        headerFooter = new TitlesFooter(startPage);
        pdfWriter.setPageEvent(headerFooter);
        pdfDocument.open();
        pdfDocument.setMarginMirroring(true);
        wikiFontSelector = new WikiFontSelector();
        //HeaderFooter hf =  new HeaderFooter(new Phrase("head1"), new Phrase("head2"));

        //pdfDocument.setHeader(hf);


        //openMultiColumn();
//        TitlesFooter.setCurrentLine(firstLine);
        PdfTitleWrapper.firstLine = firstLine;
        PdfTitleWrapper.num=num;
        
        
        
        
        
        PdfContentByte cb = pdfWriter.getDirectContent();
        ct = new ColumnText(cb);
        //ct.setIndent(20);
        
//        addPrologue();
//        newPage();
        
    }

    /**
     *
     * @param line
     * @throws DocumentException
     */

    public void writeTitle(String line,FontSelector fs) throws DocumentException {
        
        try {
                System.out.println(line + " begin writetitle");
                if(line.length()>100){
                    line = line.substring(0, 96)+"...";
                }
                Phrase ph = fs.process(line);
                ph.setLeading(8);
                Paragraph pr = new Paragraph(ph);
                pr.setAlignment("Left");
                pr.setFirstLineIndent(-5);
                pr.setIndentationLeft(5);
                pr.setKeepTogether(true);//should this always be set to true?

            //if word wraps
            //this needs to be adjusted to indent wrapped words 2013
            
            mct.addElement(pr);
            pdfDocument.add(mct);

            headerFooter.setCurrentLine(line);

            
            //ph.setFontSize(20);
            

            
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
        currentTitleNum++;
    }


   
    public void close() {
        System.out.println("you are closed");
        try {
            pdfDocument.close();
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public String coverChad(){
        System.out.println("hi welcome to chad");
        PdfContentByte canvas = pdfWriter.getDirectContent();
        Rectangle rect = new Rectangle(pdfDocument.right(), pdfDocument.top());
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(0);
        rect.setGrayFill(3);
        canvas.rectangle(rect);
        int lc = headerFooter.getLineCount();
        String newLastLine = headerFooter.lineList.get(lc-2);
        System.out.println(newLastLine + " this is ll");
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
    public final void jknewPage() throws DocumentException{
      pdfDocument.add(new Paragraph(""));
      pdfDocument.newPage();
      pdfDocument.add(new Paragraph(""));
    }
    public final void addPrologue() throws DocumentException {
        PdfContentByte cb = pdfWriter.getDirectContent();
        BaseFont times = null;
        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        
        //wikipedia on the first inside page.--right facing
        cb.beginText();
        
        cb.setFontAndSize(times, 40);
        cb.setTextMatrix(pdfDocument.right() - 182, 425);
        cb.showText("Wikipedia");
        cb.endText();
        PdfPTable tocTable = new PdfPTable(1);
//        ct.setSimpleColumn(pdfDocument.left(),100,pdfDocument.right(),300);//llx,lly,urx,ury
        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
            Font pght = new Font(times,15);
            Paragraph pgh = new Paragraph("Table of Contents\nVolume "+String.valueOf(PdfTitleWrapper.num),pght);
            PdfPCell cell = new PdfPCell(pgh);
            cell.setBorderWidth(0f);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setColspan(1);
            tocTable.addCell(cell);
            ColumnText column = new ColumnText(pdfWriter.getDirectContent());
            column.addElement(tocTable);
            column.setSimpleColumn (pdfDocument.left()+20, pdfDocument.bottom()+20, pdfDocument.right()+27.5f, pdfDocument.bottom()-100);
            column.go();
//        pgh.setAlignment("right");
        
//      pgh.setFont(pght);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        pdfDocument.newPage();//get second page for the copyright text
//        ColumnText ct = new ColumnText(pdfWriter.getDirectContent());
        
        
        
//        cb.setTextMatrix(pdfDocument.right(), 100);
        
//        cb.showText("Table of Contents");
//        cb.endText();
//        cb.beginText();
//        cb.setFontAndSize(times, 8);
//        cb.setTextMatrix(pdfDocument.right(), 90);
//        cb.showText(String.valueOf(PdfTitleWrapper.num));
       
//        String copyrightText = "Copyright (c) 2013 WIKIMEDIA FOUNDATION. \r\n" +
//                "Permission is granted to copy, distribute and/or modify this document under the \r\n" +
//                "terms of the GNU Free Documentation License, Version 1.2 or any later version \r\n" +
//                "published by the Free Software Foundation; with no Invariant Sections, no \r\n" +
//                "Front-Cover Texts, and no Back-Cover Texts. A copy of the license is included \r\n" +
//                "in the section entitled ‚\"GNU Free Documentation License\".";

        String copyrightText = "CC BY-SA 2014, Wikipedia contributors; see Appendix for a complete list of contributors. Please see http://creativecommons.org/licenses/by-sa/3.0/ for full license.\r\r"+
                "Edited, compiled and designed by Michael Mandiberg (User:Theredproject).\r\r"+ 
                "This work is legally categorized as an artistic work. As such, this qualifies for trademark use under clause 3.6.3 (Artistic, scientific, literary, political, and other non-commercial uses) as denoted at wikimediafoundation.org/wiki/ Trademark_policy\r\r"+
                "Wikipedia is a trademark of the Wikimedia Foundation and is used with the permission of the Wikimedia Foundation. This work is not endorsed by or affiliated with the Wikimedia Foundation.\r\r"+
                "Produced with support from Eyebeam, The Banff Centre and the City University of New York, and assistance from Patrick Davison, Denis Lunev, Kenny Lozowski, Colin Elliot, and Jonathan Kirtharan. \r\r"+
                "www.PrintWikipedia.com\r\r Printed by Lulu.com";
        PdfPTable cpTable = new PdfPTable(1);
        try {
            times = wikiFontSelector.getCommonFont().getBaseFont();
            Font cpt = new Font(times,8);
            Paragraph cpp = new Paragraph(copyrightText,cpt);
            PdfPCell cell2 = new PdfPCell(cpp);
            cell2.setBorderWidth(0f);
            cell2.setHorizontalAlignment(Element.ALIGN_TOP-50);
            cell2.setVerticalAlignment(Element.ALIGN_LEFT);
            cell2.setColspan(1);
            cpTable.addCell(cell2);
            ColumnText column2 = new ColumnText(pdfWriter.getDirectContent());
            column2.addElement(cpTable);
            column2.setSimpleColumn (pdfDocument.left()-88, pdfDocument.bottom()-10, pdfDocument.right(), pdfDocument.top()-57);
            column2.go();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        cb.beginText();
//        cb.setFontAndSize(times, 8);
//
//        String[] textArr = copyrightText.split("\r\n");
//        for (int i = 0; i < textArr.length; i++) {
//            cb.setTextMatrix(pdfDocument.left() - 10, 100 - (i * 10));
//            cb.showText(textArr[i]);
//        }
////        cb.beginText();
//        cb.setFontAndSize(times,8);
//        cb.setTextMatrix(pdfDocument.left()-19,100);
////        cb.showText("");
//        cb.endText();
        
        
        
        
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(60);
        mct.addRegularColumns(pdfDocument.left(),
                pdfDocument.right(), 15f, 4);
        

        //First page hack 
       
        for (int i = 0; i < 4; i++) {
            try {
                Phrase ph = wikiFontSelector.getTitleFontSelector().process("\n");

                mct.addElement(ph);
                pdfDocument.add(mct);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
    private FontSelector _fontSelector = null;
    private MultiColumnText mct = null;
    private PdfWriter pdfWriter;
    private int currentPageNum = 3;
    private int currentTitleNum = 0;
    private TitlesFooter headerFooter;
    private WikiFontSelector wikiFontSelector;
}
