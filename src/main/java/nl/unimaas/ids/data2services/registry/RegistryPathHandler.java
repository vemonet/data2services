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
    
    //TODO make map ServiceRealm by implementing equals
    private Map<String, AbstractPathHandler> phMap = new HashMap<String, AbstractPathHandler>();
    
    
    public void registerHandler(AbstractPathHandler ph){
        ServiceRealm realm = ph.getServiceRealm();
        System.out.println("realm "+ realm.getRealm().get());
        phMap.put(realm.getRealm().get(), ph);
    }
        
    public AbstractPathHandler getHandler(ServiceRealm realm, String path){
        return this.phMap.get(realm.getRealm().get());
    }
}
