/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nuno
 */
public class RegistryPathHandler {
    
    Map<String, PathHandler> phMap = new HashMap();
    
    
    public void registerHandler(PathHandler ph){
        String domain = ph.getDomain();
        phMap.put(domain, ph);
    }
        
    public PathHandler getHandler(String domain, String path){
        return this.phMap.get(domain);
    }
}
