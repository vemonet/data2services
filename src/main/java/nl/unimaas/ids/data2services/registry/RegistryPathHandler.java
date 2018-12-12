/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.HashMap;
import java.util.Map;
import nl.unimaas.ids.data2services.model.ServiceRealm;

/**
 *
 * @author nuno
 */
public class RegistryPathHandler {
    
    Map<ServiceRealm, AbstractPathHandler> phMap = new HashMap<ServiceRealm, AbstractPathHandler>();
    
    
    public void registerHandler(AbstractPathHandler ph){
        ServiceRealm realm = ph.getServiceRealm();
        phMap.put(realm, ph);
    }
        
    public AbstractPathHandler getHandler(String domain, String path){
        return this.phMap.get(domain);
    }
}
