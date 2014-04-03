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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 *
 * @author Home
 */
public class PdfCoverWrapper {
    
   //dunno if these are necessary 
    private ColumnText ct;
    private int column = 0;
    private int status = 0;
    float[] right = {70, 320};
    float[] left = {300, 550};
    public int width = 1025;
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
        cb.setTextMatrix(pdfDocument.right() - 130, 500);
        cb.showText("Wikipedia");
        
        cb.setFontAndSize(times, 12);
        cb.setTextMatrix(pdfDocument.right()-130, pdfDocument.bottom()+20);
        cb.showText("March 2014 Edition");
        //Use the code below to create rotated text the first constant indicates alignment,
        //the third and fourth arguments indicate the origin of rotation,
        //the last argument is the rotation in degrees
        //cb.showTextAligned(cb.ALIGN_LEFT,"Wikipedia",0,0,90);
        String[] titleArr = fileName.split("-");
        String beginTitle = titleArr[2];
        String endTitle = titleArr[3];
        beginTitle = beginTitle.replaceAll("[_]"," ");
        endTitle = endTitle.replaceAll("[_]"," ");
        String volNumber = titleArr[1];
        cb.setFontAndSize(times, 8);
        cb.setTextMatrix(pdfDocument.right() - 50, 490);
        cb.showText("Volume "+volNumber);
        
        cb.setFontAndSize(times, 24);
        
        String mainTitle = beginTitle +" - "+ endTitle;
        String[] frontTitle = mainTitle.split("\n");
        float widthLine = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(mainTitle,
                    wikiFontSelector.getCommonFont().getSize());
        System.out.println(widthLine + " widthline");
        for (int i = 0; i < frontTitle.length; i++) {
            cb.setTextMatrix(pdfDocument.right()-130, pdfDocument.bottom()+350);
            cb.showText(frontTitle[i]);
        }
//        int leftNess = 50;//amount of pixels to the right
//        int titleLength = mainTitle.length();//length of title string
//        if(titleLength > 12){//at 12 times new roman font there are 4.167 pixels per char.--double vs. int issue here.
//            leftNess = titleLength * 5;
            //need to do something more here because some titles are very very long. inserting
            //inserting a "\n" character at X point would be the solution
            //java should store strings as Arrays. Just a question of where to split.
            
//        }
//        cb.setTextMatrix(pdfDocument.right() - 200, 470);
//        cb.showText(mainTitle);
//        
        cb.endText();
        VerticalText vt = new VerticalText(pdfWriter.getDirectContent());
         vt.setVerticalLayout(width/2, height/2, 540, 2, 0);
         vt.addText(new Phrase("Wikipedia Table of Contents"));
         
         vt.go();
          vt.setAlignment(Element.ALIGN_RIGHT);
//        cb.showText("Wikipedia Table of Contents");
        
        cb.endText();     
        String copyrightText = "Copyright (c) 2013 WIKIMEDIA FOUNDATION. \r\n" +
                "Permission is granted to copy, distribute and/or modify this document under the \r\n" +
                "terms of the GNU Free Documentation License, Version 1.2 or any later version \r\n" +
                "published by the Free Software Foundation; with no Invariant Sections, no \r\n" +
                "Front-Cover Texts, and no Back-Cover Texts. A copy of the license is included \r\n" +
                "in the section entitled ‚\"GNU Free Documentation License\".";

        cb.beginText();
        cb.setFontAndSize(times, 8);

        String[] textArr = copyrightText.split("\r\n");
        for (int i = 0; i < textArr.length; i++) {
            cb.setTextMatrix(pdfDocument.left() + 320, 100 - (i * 10));
            cb.showText(textArr[i]);
        }
        cb.endText();
 //       pdfDocument.newPage();
        
        
        
        
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

