 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author Colin
 */
public class WikiRestart {
    int startVol = 0;
    int startLimit = 0;
    int startPage = 0;
    
    public WikiRestart(File[] folder) throws NumberFormatException{
        try {
            File lastFile = folder[folder.length - 1];
            for(File file : folder)
            {
                System.out.println(file.getCanonicalFile() + "\n");
            }
            String pathName = lastFile.getCanonicalPath();
            String[] pathArray = pathName.split("/");
            String lastOutput = pathArray[pathArray.length - 1];
            String[] settings = lastOutput.split("[-.]");
            startVol = Integer.parseInt(settings[1]);
            startLimit = Integer.parseInt(settings[settings.length - 3]);
            startPage = Integer.parseInt(settings[settings.length - 2]);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public int getRestartLimit(){
        return startLimit;
    }
    
    public int getRestartVol(){
        return startVol + 1;
    }
    
    public int getRestartPage(){
        return startPage;
    }
}