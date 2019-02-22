/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nuno
 */
public class Query {
    private String label;
    private String rawQuery;
    private String path;
    private List<QueryVariable> variableNameList = new ArrayList<QueryVariable>();
    private List<String> tagList = new ArrayList<String>();

    
    public List<QueryVariable> getVariables(){
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

    //think about a way of making this abstract
    public void setPath(String path) {
        this.path = path;
    }
    
    //return raw path (as is)
    public String getPath(){
        return this.path;
    }
    
    //return path according to a specific variable template
    public String getPath(String variableTemplate) {
        return this.path;
    }

    public List<String> getContentTypeList() {
        List<String> contentTypeList = new ArrayList<String>();
        // TODO: use CSV as default response type, should we use JSON?
        contentTypeList.add("text/csv");
        contentTypeList.add("application/sparql-results+json");
        contentTypeList.add("application/json");
        contentTypeList.add("text/xml");
        contentTypeList.add("text/tab-separated-values");
        contentTypeList.add("text/html");
        contentTypeList.add("application/rdf+xml");
        
        return contentTypeList;
    }

    public void addTag(String tag) {
        this.tagList.add(tag);
    }

    public List<String> getTagList() {
        return this.tagList;
    }
}
