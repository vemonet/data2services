/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.model;

import java.util.Optional;

/**
 *
 * @author nuno
 */
public class ServiceRealm {
    private String serviceRealm = null;

    public ServiceRealm(String serviceDomain) {
        this.serviceRealm = serviceDomain;
    }

    public ServiceRealm() {
        serviceRealm = null;
    }
    
    public Optional<String> getRealm(){
        return Optional.ofNullable(serviceRealm);
    }
}
