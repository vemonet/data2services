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
    
    //pattern {{variable}}
    //private final static String VARPATTERN = "\\{\\{(.*?)\\}\\}";
    //pattern ?_variable
    private final static String VARPATTERN = "\\?_([a-zA-Z0-9_]+)";
    private FileReader fileReader;
    private List<Query> queryList = new ArrayList<Query>();
    
    public static void main(String [] args){
       QueryParser qp = new QueryParser();
       int i = 0;
       for(Query query : qp.getQueryList()){
            System.out.println("query " + ++i);
            System.out.println("label: "+query.getLabel());
            System.out.println("query: "+query.getRawQuery());
            System.out.println("variable list:");
            
            for(QueryVariable qvariable : query.getVariables()){
                System.out.print(qvariable.getLabel() + " " + qvariable.getId() + "; ");
            }
            
            System.out.println("");
       }
    }
    
    public QueryParser(){
        fileReader = new FileReader("queries.rq");
        
        String fileContent = fileReader.read();
        parse(fileContent); 
    }
    
    public List<Query> getQueryList(){
        return this.queryList;
    }
    
    private void parse(String content){
        String[] contentSplit = content.split("\n\n");
        
        for(String sQuery : contentSplit){
            Query query = new Query();
            
            String[] queryStringArray = sQuery.split("\n");
            String rawQuery = "";
            
            for(int i = 0; i < queryStringArray.length; i++){
                if(i==0){
                    query.setLabel(this.cleanComment( queryStringArray[0] ));
                }else if(i==1){
                    query.setPath(this.cleanComment( queryStringArray[1] ));
                }else if(i==3){
                    String sTags = this.cleanComment( queryStringArray[2] );
                    String[] tags = sTags.split(",");
                                       
                    for(String s: tags)
                        query.addTag(s.trim());     
                    
                }else{
                    rawQuery += queryStringArray[i] + "\n";
                }
            }
            
            query.setRawQuery(rawQuery);
            
            List<QueryVariable> variableList = parseVariables(rawQuery);
            query.setVariableList(variableList);
            
            this.queryList.add(query);
                    
            }
        }  
        
        private List<QueryVariable> parseVariables(String rawQuery){
            
            List<QueryVariable> variableList = new ArrayList<QueryVariable>();
            
            Pattern pattern = Pattern.compile(this.VARPATTERN);
            Matcher matcher = pattern.matcher(rawQuery);
            
            QueryVariable qv = null;
            while (matcher.find())
            {
                String variableName = matcher.group(1);
                System.out.println(matcher.group(1));
                
                qv = new QueryVariable();
                qv.setLabel(variableName);
                qv.setId(matcher.group(0), true);
                
                variableList.add(qv);
            }
           
         return variableList;
        }
    
        private String cleanComment(String txt){
            String cleanTxt = txt.substring(1);
            return cleanTxt.trim();
        }
    
    
}
