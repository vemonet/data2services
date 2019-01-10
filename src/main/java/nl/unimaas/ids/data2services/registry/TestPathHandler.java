/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.List;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.rdf2api.io.utils.QueryParser;

/**
 *
 * @author nuno
 */
public class TestPathHandler extends AbstractPathHandler{
    
    public TestPathHandler(){
        
        QueryParser qp = new QueryParser();
        List<Query> queryList = qp.getQueryList();
        
        this.setServiceRealm(new ServiceRealm("test"));
        
        for(Query query : queryList){
            this.addPathHandlerModel( query.getPath("{varName}") );
        }
        
        setupSwaggerOperation();
    }
    
    
    private void readQueries(){
    }
    
    @Override
    public String process(String path) {
        return "this is a test for "+path;
    }
    
}
