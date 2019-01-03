/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.model;

import java.util.List;

/**
 *
 * @author nuno
 */
public class Query {
    private String label;
    private String rawQuery;
    private List<QueryVariable> variableNameList;

    
    public List<QueryVariable> variableNameList(){
        return variableNameList;
    }
    
    public void addVariableName(QueryVariable variableName){
        variableNameList.add(variableName);
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

    /**
     * @return the rawQuery
     */
    public String getRawQuery() {
        return rawQuery;
    }

    /**
     * @param rawQuery the rawQuery to set
     */
    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public void setVariableList(List<QueryVariable> variableList) {
        this.variableNameList = variableList;
    }
}
