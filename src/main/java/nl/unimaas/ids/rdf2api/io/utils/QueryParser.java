/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.rdf2api.io.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.QueryVariable;

/**
 *
 * @author nuno
 */
public class QueryParser {
    
    
    public static void main(String [] args){
       QueryParser qp = new QueryParser();
    }
    
    
    private final static String VARPATTERN = "\\{\\{(.*?)\\}\\}";
    private FileReader fileReader;
    private List<Query> queryList = new ArrayList<Query>();
    
    public QueryParser(){
        fileReader = new FileReader("MetaData-Sources.ql");
        
        String fileContent = fileReader.read();
        parse(fileContent); 
    }
    
    public List<Query> getQueryList(){
        return this.queryList;
    }
    
    private void parse(String content){
        String[] contentSplit = content.split("/n/n");
        
        for(String sQuery : contentSplit){
            Query query = new Query();
            
            String[] queryStringArray = sQuery.split("/n");
            String rawQuery = "";
            
            for(int i = 0; i < queryStringArray.length; i++){
                if(i==0)
                    query.setLabel(queryStringArray[0]);
                else{
                   rawQuery += queryStringArray[i];
                }
            }
            
            query.setRawQuery(rawQuery);
            
            List<QueryVariable> variableList = parseVariables(rawQuery);
            query.setVariableList(variableList);
            
            this.queryList.add(query);
                    
            }
        }  
        
        private List<QueryVariable> parseVariables(String rawQuery){
            
            List<QueryVariable> variableList = new ArrayList();
            
            Pattern pattern = Pattern.compile(this.VARPATTERN);
            Matcher matcher = pattern.matcher(rawQuery);
            
            QueryVariable qv = null;
            while (matcher.find())
            {
                String variableName = matcher.group(1);
                System.out.println(matcher.group(1));
                
                qv = new QueryVariable();
                qv.setLabel(variableName);
                
                variableList.add(qv);
            }
           
         return variableList;
        }
    
    
    
}
