/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


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
    public int width = 1109;//spine = 119 points beginning at 495 points.
    //meaning that each page is 495 points wide.
    public int height = 774;
    
        /**
     *
     * @param num
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public PdfCoverWrapper(int num, int startPage) throws FileNotFoundException, DocumentException {
        //Read settings
        
        String outputFileName = "covers/volume" + num + ".pdf";
        
        
        /*
         * NEED TO CHANGE THESE VALUES TO REFLECT COVER SPECS
         * 
         */
        
        pdfDocument = new Document(new Rectangle(width, height));

        pdfDocument.setMargins(25, 25, -35, 25);

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream(outputFileName));

        headerFooter = new TitlesFooter(startPage);
        pdfWriter.setPageEvent(headerFooter);
        pdfDocument.open();

        wikiFontSelector = new WikiFontSelector();
        //HeaderFooter hf =  new HeaderFooter(new Phrase("head1"), new Phrase("head2"));

        //pdfDocument.setHeader(hf);

        //addPrologue();
        //openMultiColumn();




        PdfContentByte cb = pdfWriter.getDirectContent();
        //ct = new ColumnText(cb);
        //ct.setIndent(20);

    }
    public int chopLine(String line, int sizer) throws DocumentException{
        int chopStart = sizer - 3;
        int chopEnd = sizer + 2;
           
        if(line.length()<chopEnd){
            return 0;
        }
        String tempLine = line.substring(chopStart,chopEnd);
        System.out.println(tempLine);
        Character c = tempLine.charAt(0);
        if(c==' '){
          return -2; 
        }
        c = tempLine.charAt(1);
        if(c==' '){
          return -1; 
        }
        c = tempLine.charAt(2);
        if(c==' '){
          return 0; 
        }
        c = tempLine.charAt(3);
        if(c==' '){
          return 1; 
        }
        c = tempLine.charAt(4);
        if(c==' '){
          return 2; 
        }
       
        return 0;
        
    }
 
    
    public String longTitle(String line, int sizer) throws DocumentException{
        boolean tooLong = false;
        if(line.length()<sizer){
            return line;
        }
        int near = chopLine(line,sizer);
        String lastLine=line.substring(0,sizer+near)+"\r\n";
        line = line.substring(sizer+near);
        while(line.length()>=sizer+near){//while line is longer than 15 characters
           near = chopLine(line,sizer);
           lastLine = lastLine+"\r\n"+line.substring(0,sizer+near);
           line = line.substring(sizer+near);
           if(line.length()<=sizer){
               break;
            }
        }
        lastLine = lastLine +"\r\n"+line;

        System.out.println(lastLine+ "  /finalLine" );
        return lastLine;
    }
    
    
        public void addCover(String fileName) throws DocumentException {
            
        PdfContentByte cb = pdfWriter.getDirectContent();
        BaseFont times = null;

        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cb.beginText();
        
        cb.setFontAndSize(times, 72);
        cb.setTextMatrix(pdfDocument.right() - 330, 370);
        cb.showText("Wikipedia");

        cb.showTextAligned(cb.ALIGN_LEFT,"Wikipedia",595,595,270);
        cb.setFontAndSize(times, 12);
        cb.setTextMatrix(pdfDocument.right()-230, pdfDocument.bottom()+20);
        cb.showText("March 2014 Edition");
        //Use the code below to create rotated text the first constant indicates alignment,
        //the third and fourth arguments indicate the origin of rotation,
        //the last argument is the rotation in degrees
        //cb.showTextAligned(cb.ALIGN_LEFT,"Wikipedia",0,0,90);
        String[] titleArr = fileName.split("&&&");
        String beginTitle = titleArr[2];
        String endTitle = titleArr[3];
        String volNumber = titleArr[1];
        cb.setFontAndSize(times, 11);
        cb.setTextMatrix(pdfDocument.right() - 150, 350);
        cb.showText("Volume "+volNumber);
        
        //string formatting for titles
        cb.setFontAndSize(times, 20);
        cb.setTextMatrix(pdfDocument.right()-575, 230);
        cb.showText("Volume");
        cb.setFontAndSize(times, 20);
        cb.setTextMatrix(pdfDocument.right()-575, 220);
        cb.showText(volNumber);
        
        String mainTitle = beginTitle +" - " + endTitle;
        String lSpineTitle = "";
        String rSpineTitle = "";
        if(beginTitle.length()>3){
            lSpineTitle = beginTitle.substring(0,3);
        }
        else{
            lSpineTitle=beginTitle;
        }
        if(endTitle.length()>4){
            rSpineTitle = endTitle.substring(0,3);
        }
        else{
            rSpineTitle = endTitle;
        }
        cb.setFontAndSize(times, 20);
        cb.setTextMatrix(pdfDocument.right()-575,100);
        cb.showText(lSpineTitle);
        cb.setFontAndSize(times, 15);
        cb.setTextMatrix(pdfDocument.right()-575,82);
        cb.showText("TO");
        cb.setFontAndSize(times, 20);
        cb.setTextMatrix(pdfDocument.right()-575,64);
        cb.showText(rSpineTitle);
        
        float widthLine = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(mainTitle,
                    wikiFontSelector.getCommonFont().getSize());
        String finalTitle ="";
        if(widthLine > 101.885){
            finalTitle = longTitle(mainTitle,32);
        }
        else{
            finalTitle = mainTitle;
        }
        System.out.println(finalTitle+" ///secind FT");
        
        cb.beginText();
        cb.setFontAndSize(times, 24);
        String[] textArr = finalTitle.split("\r\n");
        for (int i = 0; i < textArr.length; i++) {
            cb.setTextMatrix(pdfDocument.right()-330, 300-(i*18));
            System.out.println(textArr[i] + " this is line");
            cb.showText(textArr[i]);
        }
        cb.endText();
     
//        String copyrightText = "Copyright (c) 2013 WIKIMEDIA FOUNDATION. \r\n" +
//                "Permission is granted to copy, distribute and/or modify this document under the \r\n" +
//                "terms of the GNU Free Documentation License, Version 1.2 or any later version \r\n" +
//                "published by the Free Software Foundation; with no Invariant Sections, no \r\n" +
//                "Front-Cover Texts, and no Back-Cover Texts. A copy of the license is included \r\n" +
//                "in the section entitled ‚\"GNU Free Documentation License\".";
//
//        cb.beginText();
//        cb.setFontAndSize(times, 8);
//
//        String[] textArr = copyrightText.split("\r\n");
//        for (int i = 0; i < textArr.length; i++) {
//            cb.setTextMatrix(pdfDocument.left() + 520, 100 - (i * 10));
//            cb.showText(textArr[i]);
//        }
//        cb.endText();
 //       pdfDocument.newPage();
        

// add a rectangle
cb.rectangle(495, 0, 195, height);
// stroke the lines
cb.setColorStroke(Color.BLACK);
cb.stroke();

        
        
    }

     /**
     * Converts the CIDs of the horizontal characters of a String
     * into a String with vertical characters.
     * @param text The String with the horizontal characters
     * @return A String with vertical characters
     */
    public String convertCIDs(String text) {
        char cid[] = text.toCharArray();
        for (int k = 0; k < cid.length; ++k) {
            char c = cid[k];
            if (c == '\n')
                cid[k] = '\uff00';
            else
                cid[k] = (char) (c - ' ' + 8720);
        }
        return new String(cid);
    }

    
    
        public void close() {
        try {
            //pdfDocument.add(mct);
            //int i = 1;
            /*
            do
            {

            //mct.nextColumn();
            //pdfDocument.newPage();

            System.out.println("page " + i + " -- " );
            i++;
            }while(mct.isOverflow());
             */

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
    private FontSelector _fontSelector = null;
    private MultiColumnText mct = null;
    private PdfWriter pdfWriter;
    private int currentPageNum = 3;
    private int currentTitleNum = 0;
    private TitlesFooter headerFooter;
    private WikiFontSelector wikiFontSelector;

    
}

