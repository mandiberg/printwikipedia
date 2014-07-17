package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.MultiColumnText;
import com.lowagie.text.pdf.PdfContentByte;
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

    /**
     *
     * @param num
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     */
 
    public PdfTitleWrapper(int num, int startPage,String firstLine, String lastLine) throws FileNotFoundException, DocumentException {
        //Read settings
        int curPage = startPage;
        startPage = 0;
        PdfTitleWrapper.lastLine =lastLine;
        if(PdfTitleWrapper.lastLine!=""){
            new File("temp/tocVol-"+PdfTitleWrapper.num+"-"+PdfTitleWrapper.firstLine+".pdf").renameTo(new File(
                    "temp/"+String.format("%04d",PdfTitleWrapper.num)+"&&&"+PdfTitleWrapper.firstLine+"&&&"+PdfTitleWrapper.lastLine+"&&&.pdf"));
        }
        firstLine = firstLine.replaceAll("[_]"," ");//get rid of the _ for pretty printing :>
        String outputFileName = "temp/tocVol-" + num + "-"+firstLine+".pdf";

        pdfDocument = new Document(new Rectangle(432, 648));
        //(l,r,t,b)
        pdfDocument.setMargins(66f, 63f, 5.5f, 62.5f);
//        System.out.println("startpage " + curPage);
//        if ((curPage % 2) == 0){
//            pdfDocument.setMargins(25.8f, 57.6f, -3f, 58.2f); //if even page then put gutter on right
//        }
//        else{
//            pdfDocument.setMargins(57.6f, 25.8f, -3f, 58.2f);//else gutter left.
//        }
        

        pdfWriter = PdfWriter.getInstance(pdfDocument,
                new FileOutputStream(outputFileName));
        
        headerFooter = new TitlesFooter(startPage);
        pdfWriter.setPageEvent(headerFooter);
        pdfDocument.open();

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
          System.out.println("-2");
          return -2; 
        }
        c = tempLine.charAt(1);
        if(c==' '){
            System.out.println("-1");
          return -1; 
        }
        c = tempLine.charAt(2);
        if(c==' '){
            System.out.println("0/middle");
          return 0; 
        }
        c = tempLine.charAt(3);
        if(c==' '){
            System.out.println("1");
          return 1; 
        }
        c = tempLine.charAt(4);
        if(c==' '){
            System.out.println("2");
          return 2; 
        }
        return 0;
        
    }
 
    
    public String longTitle(String line, int sizer) throws DocumentException{
        boolean tooLong = false;
        if(line.length()<sizer){//sier is the size of the column...
            return line;
        }
        int near = chopLine(line,sizer);//determine where to cut the line at and start a new line
        String lastLine=line.substring(0,sizer+near);
        line = line.substring(sizer+near);
        int doo = sizer+near;
        System.out.println(doo + "  16near outwhile");
        int x = 0;
        while(line.length()>=sizer+near){//while line is longer than 15 characters
            near = chopLine(line,sizer);
            doo = sizer+near;
            System.out.println(doo + "  16near in while");
           lastLine = lastLine+"\n  "+line.substring(0,sizer+near);
           line = line.substring(sizer+near);
           if(line.length()<=sizer){
               break;
            }
           x++;
           if(x>=5){//check to see if there should be an added line at the end
            tooLong = true;
            break;
           }
           else{
               tooLong = false;
           }
        }
        if(tooLong==false){
            System.out.println("too long == false");
            if(line.length()>=1){
                System.out.println(lastLine+ "  /finalLine" );
                lastLine = lastLine +"\n"+"  "+line;
                return lastLine;
            }
        }
        else{
            System.out.println(lastLine+ "  /finalLine" );
            lastLine = lastLine+"...";
            return lastLine;
        }
            
//        }
        System.out.println(lastLine+ "  /finalLine" );
        return lastLine;
    
    }
    public void writeTitle(String line) throws DocumentException {
        try {
            float widthLine = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(line,
                    wikiFontSelector.getCommonFont().getSize());
            if (widthLine > 40) {
                line = longTitle(line,20);//do the function returning the correct string.
            }
                
                Phrase ph = wikiFontSelector.getTitleFontSelector().process(line);
                ph.setLeading(8);
                Paragraph pr = new Paragraph(ph);

                pr.setKeepTogether(true);//should this always be set to true?

            //if word wraps
            // this needs to be adjusted to indent wrapped words 2013
            

            if (mct.isOverflow()) {
                System.out.println("page is end  :" + pdfDocument.getPageNumber() + "  " + line);
                mct.nextColumn();
                pdfDocument.newPage();
            }

            mct.addElement(pr);
//            System.out.println(mct);
            pdfDocument.add(mct);
            headerFooter.setCurrentLine(line);

            
            //ph.setFontSize(20);
            

            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        currentTitleNum++;
    }


    /*
    public void wrline(String line)
    {
    this.
    ct.setSimpleColumn(this.right[column], 60, this.left[column], 700, 16, Element.ALIGN_JUSTIFIED);

    //Phrase element = new Phrase(line);
    Chapter chapter = new Chapter(new Paragraph(line), 1);

    ct.addElement(chapter);
    //ct.addText(chapter);
    try
    {
    status = ct.go();

    System.out.println("status: " + status);
    System.out.println("columnText: " + ColumnText.NO_MORE_COLUMN);
    }
    catch (Exception ex)
    {
    ex.printStackTrace();
    }
    }
     */
    /**
     *
     */
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

        cb.beginText();
        
        cb.setFontAndSize(times, 32);
        cb.setTextMatrix(pdfDocument.right() - 130, 500);
        cb.showText("Wikipedia");
        
        cb.setFontAndSize(times, 8);
        cb.setTextMatrix(pdfDocument.right() - 50, 490);
        cb.showText("table of contents");
        
//        cb.setFontAndSize(times, 12);
//        cb.setTextMatrix(pdfDocument.right() - 200, 200);
//        cb.showText("testing - delete me");
        
        cb.endText();
               
        /*
        // adding blank page - WHY ISN'T THIS WORKING???
        cb.beginText();
        
        cb.setFontAndSize(times, 32);
        cb.setTextMatrix(pdfDocument.right() - 130, 500);
        cb.showText("blank page");
        
        cb.endText();

        pdfDocument.newPage();
*/
        
        
        
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
            cb.setTextMatrix(pdfDocument.left() - 10, 100 - (i * 10));
            cb.showText(textArr[i]);
        }
        cb.endText();
        pdfDocument.newPage();
        cb.beginText();
        cb.setFontAndSize(times,8);
        cb.setTextMatrix(pdfDocument.left()-19,100);
        cb.showText("");
        cb.endText();
        
        
        
        
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(60);
        mct.addRegularColumns(pdfDocument.left(),
                pdfDocument.right(), 6f, 4);

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
    private Document pdfDocument = null;
    private FontSelector _fontSelector = null;
    private MultiColumnText mct = null;
    private PdfWriter pdfWriter;
    private int currentPageNum = 3;
    private int currentTitleNum = 0;
    private TitlesFooter headerFooter;
    private WikiFontSelector wikiFontSelector;
}
