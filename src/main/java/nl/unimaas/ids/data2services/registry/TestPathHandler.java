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
    
    public TestPathHandler(){
        
        this.setServiceRealm(new ServiceRealm("test"));
        
        QueryParser qp = new QueryParser();
        this.queryList = qp.getQueryList();
        
        for(Query query : queryList){
            this.addQuery( query );
        }
        
        setupSwaggerOperation();
    }
    
    
    private void readQueries(){
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
                       qv.setValue(  decodeVariable(pathElementList.get(n).getLabel())  );
                       
                       queryVariableList.add(qv);
                   }
                   n++;
               }
                 
               //List<QueryVariable> queryVariableList = this.queryList.get(i).getVariables();
               
               
               for(QueryVariable queryVariable : queryVariableList){
                   System.out.println(queryVariable.getId() + " " + queryVariable.getValue());
                   sQuery = sQuery.replaceAll("\\?_"+ queryVariable.getId(),  queryVariable.getValue());
               }
               System.out.println(sQuery);
               
               String finalQuery = sQuery;
               //String[] queryLines = sQuery.split("\n");
               //for(String queryLine : queryLines){
               //    for(QueryVariable queryVariable : queryVariableList){
               //        if(!queryLine.trim().startsWith(queryVariable.getValue())){
               //            finalQuery += queryLine+"\n";
               //        }
               //    }
               //}               
               
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
               return response;
           }
           i++;
        }
        
        return "path not found";
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
    
    //currently decoding to base64 or expands prefix
    private String decodeVariable(String txt){
        // check if it's base64
        try {
               byte[] decodedData = Base64.getDecoder().decode(txt);
               return new String(decodedData);
        } catch(IllegalArgumentException iae) {}
        
        // expand prefix ()
        String uri;
        uri = URI2Prefix.prefixToUri(txt);
        return uri;
      
    }
    
}
