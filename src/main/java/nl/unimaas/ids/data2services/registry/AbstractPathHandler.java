package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.List;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.util.SwaggerManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nuno
 */
public abstract class AbstractPathHandler {
    
    private ServiceRealm realm = new ServiceRealm();
    private List<Query> queryList = new ArrayList<Query>();
    
    
    public void setServiceRealm(ServiceRealm serviceDomain){
        this.realm = serviceDomain;
    }
    
    public ServiceRealm getServiceRealm(){
        return realm;
    }
    
    public void addQuery(Query query){
        this.queryList.add(query);
    }
    
    public List<Query> getQueryList(){
        return this.queryList;
    }
    
    public void setupSwaggerOperation(){
          for(Query query : this.queryList){
                //System.out.println("registering for "+pathHandler);
                //System.out.flush();
                SwaggerManager.getInstance().registerOperation(this.getServiceRealm(), query);
          }
    }
        
    //fixit (find a way of passing other context variables)
    public abstract String process(String path);
    public abstract String process(String path, String contentType); //improve concept

}
