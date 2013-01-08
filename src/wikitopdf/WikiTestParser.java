/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wikitopdf;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author денис
 */
public class WikiTestParser
{
    /**
     *
     * @param inputFileName
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void parse(String inputFileName) throws UnsupportedEncodingException, FileNotFoundException, IOException
    {

        long startDate = new Date().getTime();

        FileInputStream inputStream = new FileInputStream(inputFileName);
        BufferedReader bufferReader = null;

        bufferReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        String line = "";

        SQLProcessor sqlProcessor = null;
        try
        {
            sqlProcessor = new SQLProcessor();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(WikiTitleParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        PdfWriter pdfWriter;
        Document pdfDocument = new Document(new Rectangle(535, 697));

        try
        {
            
            pdfWriter = PdfWriter.getInstance(pdfDocument, new FileOutputStream("hhh.pdf"));
            pdfDocument.open();
            
            PdfContentByte cb = pdfWriter.getDirectContent();
            ColumnText ct = new ColumnText(cb);
            float[] right = {10, 110, 210};
            float[] left = {310, 410, 510};
            ct.setIndent(20);

            int status = ColumnText.START_COLUMN;
            int column = 0;
            float pos;

            //ct.setSimpleColumn(36, 36, PageSize.A4.getWidth() - 36, PageSize.A4.getHeight() - 36, 18, Element.ALIGN_JUSTIFIED);
            ct.setSimpleColumn(left[column], 36, right[column], PageSize.A4.getHeight() - 36);
            
            while ((line = bufferReader.readLine()) != null)
            {
                Font fnt = new Font();
                fnt.setSize(5);
                Phrase p = new Phrase(line, fnt);
                ct.addText(p);
                pos = ct.getYLine();
                status = ct.go(true);
                //System.out.println(column);
                
                if (!ColumnText.hasMoreText(status))
                {
                    ct.addText(p);
                    ct.setYLine(pos);
                    ct.go(false);
                }
                else
                {
                    ct.setSimpleColumn(left[column], 36, right[column], PageSize.A4.getHeight() - 36);
                    //pdfDocument.newPage();
                    ct.setText(p);
                    ct.setYLine(PageSize.A4.getHeight() - 36);
                    ct.go();
                    column++;
                }

                
                //status = ct.go();
                //column++;
                if (column > 2)
                {
                    column = 0;
                    pdfDocument.newPage();
                    System.out.println(line);
                }
                //System.out.println("status: " + status);
                //System.out.println("Column " + ColumnText.hasMoreText(status));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            pdfDocument.close();
            bufferReader.close();
        }

        
    }
}