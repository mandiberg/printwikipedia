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
 * CHANGE THE FOLLOWING LINES TO MAKE COVERS FOR PDF(output) or TOC(temp) or contrib ;)
 * 
 */
        
//          String use_folder = "output";
//        String use_folder = "temp";
        String use_folder = "contrib";
        File folder = new File(use_folder);
        File[] listOfFiles = folder.listFiles();

        String check_str = "";
        for (int i = 0; i < listOfFiles.length; i++) {
          if (listOfFiles[i].isFile()) {
            String cur_file = listOfFiles[i].getName();
            if(listOfFiles[i].getName().length() > 4){
                check_str = cur_file.substring(0,3);
            }
            if(check_str.contains("pre") == false || check_str.contains("_")==false){ //if the files in the folder you're iterating through are not pre files or DS_STORE then go on.
                try
                {
                    System.err.println(listOfFiles[i].getName());//make sure you're on the right path here.
                    if(listOfFiles[i].getName()==".DS_Store")
                        continue;
                    num = Integer.parseInt(listOfFiles[i].getName().split("&&&")[0]);
                    pdfWrapper = new PdfCoverWrapper(num, pagesCount);
                    pdfWrapper.addCover(listOfFiles[i].getName(), use_folder); // will parse file name inside pdfWrapper
                    pdfWrapper.close();
                    
                }   
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally
                {
                    

                }
            }
            

          
          } else if (listOfFiles[i].isDirectory()) {
            System.out.println("Directory " + listOfFiles[i].getName());
          }
        }
        

    }    
}
