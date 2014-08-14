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
    //w = 1129 h = 774 for hc 670 pg book
    public int width = 1103;//spine = 119 points beginning at 495 points.
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
         * 
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
 
    
    public String longTitle(String line, int sizer, boolean multiLine) throws DocumentException{
        boolean tooLong = false;
        if(line.length()<sizer){
            return line;
        }
        int near = chopLine(line,sizer);
        String lastLine=line.substring(0,sizer+near)+"\r\n";
        line = line.substring(sizer+near);
        if(multiLine==false){
            sizer=sizer-5;
        }//after first iteration of chopping it make sizer smaller after indent.
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
    public float firstTitleResize(String beginTitle, double titleLength){//fuck with this
        String[] tempBegin = beginTitle.split("\r\n");
        int tb = tempBegin.length;
        String tempFirst = "";
        if(tb>1){
            tempFirst = tempBegin[tb];
        }
        else{
            tempFirst = beginTitle;
        }
        float tempFirstWidth = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(tempFirst,
            wikiFontSelector.getCommonFont().getSize());
        
        return tempFirstWidth;
    }
    
    public String breakTitle(String beginTitle, String endTitle, double titleLength){
        String finalTitle="";
        float beginWidth = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(beginTitle,
                wikiFontSelector.getCommonFont().getSize());
         float endWidth = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(endTitle,
                wikiFontSelector.getCommonFont().getSize());
         System.out.println(beginWidth + " that's begin \n" + endWidth + " tht's end width");
        if(beginWidth>titleLength){ //if first title has more space  than the second
            try {
                beginTitle = longTitle(beginTitle,36,false);//so you chop it up at 36
            }
            catch (DocumentException ex) {
                Logger.getLogger(PdfCoverWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
//            if(endWidth>titleLength){
//                try{
//                    endTitle=longTitle(endTitle,27,true);//if second title is larger and it's already broken then send true
//                                                    //so that sizer is not broken 5 chars earlier
//                }
//                catch(DocumentException ex) {
//                    Logger.getLogger(PdfCoverWrapper.class.getName()).log(Level.SEVERE, null, ex);
//                }//end catch
//            }//end if endtitle
//            finalTitle = beginTitle+endTitle;
//            System.out.println(finalTitle + " this is finaltitle before she airs.");
//            return finalTitle;
        }
        boolean nextLine = false;
        float tempFirstWidth = firstTitleResize(beginTitle,titleLength);
        if(tempFirstWidth>titleLength-30){
            beginTitle=beginTitle+"\n";
            nextLine = true;
        }
        if(nextLine==true){
            titleLength = titleLength -27;
        }

        if(endWidth>titleLength){//if you got this far it means that begintitle is short
                                 //and end is long so just add the two together and treat it as one.
            finalTitle = beginTitle + endTitle;
            try {
                finalTitle = longTitle(finalTitle,36,false);
                return finalTitle;
            } catch (DocumentException ex) {
                Logger.getLogger(PdfCoverWrapper.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
               
        return finalTitle;
    }
    
    
        public void addCover(String fileName) throws DocumentException {
            
        PdfContentByte cb = pdfWriter.getDirectContent();
        //declare my fonts
        BaseFont times = null;
        //could not figure out why these would not work with times. i guess because one is in contentbyte and one im doing paragraphs?
        Font spine_vol_font = null;
        Font spine_abbr_font = null;
        Font spine_to_font = null;
        Font main_title_font = null;
        
	
        
        try {
            wikiFontSelector.getTitleFontSelector().process("");
            times = wikiFontSelector.getCommonFont().getBaseFont();
            BaseFont spine_base = BaseFont.createFont("/Users/wiki/Library/Fonts/Cardo-Regular.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
            spine_vol_font = new Font(spine_base, 40);
            spine_abbr_font = new Font(spine_base,20);
            spine_to_font = new Font(spine_base,13);
            main_title_font = new Font(spine_base,24);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        cb.beginText();
        
        cb.setFontAndSize(times, 90);
        cb.setTextMatrix(pdfDocument.right() - 440, 600);
        cb.showText("Wikipedia");
        
        cb.setFontAndSize(times, 40);
        cb.setTextMatrix(50, 595);
        cb.showTextAligned(0,"Wikipedia Table of Contents",558,705,270);
        cb.setFontAndSize(times, 12);
        cb.setTextMatrix(pdfDocument.right()-230, 70);
        cb.showText("May 2014 Edition");
        //Use the code below to create rotated text the first constant indicates alignment,
        //the third and fourth arguments indicate the origin of rotation,
        //the last argument is the rotation in degrees
        String[] titleArr = fileName.split("&&&");
        String beginTitle = titleArr[1];
        String endTitle = titleArr[2];
        String volNumber = titleArr[0];
        //****replace the leading zeroes**
        volNumber = volNumber.replaceFirst("^0+(?!$)", "");
        cb.setFontAndSize(times, 18);
        cb.setTextMatrix(pdfDocument.right() - 150, 572);
        cb.showText("Volume "+ volNumber);
        

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
        PdfPTable table1 = new PdfPTable(1);
        PdfPTable table2 = new PdfPTable(1);
        PdfPTable table3 = new PdfPTable(1);
        PdfPTable table4 = new PdfPTable(1);
//        table.setTotalWidth(18.6f);
        Paragraph vol_num;
        Paragraph first_abbr;
        Paragraph spine_to;
        Paragraph scnd_abbr;

        vol_num = new Paragraph(volNumber, spine_vol_font);
        first_abbr = new Paragraph(lSpineTitle.toUpperCase(),spine_abbr_font);
        scnd_abbr = new Paragraph(rSpineTitle.toUpperCase(),spine_abbr_font);
        spine_to = new Paragraph("TO",spine_to_font);
        
        
        //spine section for volume number
        PdfPCell cell;
        cell = new PdfPCell(vol_num);
        cell.setBorderWidth(0f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setColspan(1);
        cell.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-220f));
        table1.addCell(cell);
        ColumnText column = new ColumnText(pdfWriter.getDirectContent());
        column.addElement(table1);
        //llx, lly,urx,ury 
        column.setSimpleColumn (494.64f, pdfDocument.bottom()-170f, 632.88f, pdfDocument.top()-50f);
        column.go();
        
        //spine section for first abbreviated title
        PdfPCell cell2;    
        cell2 = new PdfPCell(first_abbr);
        cell2.setBorderWidth(0f);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell2.setColspan(1);
        cell2.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-180f));
        table2.addCell(cell2);
        ColumnText column2 = new ColumnText(pdfWriter.getDirectContent());
        column2.addElement(table2);
        //llx, lly,urx,ury 
        column2.setSimpleColumn (494.64f, pdfDocument.bottom()-170f, 632.88f, pdfDocument.top()-50f);
        column2.go();
        
        //third table for TO
        PdfPCell cell3;    
        cell3 = new PdfPCell(spine_to);
        cell3.setBorderWidth(0f);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell3.setColspan(1);
        cell3.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-162f));
        table3.addCell(cell3);
        ColumnText column3 = new ColumnText(pdfWriter.getDirectContent());
        column3.addElement(table3);
        //llx, lly,urx,ury 
        column3.setSimpleColumn (494.64f, pdfDocument.bottom()-170f, 632.88f, pdfDocument.top()-50f);
        column3.go();
        
        //4th table for second abbreviated title!
        PdfPCell cell4;    
        cell4 = new PdfPCell(scnd_abbr);
        cell4.setBorderWidth(0f);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell4.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell4.setColspan(1);
        cell4.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-144f));
        table4.addCell(cell4);
        ColumnText column4 = new ColumnText(pdfWriter.getDirectContent());
        column4.addElement(table4);
        //llx, lly,urx,ury 
        column4.setSimpleColumn (494.64f, pdfDocument.bottom()-170f, 632.88f, pdfDocument.top()-50f);
        column4.go();
        //18 less
        
        
        //all this stuff is unecessary at this piont!!!!!
        
        
        
        
        
        
//        cb.setFontAndSize(times, 20);
//        cb.setTextMatrix(pdfDocument.right()-562,120);
//        cb.showText(lSpineTitle.toUpperCase());
//        cb.setFontAndSize(times, 13);
//        cb.setTextMatrix(pdfDocument.right()-549,102);
//        cb.showText("TO");
//        cb.setFontAndSize(times, 20);
//        cb.setTextMatrix(pdfDocument.right()-562,84);
//        cb.showText(rSpineTitle.toUpperCase());
        
        //printing the title underneath Wikipedia on right cover
        beginTitle = beginTitle + " — ";
        String mainTitle = beginTitle + endTitle;//title as it appears on the front cover
        PdfPTable tableTitle = new PdfPTable(1);
        tableTitle.setLockedWidth(false);
        Paragraph vol_title;
        vol_title = new Paragraph(mainTitle, main_title_font);
//        vol_title.setFirstLineIndent(-10f);
        PdfPCell cell_title;
        cell_title = new PdfPCell(vol_title);
        cell_title.setFollowingIndent(50);
        cell_title.setBorderWidth(0f);
//        cell_title.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell_title.setVerticalAlignment(Element.ALIGN_TOP);
        cell_title.setColspan(1);
        cell_title.setMinimumHeight(pdfDocument.top()+(pdfDocument.bottom()-220f));
        tableTitle.addCell(cell_title);
        ColumnText columnTitle = new ColumnText(pdfWriter.getDirectContent());
        columnTitle.addElement(tableTitle);
        //llx, lly,urx,ury 
        columnTitle.setSimpleColumn (637f, pdfDocument.bottom()-170f, 1037f, pdfDocument.top()-330f);
        columnTitle.go();
        
        
//        String finalTitle ="";
//        
//        float widthLine = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(mainTitle,
//                    wikiFontSelector.getCommonFont().getSize());
//        double titleLength = 89.885;//121.885 is original
//        
//        if(widthLine > titleLength){//if the whole thing is too large check to see if you can break it up
//            finalTitle = breakTitle(beginTitle,endTitle,titleLength);
//        }
//        else{
//            finalTitle = mainTitle;
//        }
//        System.out.println(finalTitle+" ///secind FT");
//        
//        cb.beginText();
//        cb.setFontAndSize(times, 24);
//        String[] textArr = finalTitle.split("\r\n");
//        for (int i = 0; i < textArr.length; i++) {
//            if(i>=1){
//                cb.setTextMatrix(pdfDocument.right()-365, 300-(i*18));
//            }
//            else{
//                cb.setTextMatrix(pdfDocument.right()-415, 300-(i*18));
//            }
//            System.out.println(textArr[i] + " this is line");
//            cb.showText(textArr[i]);
//        }
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
//cb.rectangle(495, 0, 195, height);
//// stroke the lines
//cb.setColorStroke(Color.BLACK);
//cb.stroke();

        
        
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

