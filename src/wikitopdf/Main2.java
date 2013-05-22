//DEPRECATED MAY 2013


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wikitopdf;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class Main2
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Document doc = new Document();

        try
        {
            
            PdfCopy cp = new PdfCopy(doc, new FileOutputStream("big.pdf"));
            for (int i = 0; i < 130; ++i)
            {
                String fileName = (1000 * i) + "output.pdf";
                File someFile = new File(fileName);
                InputStream s = new FileInputStream(someFile);
                byte arrayIn[] = RandomAccessFileOrArray.InputStreamToArray(s);

                PdfReader r = new PdfReader(arrayIn);
                // do rest of process

                s.close();


                //PdfReader r = new PdfReader(new RandomAccessFileOrArray(fileName), null);
                //PdfReader r = new PdfReader(fileName);
                for (int k = 1; k <= r.getNumberOfPages(); ++k)
                {
                    cp.addPage(cp.getImportedPage(r, k));
                }

                cp.freeReader(r);

                System.out.println(i + ": " + (Runtime.getRuntime().freeMemory()));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            doc.close();
        }
    }
}
