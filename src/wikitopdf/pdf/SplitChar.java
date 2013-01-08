/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wikitopdf.pdf;

import com.lowagie.text.SplitCharacter;
import com.lowagie.text.pdf.PdfChunk;

/**
 *
 * @author Denis Lunev
 */
public class SplitChar implements SplitCharacter {

    public boolean isSplitCharacter(int arg0, int arg1, int arg2, char[] arg3, PdfChunk[] arg4) {
        //It means no split char
        return false;
    }

}
