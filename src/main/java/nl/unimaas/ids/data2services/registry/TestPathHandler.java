/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nl.unimaas.ids.data2services.model.PathElement;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.QueryVariable;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.util.URI2Prefix;
import nl.unimaas.ids.rdf2api.io.utils.HttpURLConnect;
import nl.unimaas.ids.rdf2api.io.utils.QueryParser;

/**
 *
 * @author nuno
 */
public class TestPathHandler extends AbstractPathHandler{
    
    List<Query> queryList;
    private HttpURLConnect httpConnect = new HttpURLConnect(); 
    private String endpointURL = "http://graphdb.dumontierlab.com/repositories/ncats-red-kg";
    
    
    public static void main(String [ ] args){
        new TestPathHandler().postProcess("hello");
    }
    
    public TestPathHandler(){
        
        this.setServiceRealm(new ServiceRealm("test"));
        
        QueryParser qp = new QueryParser();
        this.queryList = qp.getQueryList();
        
        for(Query query : queryList){
            this.addQuery( query );
        }
        
        setupSwaggerOperation();
    }
    
    @Override
    public String process(String path){
        //tempfix
        return this.process(path, null);
    }
    
    @Override
    public String process(String path, String acceptHeader) {
        
        if(acceptHeader == null)
            acceptHeader = "application/sparql-results+json";
        
        List<Query> queryList = this.getQueryList();
        
        int i = 0;
        for(Query query : queryList){
           if(matchPath(path, query.getPath())){
               
//               return "true";
               
               String sQuery = this.queryList.get(i).getRawQuery();
               
               List<PathElement> pathElementList = decomposePath(path);
               List<PathElement> pathModelElementList = decomposePath(query.getPath());
               
               List<QueryVariable> queryVariableList = new ArrayList<QueryVariable>();
               int n = 0;
               for(PathElement pathElement : pathModelElementList){
                   if(pathElement.isVariable()){
                       QueryVariable qv = new QueryVariable();
                       qv.setId(pathElement.getLabel(), true);
                       qv.setLabel(pathElement.getLabel());
                       //qv.setValue(  decodeVariable(pathElementList.get(n).getLabel())  );
                       qv.setValue(  pathElementList.get(n).getLabel() );
                       qv.setRawValue(pathElementList.get(n).getLabel());
                       
                       queryVariableList.add(qv);
                   }
                   n++;
               }
                 
               //List<QueryVariable> queryVariableList = this.queryList.get(i).getVariables();
               
               
               for(QueryVariable queryVariable : queryVariableList){
                   System.out.println(">>>>>    "+ queryVariable.getId() + " " + queryVariable.getValue());
                   sQuery = sQuery.replaceAll("\\?_"+ queryVariable.getId(),  queryVariable.getValue());
               }
               System.out.println(sQuery);
               
               String finalQuery = sQuery;
            //   String[] queryLines = sQuery.split("\n");
            //   for(String queryLine : queryLines){
             //      for(QueryVariable queryVariable : queryVariableList){
             //          if(queryLine.startsWith("?!="+queryVariable.getRawValue())){
             //  
             //          } else {
             //              queryLine = queryLine.replaceFirst("?!="+queryVariable.getRawValue(), sQuery);
             //              finalQuery += queryLine+"\n";
             //          }
             //      }
              // }               
               
               //prepare hashmap
               HashMap<String, String> headers = new HashMap<String, String>();
               headers.put("Accept", acceptHeader);
               
               
               String response;
               try {
                   response = this.httpConnect.sendPost(this.endpointURL, finalQuery, headers);
               } catch (Exception ex) {
                   Logger.getLogger(TestPathHandler.class.getName()).log(Level.SEVERE, null, ex);
                   return "processing issues";
               }
               
               response = postProcess(response);
               
               return response;
           }
           i++;
        }
        
        return "path not found";
    }
    
    private String postProcess(String response){
        // Query to get the prefixes/namespace from the GraphDB triplestore
        String query = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                        "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n" +
                        "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n" +
                        "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "PREFIX idot: <http://identifiers.org/idot/>\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX void: <http://rdfs.org/ns/void#>\n" +
                        "PREFIX d2svocab: <https://w3id.org/data2services/vocab/>\n" +
                        "PREFIX bl: <http://w3id.org/biolink/vocab/>\n" +
                        "SELECT ?prefix ?namespace {\n" +
                        "    GRAPH <https://w3id.org/data2services/graph/prefixes> {\n" +
                        "        [] a d2svocab:Prefix ;\n" +
                        "            d2svocab:prefix ?prefix;\n" +
                        "            d2svocab:namespace ?namespace .\n" +
                        "    }\n" +
                        "}";
        
        String keyValueString = "";
        try {
           HashMap<String, String> headers = new HashMap<String, String>();
           headers.put("Accept", "text/tab-separated-values");
           keyValueString = this.httpConnect.sendPost(this.endpointURL, query, headers);
                      
           HashMap<String, String> map = tsv2hash(keyValueString);
           
           for(String key: map.keySet()){
               String value = map.get(key);
               System.out.println(">>>>>>>>>>>>>> replacing " + key + " with " + value);
               response = response.replaceAll(Pattern.quote(value), key);
           }
           
        } catch (Exception ex) {
            Logger.getLogger(TestPathHandler.class.getName()).log(Level.SEVERE, null, ex);
            return response;
        }
        
        return response;
    }
    
    
    private boolean matchPath(String requestedPath, String modelPath){
        List<PathElement> requestedPathElementList = decomposePath(requestedPath);
        List<PathElement> modelPathElementList = decomposePath(modelPath);
        
        if(requestedPathElementList.size()!=modelPathElementList.size()) return false;
        
        
        for(int i = 0; i < requestedPathElementList.size(); i++){
            if(!modelPathElementList.get(i).isVariable()){
                String modelPathElementString = modelPathElementList.get(i).getLabel();
                String requestedPathElementString = requestedPathElementList.get(i).getLabel();
                
                System.out.println("comparing -"+modelPathElementString + "- and -" + requestedPathElementString + "-");
                
                if( ! modelPathElementString.equals(requestedPathElementString) )
                    return false;
            }
        }
        return true;   
    }
    
    private List<PathElement> decomposePath(String path){
        System.out.println("decomposing path "+ path + "<-");
        System.out.flush();
        path = path.charAt(0) == '/' ? path.substring(1) : path;
            
        String[] splitPath = path.split("/");
        List<PathElement> pathElementList = new ArrayList<PathElement>();
        for(String sPathElement : splitPath){
            PathElement pathElement = new PathElement(sPathElement, sPathElement.charAt(0)=='{');
            pathElementList.add(pathElement);
        }
        return pathElementList;
    }
    
    // decoding to base64 or expands prefix
    private String decodeVariable(String txt){
        
        // check if it's base64
        try {
               byte[] decodedData = Base64.getDecoder().decode(txt);
               return new String(decodedData);
        } catch(IllegalArgumentException iae) {}
        
        // expand prefixes to full URI's
        String uri;
        uri = URI2Prefix.prefixToUri(txt);
        return uri;
      
    }
    
    private HashMap<String, String> tsv2hash(String txt){
        HashMap<String,String> map = new HashMap<String,String>();
        String[] line = txt.split("\n");
        String str[];
              
        for(int i = 0; i < line.length; i++){
            if(i>0){
                System.out.println(line[i]);
                str = line[i].split("\t");
                System.out.println(">>>>> "+ str[0] +" - " + str[1]);
                map.put(str[0], str[1]);
            }
        }
        return map;
    }
    
}
