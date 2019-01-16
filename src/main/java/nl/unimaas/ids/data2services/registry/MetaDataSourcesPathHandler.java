/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.List;
import nl.unimaas.ids.data2services.model.PathElement;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.service.ReadEntitiesFromEndPoint;
import nl.unimaas.ids.rdf2api.io.utils.FileReader;
import nl.unimaas.ids.rdf2api.io.utils.HttpURLConnect;

/**
 *
 * @author nuno
 */
public class MetaDataSourcesPathHandler extends AbstractPathHandler{
    
    private String pathHandlerModel = "/metadata/sources/";
    private ReadEntitiesFromEndPoint readEntities;
    
    private HttpURLConnect httpConnect = new HttpURLConnect();  
    private String endpointURL = "http://graphdb.dumontierlab.com/repositories/ncats-red-kg";
    
    
    public MetaDataSourcesPathHandler(){
        this.setServiceRealm(new ServiceRealm("test"));
        this.addQuery(this.pathHandlerModel);
        setupSwaggerOperation();
        
        //init data access
        readEntities = new ReadEntitiesFromEndPoint();
    }
    
     
    @Override
    public String process(String path){
        try {
               // matchPath(path, );
            
                String r = this.httpConnect.sendPost(this.endpointURL, "");
                return r;
        } catch (Exception ex) {
               //TODO setup logging properly
               //Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
               return "error"; // replace with custom error
        }
    }
    

    
}
