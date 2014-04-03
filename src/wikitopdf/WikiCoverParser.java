/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import wikitopdf.pdf.PdfCoverWrapper;

/**
 *
 * @author Home
 */
public class WikiCoverParser {

    
    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     * @throws DocumentException
     * @throws ParseException
     */
    public void parseCover() throws FileNotFoundException, IOException, DocumentException, ParseException
    {
        
        int num = 1;
        int pagesCount = 0;
        
        PdfCoverWrapper pdfWrapper = new PdfCoverWrapper(num, pagesCount);
/*
 * 
 * CHANGE THE FOLLOWING LINES TO MAKE COVERS FOR PDF(output) VS. TOC(temp)
 * 
 */
        
//        File folder = new File("output");
        File folder = new File("temp");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
              // need to filter out .ds_store and other "." files
            System.out.println("File " + listOfFiles[i].getName());
                try
                {
                System.out.println("Trying " + listOfFiles[i].getName());
                   
                    pdfWrapper.addCover(listOfFiles[i].getName()); // will parse file name inside pdfWrapper
                    
                }   
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    pdfWrapper.close();
                    num++;
                    pdfWrapper = new PdfCoverWrapper(num, pagesCount);

                }
            

          
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
        

    }    
}
