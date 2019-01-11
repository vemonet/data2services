/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.List;
import nl.unimaas.ids.data2services.model.PathElement;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.QueryVariable;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.rdf2api.io.utils.QueryParser;

/**
 *
 * @author nuno
 */
public class TestPathHandler extends AbstractPathHandler{
    
    List<Query> queryList;
    
    public TestPathHandler(){
        
        this.setServiceRealm(new ServiceRealm("test"));
        
        QueryParser qp = new QueryParser();
        this.queryList = qp.getQueryList();
        
        for(Query query : queryList){
            this.addPathHandlerModel( query.getPath("{varName}") );
        }
        
        setupSwaggerOperation();
    }
    
    
    private void readQueries(){
    }
    
    @Override
    public String process(String path) {
        List<String> pathHandlerModelList = this.getPathHandlerModelList();
        
        int i = 0;
        for(String pathHandlerModel : pathHandlerModelList){
           if(matchPath(path, pathHandlerModel)){
               
               String query = this.queryList.get(i).getRawQuery();
               
               List<PathElement> pathElementList = decomposePath(path);
               List<PathElement> pathModelElementList = decomposePath(pathHandlerModel);
               
               List<QueryVariable> queryVariableList = new ArrayList<QueryVariable>();
               int n = 0;
               for(PathElement pathElement : pathModelElementList){
                   if(pathElement.isVariable()){
                       QueryVariable qv = new QueryVariable();
                       qv.setId("?_"+pathElement.getLabel());
                       qv.setLabel(pathElement.getLabel());
                       qv.setValue(pathElementList.get(n).getLabel());
                       
                       queryVariableList.add(qv);
                   }
                   n++;
               }
                 
               //List<QueryVariable> queryVariableList = this.queryList.get(i).variableNameList();
              
               for(QueryVariable queryVariable : queryVariableList){
                   query = query.replaceAll("\\?_"+ queryVariable.getLabel(),  queryVariable.getValue());
               }
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
            if(!modelPathElementList.get(i).isVariable())
                if(modelPathElementList.get(i).getLabel()!=requestedPathElementList.get(i).getLabel())
                    return false;
        }
        
        return true;   
    }
    

    
    private List<PathElement> decomposePath(String path){
        String[] splitPath = path.split("/");
        List<PathElement> pathElementList = new ArrayList<PathElement>();
        for(String sPathElement : splitPath){
            PathElement pathElement = new PathElement(sPathElement, sPathElement.charAt(0)=='{');
            pathElementList.add(pathElement);
        }
        return pathElementList;
    }
    
}
