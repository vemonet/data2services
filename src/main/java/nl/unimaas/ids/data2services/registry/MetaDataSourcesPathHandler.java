/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.service.ReadEntitiesFromEndPoint;

/**
 *
 * @author nuno
 */
public class MetaDataSourcesPathHandler extends AbstractPathHandler{
    
    String pathHandlerModel = "/metadata/sources/";
    private ReadEntitiesFromEndPoint readEntities;
    
    
    public MetaDataSourcesPathHandler(){
        this.setServiceRealm(new ServiceRealm("test"));
        this.setPathHandlerModel(this.pathHandlerModel);
        setupSwaggerOperation();
        
        //init data access
        readEntities = new ReadEntitiesFromEndPoint();
    }
    
    
    @Override
    public String process(String path) {
        String data = readEntities.metadataSources();
        return data;
    }
    
}
