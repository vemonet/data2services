/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.service.ReadEntitiesFromEndPoint;
import nl.unimaas.ids.rdf2api.io.utils.FileReader;

/**
 *
 * @author nuno
 */
public class MetaDataSourcesPathHandler extends AbstractPathHandler{
    
    private String pathHandlerModel = "/metadata/sources/";
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
        FileReader fr = new FileReader("MetaData-Sources.ql");
        
        String query = fr.read();
        data = readEntities.execute(query);
        
        return data;
    }
    
}
