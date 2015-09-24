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
        String lastOutput = "";
        String[] pathArray = null;
        try {
            for(int i=folder.length-1;i>-1;i--){
               File lastFile = folder[i];
               String pathName = lastFile.getCanonicalPath();
               pathArray = pathName.split("/");
               lastOutput = pathArray[pathArray.length - 1];
               if(lastOutput.startsWith("_")){
                   lastOutput = lastOutput.substring(1, lastOutput.length());
                   System.out.println(lastOutput);
                   break;
               }
                else{
                    continue;
                }
            }
            
            
            
            String[] settings = lastOutput.split("[-.]");
            startVol = Integer.parseInt(settings[1]);
            System.out.println(startVol);
            startLimit = Integer.parseInt(settings[0]);
            startPage = Integer.parseInt(settings[2]);
            settings = null;
            pathArray = null;
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
        return startVol;
    }
    
    public int getRestartPage(){
        return startPage;
    }
}