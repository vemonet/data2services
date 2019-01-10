package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.List;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.util.SwaggerTest;

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
    private List<String> pathHandlerModelList = new ArrayList<String>();
    
    
    public void setServiceRealm(ServiceRealm serviceDomain){
        this.realm = realm;
    }
    
    public ServiceRealm getServiceRealm(){
        return realm;
    }
    
    public void addPathHandlerModel(String pathHandlerModel){
        this.pathHandlerModelList.add(pathHandlerModel);
    }
    
    public List<String> getPathHandlerModelList(){
        return this.pathHandlerModelList;
    }
    
    public void setupSwaggerOperation(){
          for(String pathHandler : this.pathHandlerModelList)
            SwaggerTest.getInstance().registerOperation(this.realm, pathHandler);
    }
    
    public abstract String process(String path);
}
