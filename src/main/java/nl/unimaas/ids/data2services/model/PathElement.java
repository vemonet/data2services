/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.model;

/**
 *
 * @author nuno
 */
public class PathElement {
    private Boolean isVariable;
    private String label = "";
    
    public PathElement(String label, Boolean isVariable){
        this.label = label; 
        this.isVariable = isVariable;
    }

    /**
     * @return the isVariable
     */
    public Boolean isVariable() {
        return isVariable;
    }

    /**
     * @param isVariable the isVariable to set
     */
    public void isVariable(Boolean isVariable) {
        this.isVariable = isVariable;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    
}
