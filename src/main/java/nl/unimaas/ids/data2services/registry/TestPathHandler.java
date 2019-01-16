/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.unimaas.ids.data2services.model.PathElement;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.QueryVariable;
import nl.unimaas.ids.data2services.model.ServiceRealm;
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
    public String process(String path) {
        
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
               
               String response;
               try {
                   response = this.httpConnect.sendPost(this.endpointURL, sQuery);
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
    
    //currently encoding to base64
    private String decodeVariable(String txt){
        byte[] decodedData = Base64.getDecoder().decode(txt);
        return new String(decodedData);
    }
    
}
