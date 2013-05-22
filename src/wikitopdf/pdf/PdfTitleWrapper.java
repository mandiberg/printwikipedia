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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


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

    /**
     *
     * @param num
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public PdfTitleWrapper(int num, int startPage) throws FileNotFoundException, DocumentException {
        //Read settings
        String outputFileName = "temp/wikipedia-toc-volume" + num + ".pdf";

        pdfDocument = new Document(new Rectangle(432, 648));

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
        ct = new ColumnText(cb);
        //ct.setIndent(20);

    }

    /**
     *
     * @param line
     * @throws DocumentException
     */
    public void writeTitle(String line) throws DocumentException {
        try {
            //Chunk ch = new Chunk(line);
            //ch.setSplitCharacter(new SplitChar());
            //Phrase ph = new Phrase(ch);
            Phrase ph = wikiFontSelector.getTitleFontSelector().process(line);
            ph.setLeading(10);
            //ph.setFontSize(20);

            Paragraph pr = new Paragraph(ph);
            //calculate width of word
            float widthLine = wikiFontSelector.getCommonFont().getBaseFont().getWidthPoint(line,
                    wikiFontSelector.getCommonFont().getSize());
            pr.setKeepTogether(true);

            //if word wraps
            // this needs to be adjusted to indent wrapped words 2013
            if (widthLine > 85) {
            //    pr.setAlignment(Element.ALIGN_RIGHT);
            }

            if (mct.isOverflow()) {
                System.out.println("page is end  :" + pdfDocument.getPageNumber() + "  " + line);
                mct.nextColumn();
                pdfDocument.newPage();
            }

            mct.addElement(pr);
            pdfDocument.add(mct);
            headerFooter.setCurrentLine(line);

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
    public void addPrologue() throws DocumentException {
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
        
        cb.setFontAndSize(times, 12);
        cb.setTextMatrix(pdfDocument.right() - 200, 200);
        cb.showText("testing - delete me");
        
        cb.endText();

        pdfDocument.newPage();

               
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
                "in the section entitled ‚ÄúGNU Free Documentation License‚Äù.";

        cb.beginText();
        cb.setFontAndSize(times, 8);

        String[] textArr = copyrightText.split("\r\n");
        for (int i = 0; i < textArr.length; i++) {
            cb.setTextMatrix(pdfDocument.left() - 10, 100 - (i * 10));
            cb.showText(textArr[i]);
        }

        cb.endText();
        pdfDocument.newPage();
        
        
        
        
    }

    /**
     *
     */
    public void openMultiColumn() {
        mct = new MultiColumnText(60);
        mct.addRegularColumns(pdfDocument.left(),
                pdfDocument.right(), 13f, 4);

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
