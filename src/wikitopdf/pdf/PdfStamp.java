/*
Write footer in existed document

 */
package wikitopdf.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import wikitopdf.utils.ByteFormatter;
import wikitopdf.utils.WikiLogger;
import wikitopdf.utils.WikiSettings;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class PdfStamp {

    /**
     *
     * @param fileName
     * @param startPage
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws IOException
     */
    public void writeFooter(String fileName, int startPage) throws FileNotFoundException, DocumentException, IOException {

        Runtime runtime = Runtime.getRuntime();

        WikiLogger.getLogger().info(fileName + " reader starts: " + ByteFormatter.format(runtime.freeMemory()));
        PdfReader reader = new PdfReader(WikiSettings.getInstance().getOutputFolder()
                + "/" + fileName);
        WikiLogger.getLogger().info(fileName + " reader ends: " + ByteFormatter.format(runtime.freeMemory()));

        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                WikiSettings.getInstance().getOutputFolder()
                + "/" + getOriginalName(fileName)));



        try {
            WikiFontSelector wikiFontSelector = new WikiFontSelector();
            wikiFontSelector.getTitleFontSelector().process("");
            bsFont = wikiFontSelector.getCommonFont().getBaseFont();
            int localTotal = reader.getNumberOfPages();
            int scaleTotal = String.valueOf(globalTotal).length();
            for (int currentPageNum = 1; currentPageNum < localTotal + 1; currentPageNum++) {
                int nullCount = scaleTotal - String.valueOf(currentPageNum + startPage).length();
                String nullsString = "";
                //add required numbers of null
                for (int i = 0; i < nullCount; i++) {
                    nullsString += "0";
                }
                String pageNum = nullsString + (currentPageNum + startPage);
                pageContent = stamper.getOverContent(currentPageNum);
                //if left page
                if (currentPageNum % 2 != 0) {
                    writeFooterLine(pageNum, 27, 27, PdfContentByte.ALIGN_LEFT);
                } else {
                    float pageWidth = reader.getPageSize(currentPageNum).getWidth();
                    writeFooterLine(pageNum, pageWidth - 27, 27, PdfContentByte.ALIGN_RIGHT);
                }
                //pageContent.endText();
                reader.releasePage(currentPageNum);

            }

            lastNumPage = localTotal;


        } catch (Exception e) {
            WikiLogger.getLogger().severe(e.getMessage());
        } finally {
            reader.close();
            stamper.close();
        }

        deleteTempFile(WikiSettings.getInstance().getOutputFolder()
                + "/" + fileName);
    }

    /**
     *
     * @param totalPages
     * @throws Exception
     */
    public void stampDir(int totalPages) throws Exception {

        this.globalTotal = totalPages;
        File directory = new File(WikiSettings.getInstance().getOutputFolder());
        File files[] = directory.listFiles();

        for (File file : files) {
            if (!file.getName().equals(".DS_Store")
                    && !file.getName().equals(".svn")) {
                writeFooter(file.getName(), lastNumPage);
            }
        }
    }

    private void writeFooterLine(String text, float x, float y, int align) {
        int sign = (align == PdfContentByte.ALIGN_LEFT) ? -1 : 1;
        pageContent.saveState();
        pageContent.beginText();
        pageContent.setFontAndSize(bsFont, 8);

        pageContent.showTextAligned(align, text, x, y, 0);

        pageContent.endText();
        pageContent.restoreState();

        //13.5f - space to left or right page border
        pageContent.moveTo(x + 13.5f * sign, y + 10);
        pageContent.setColorStroke(new GrayColor(1));
        pageContent.lineTo(x + 13.5f * sign, y - 1);
        pageContent.stroke();
    }

    private void deleteTempFile(String fileName) {

        File f = new File(fileName);

        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) {
            throw new IllegalArgumentException(
                    "Delete: no such file or directory: " + fileName);
        }

        if (!f.canWrite()) {
            throw new IllegalArgumentException("Delete: write protected: " + fileName);
        }

        // Attempt to delete it
        boolean success = f.delete();

        if (!success) {
            throw new IllegalArgumentException("Delete: deletion failed");
        }
    }

    private String getOriginalName(String fName) {
        String origName = fName.substring(1, fName.length());
        return origName;
    }

    private PdfContentByte pageContent;
    private BaseFont bsFont;
    private int lastNumPage = 0;
    private int globalTotal = 0;
}
