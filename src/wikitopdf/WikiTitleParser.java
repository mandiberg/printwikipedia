package wikitopdf;

import com.lowagie.text.DocumentException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import wikitopdf.pdf.PdfTitleWrapper;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiTitleParser
{
    /**
     *
     * @param inputFileName
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     * @throws ParseException
     */
    public void parseTxt(String inputFileName) throws FileNotFoundException, IOException, DocumentException, ParseException
    {
        //long startDate = new Date().getTime();

        FileInputStream inputStream = new FileInputStream(inputFileName);
        BufferedReader bufferReader = null;
        
        bufferReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        String line = "";
        /*
        SQLProcessor sqlProcessor = null;
        try
        {
            sqlProcessor = new SQLProcessor();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(WikiParser.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
        int num = 1;
        int pagesCount = 0;
        PdfTitleWrapper pdfWrapper = new PdfTitleWrapper(num, pagesCount);
        pdfWrapper.addPrologue();
        try
        {
            //while()
            {   
                //start writing the first volume
                pdfWrapper.openMultiColumn();
                
                
                while ((line = bufferReader.readLine()) != null)
                {
                    //sqlProcessor.saveTitle(line);
                    
                    //if the pages in the current volume == 670, close current pdf, track volume/page numbers, and start new pdf
                    if(pdfWrapper.getPagesCount() % 670 == 0 && pdfWrapper.getTitlesCount() > 100)
                    {
                        System.out.println("file " + num);
                        pagesCount += pdfWrapper.getPagesCount();
                        pdfWrapper.close();
                        num ++;
         
                        pdfWrapper = new PdfTitleWrapper(num, pagesCount);
                        pdfWrapper.openMultiColumn(); // starts new volume (starting from vol 2)
                    }
                    
                    //while there are lines, write the line
                    pdfWrapper.writeTitle(line);
                }
            }
        } 
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            //sqlProcessor.close();
            //pdfWrapper.closeMultiColumn();
            pdfWrapper.close();
            bufferReader.close();
        }
    }

    //private String inputFileName = "";
}
