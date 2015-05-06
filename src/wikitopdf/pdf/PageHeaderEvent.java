/*
 */
package wikitopdf.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import wikitopdf.utils.WikiLogger;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PageHeaderEvent extends PdfPageEventHelper {

    /**
     *
     * @param startPage
     */
    public PageHeaderEvent(int startPage) {
        this.startPage = startPage;
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {

        contentPage = writer.getDirectContent();
        theBottom = document.bottom();
        
        try {
            WikiFontSelector wikiFontSelector = new WikiFontSelector();
            wikiFontSelector.getTitleFontSelector().process("");
            bsFont = wikiFontSelector.getCommonFont().getBaseFont();
        } catch (Exception e) {
            WikiLogger.getLogger().severe(e.getMessage());
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document){
       
       pageNum = writer.getPageNumber() + startPage;
       if(writer.getPageNumber() <=3)
           return;
       contentPage.saveState();
       
       if ((pageNum % 2) == 0 && writer.getPageNumber() > 3) {
           writeHeader(currentTitle, document.left(),//-5.5f,
                    document.getPageSize().getHeight() - 27, PdfContentByte.ALIGN_LEFT);
           contentPage.restoreState();
       }
       
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        pageNum = writer.getPageNumber() + startPage;
        if(writer.getPageNumber() < 2)
            return;
        contentPage.saveState();
        
        String pNumString = String.valueOf(pageNum);
        float textBase = document.bottom() +2;
        float textSize = bsFont.getWidthPoint(pNumString, 8);
        contentPage.beginText();
        contentPage.setFontAndSize(bsFont, 8);
        currentTitle = currentTitle.length() > 20 ? currentTitle.substring(0, 20) + "..." : currentTitle;
        if ((pageNum % 2) == 1)
        {
            if(writer.getPageNumber()>2){
                contentPage.setTextMatrix(document.right()-16.5f, textBase-25);
                contentPage.showText(pNumString);
                contentPage.endText();
                contentPage.restoreState();
            }
            
            if(pageNum!=1){
                if(writer.getPageNumber()>3){
                    
                    writeHeader(currentTitle, document.right(),//-5.5f,
                    document.getPageSize().getHeight() - 27, PdfContentByte.ALIGN_RIGHT);
                
                    contentPage.restoreState();
                }
            }
            
        }
        else
        {
//            float adjust = bsFont.getWidthPoint("0", 8);
            if(writer.getPageNumber()>2){
                contentPage.setTextMatrix(document.left(), textBase-25);
                contentPage.showText(pNumString);
                contentPage.endText();
                contentPage.restoreState();
            }
            
//            writeHeader(currentTitle, document.left(),
//                    document.getPageSize().getHeight() - 47,
//                    PdfContentByte.ALIGN_LEFT);
//            contentPage.restoreState();
            //Write header
//            writeHeader(writer, document);
        }
        
        
    }

    /**
     *
     * @param line
     */
    public void setCurrentTitle(String line) {
        //Current line for header
        currentTitle = line;
    }

    /**
     *
     * @return
     */
    public int getPageNum(){
        return pageNum;
    }

    private void writeHeader(String text, float x, float y, int align){
        
        int sign = (align == PdfContentByte.ALIGN_LEFT) ? -1 : 1;
        contentPage.saveState();
        contentPage.beginText();
        contentPage.setFontAndSize(bsFont, 8);

        contentPage.showTextAligned(align, text, x, y, 0);
        
        contentPage.endText();
//        contentPage.restoreState();

        //13.5f - space to left or right page border
//        40.5<x  y>621.010
//        418.5<x  y>621.010
        System.out.println(x+13.5 + "<x  y>" + y+10);
        contentPage.moveTo(x + 13.5f * sign, y + 10);
        contentPage.setColorStroke(new GrayColor(1));
        contentPage.lineTo(x + 13.5f * sign, y - 1);
        contentPage.stroke();
        
        contentPage.moveTo(x+13.5f* sign, theBottom -13);
        contentPage.setColorStroke(new GrayColor(1));
        contentPage.lineTo(x + 13.5f* sign, theBottom - 24);
        contentPage.stroke();
    }

    private PdfContentByte contentPage;
    private BaseFont bsFont;
    private int pageNum = 0;
    private int startPage = 0;
    private float theBottom = 0f;
    private String currentTitle = "";
}
