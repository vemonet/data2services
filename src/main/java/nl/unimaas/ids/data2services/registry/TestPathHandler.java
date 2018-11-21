/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

/**
 *
 * @author nuno
 */
public class TestPathHandler extends AbstractPathHandler{
    
    public TestPathHandler(){
        this.setDomain("test");
        this.setPathHandlerModel("/{test}/{test2}/{test3}");
        setupSwaggerOperation();
    }
    
    
    @Override
    public String process(String path) {
        return "this is a test for "+path;
    }
    
}
