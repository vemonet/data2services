/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services;

import nl.unimaas.ids.data2services.registry.RegistryPathHandler;
import nl.unimaas.ids.data2services.registry.BiolinkPathHandler;
import nl.unimaas.ids.data2services.util.SwaggerManager;

/**
 *
 * @author nuno
 */
public class Test {
    public static void main(String [] args)
	{
            SwaggerManager swaggerTest = SwaggerManager.getInstance();
        
            RegistryPathHandler registryPathHandler = new RegistryPathHandler();
            registryPathHandler.registerHandler(new BiolinkPathHandler());
            
            System.out.println("testing..");
            System.out.println(swaggerTest.getSwaggerJson());
	}

}
