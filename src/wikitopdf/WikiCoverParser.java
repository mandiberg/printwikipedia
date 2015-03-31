/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf;

import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileFilter;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import scala.actors.threadpool.Arrays;
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

//      String use_folder = "output";
        String use_folder = "temp";
        File folder = new File(use_folder);
        File[] listOfFiles = folder.listFiles();
       // Arrays.sort(listOfFiles);
//        for(int i = 0; i<listOfFiles.length-1; i++){
//            String[] titleArrOne = listOfFiles[i].getName().split("&&&");
//            String[] titleArrTwo = listOfFiles[i+1].getName().split("&&&");
//            int thisVol = Integer.parseInt(titleArrOne[0]);
//            int nextVol = Integer.parseInt(titleArrTwo[0]);
//            if(thisVol>nextVol){
//                
//            }
//        }
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
              // need to filter out .ds_store and other "." files
            System.out.println("File " + listOfFiles[i].getName());
                try
                {
                    System.out.println("Trying " + listOfFiles[i].getName());
                    pdfWrapper.addCover(listOfFiles[i].getName(), use_folder); // will parse file name inside pdfWrapper
                    
                }   
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    pdfWrapper.close();
                    //System.out.println(num + " this is what num is now");
                    num++;
                   // System.out.println(num + " this is what num is after adding");
                    pdfWrapper = new PdfCoverWrapper(num, pagesCount);

                }
            

          
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
        

    }    
}
