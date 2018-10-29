/*
 * The MIT License
 *
 * Copyright 2018 nuno.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.unimaas.ids.data2services.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nl.unimaas.ids.data2services.model.NamedQueryEntity;
import nl.unimaas.ids.data2services.model.QueryVariable;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

/**
 *
 * @author nuno
 */
public class ReadQueriesFromFile  extends Entity{
    
    String queryString  = "PREFIX rdfs:    <http://www.w3.org/2000/01/rdf-schema#> "
            + "PREFIX dc:      <http://purl.org/dc/elements/1.1/> "
            + "PREFIX foaf:    <http://xmlns.com/foaf/0.1/> "
            + "PREFIX wp: <http://vocabularies.wikipathways.org/wp#> "
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
            + "SELECT DISTINCT ?wpIdentifier ?pathway ?page "
            + "WHERE { "
            + "?pathway dc:title ?title ."
            + "?pathway foaf:page ?page ."
            + "?pathway dc:identifier ?wpIdentifier ."
            + "?pathway wp:organismName \"{{species}}\"^^xsd:string "
            + "} " 
            + "ORDER BY ?wpIdentifier";
    
    String pattern = "\\{\\{(.*?)\\}\\}";
    
    List<NamedQueryEntity> nqeList;
    
    public static void main(String[] argv){
        System.out.println("starting");
        ReadQueriesFromFile r = new ReadQueriesFromFile();
    }


    public ReadQueriesFromFile(){
        
            nqeList = new ArrayList();
            NamedQueryEntity namedQuery  = new NamedQueryEntity();
            namedQuery.setLabel("ExampleQuery - Pathways with organism ..."); //set dynamic
            
            Pattern pattern = Pattern.compile(this.pattern);
            Matcher matcher = pattern.matcher(queryString);
            
            
            
            QueryVariable qv = null;
            while (matcher.find())
            {
                String variableName = matcher.group(1);
                System.out.println(matcher.group(1));
                
                qv = new QueryVariable();
                qv.setLabel(variableName);
                
                namedQuery.addVariable(qv);
            }
            
            nqeList.add(namedQuery);
            
    }

    public List<NamedQueryEntity> getNamedQueryList(){
        return this.nqeList;
    }
    
    public String executeQuery(String var){
             RepositoryConnection conn = getConnection();
               
            //Repository repository = repositoryManager.getRepository("RepositoryID"); 
            // Open a connection to this repository
            // Open a connection to the database
            
            String lqueryString = this.queryString;
            
            lqueryString = lqueryString.replaceAll("\\{\\{species\\}\\}",  var);
                        
            TupleQuery query = conn.prepareTupleQuery(lqueryString);
            
            String s = "";
            
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    System.out.println("?s = " + solution.getValue("s") + " " + solution.getValue("o"));
                    //System.out.println("?n = " + solution.getValue("n"));
                    //IRIEntity iriEntity = new IRIEntity();
                    //iriEntity.setLabel(solution.getValue("o")+"");
                    s += "<"+solution.getValue("wpIdentifier") + "> <" + solution.getValue("pathway").toString() + "> <" + solution.getValue("page").toString() + ">\n"; 
                    System.out.println(s);
                }
            } catch (Exception e){
                System.out.println(e);
            } finally {
           
            }


        return s;
    }
    
}
