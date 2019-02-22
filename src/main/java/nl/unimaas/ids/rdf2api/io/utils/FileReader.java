package nl.unimaas.ids.rdf2api.io.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

/**
 *
 * @author nuno
 */
public class FileReader {
 
 private String filename = "";
 private String fileContent = "";
    
 public FileReader(String filename){
     this.filename = filename;
 }
 
 public String read(){
     try {
         InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
         //Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)
         fileContent = IOUtils.toString(inputStream);
         } catch (IOException ex) {
               Logger.getLogger(FileReader.class.getName()).log(Level.SEVERE, null, ex);
         }
         return fileContent;
 }
 
}
