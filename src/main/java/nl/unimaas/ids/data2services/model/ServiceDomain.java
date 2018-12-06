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
public class ServiceDomain {
    String serviceDomain = null;

    public ServiceDomain(String serviceDomain) {
        this.serviceDomain = serviceDomain;
    }

    public ServiceDomain() {
        serviceDomain = null;
    }
    
    public Optional<String> getDomain(){
        return Optional.ofNullable(serviceDomain);
    }
}
