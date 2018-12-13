package nl.unimaas.ids.data2services.registry;

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
    private String pathHandlerModel = "";
    
    
    public void setServiceRealm(ServiceRealm serviceDomain){
        this.realm = realm;
    }
    
    public ServiceRealm getServiceRealm(){
        return realm;
    }
    
    public void setPathHandlerModel(String pathHandlerModel){
        this.pathHandlerModel = pathHandlerModel;
    }
    
    public String getPathHandlerModel(){
        return pathHandlerModel;
    }
    
    public void setupSwaggerOperation(){
          SwaggerTest.getInstance().registerOperation(this.realm, this.pathHandlerModel);
    }
    
    public abstract String process(String path);
}
