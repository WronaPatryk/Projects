package gui;

import javax.swing.filechooser.FileFilter;

import java.io.File;

public class WordFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {

        // AKCEPTACJA KATALOGÓW
        if(f.isDirectory()) return true;

        String name = f.getName();

        String extension = getExtension( name );
        if(extension == null){
            return false;
        }
        if(extension.equals( "wd" )){
            return true;
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Word database files (*.wd)";
    }

    // BONUS METHOD
    public static String getExtension(String fileName) {
        char ch;
        int len;
        if(fileName==null ||
                (len = fileName.length())==0 ||
                (ch = fileName.charAt(len-1))=='/' || ch=='\\' || //in the case of a directory
                ch=='.' ) //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if( dotInd<=sepInd )
            return "";
        else
            return fileName.substring(dotInd+1).toLowerCase();
    }




}
