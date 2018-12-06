/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nuno
 */
public class RegistryPathHandler {
    
    Map<String, AbstractPathHandler> phMap = new HashMap<String, AbstractPathHandler>();
    
    
    public void registerHandler(AbstractPathHandler ph){
        String domain = ph.getDomain();
        phMap.put(domain, ph);
    }
        
    public AbstractPathHandler getHandler(String domain, String path){
        return this.phMap.get(domain);
    }
}
