/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.service;

import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
//import nl.unimaas.ids.rdf2api.controller.EntityParser;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.rio.RDFFormat;
import java.lang.String;
import java.nio.charset.StandardCharsets;
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.unimaas.ids.data2services.model.IRIEntity;
import nl.unimaas.ids.data2services.service.ConnectionManager.ConnectionManager;
import nl.unimaas.ids.rdf2api.io.utils.RDFUtils;
//import nl.unimaas.ids.rdf2api.io.utils.RDFUtils;
import org.eclipse.rdf4j.model.impl.SimpleIRI;
import org.eclipse.rdf4j.model.impl.URIImpl;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.URI;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.eclipse.rdf4j.repository.sail.ProxyRepository;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;


/**
 *
 * @author nuno
 */
public class ReadEntitiesFromFile extends Entity /*implements ReadEntities*/{

    

    public ReadEntitiesFromFile() {
    }
	
    public static void main(String[] args){
        System.out.println("Starting...");
        ReadEntitiesFromFile entities = new ReadEntitiesFromFile();
        //entities.getClassList();
        //entities.getSubjectListByClass(new URIImpl("http://vocabularies.wikipathways.org/wp#Pathway"));
        entities.getClassList();
        //entities.getSubject(new URIImpl("http://identifiers.org/wikipathways/WP1000_r80835"));
    }
    
    
    
     /**
     * Parse RDF string to create catalog metadata object
     *
     * @param catalogMetadata Catalog metadata as a RDF string
     * @param catalogURI Catalog URI
     * @param fdpURI FairDataPoint URI
     * @param format RDF string's RDF format
     * @return CatalogMetadata object
     * @throws MetadataParserException
     */
//   public ReadFileEntities parse(@Nonnull String catalogMetadata,
//           @Nonnull IRI catalogURI, IRI fdpURI, @Nonnull RDFFormat format)
//           throws MetadataParserException {
//       Preconditions.checkNotNull(catalogMetadata,
//               "Catalog metadata string must not be null.");
//       Preconditions.checkNotNull(catalogURI, "Catalog URI must not be null.");
//       Preconditions.checkNotNull(format, "RDF format must not be null.");

//        Preconditions.checkArgument(!catalogMetadata.isEmpty(),
//                "The catalog metadata content can't be EMPTY");
//        List<Statement> statements = RDFUtils.getStatements(catalogMetadata,
//                catalogURI, format);

//            CatalogMetadata metadata = this.parse(statements, catalogURI);
//            metadata.setParentURI(fdpURI);

       
//    }
    
    private List<String> parse(List<Statement> statements, IRI metadataUri){
        //LOGGER.info("Parse common metadata properties");
        List<String> l = new ArrayList();
                
        ValueFactory f = SimpleValueFactory.getInstance();
        for (Statement st : statements) {
            Resource subject = st.getSubject();
            IRI predicate = st.getPredicate();
            Value object = st.getObject();

                //if (subject.equals(metadataUri)) {
                    if (predicate.equals(RDF.TYPE)) {
                        l.add(object.stringValue());
                        //System.out.println(" "+ object.stringValue());
                    } 
                //}
        }
        
        return l;
    }
    
    //@Override
    public void setSource(URL url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getEntities(){
        
        List<Statement> statements;
        List<String> l = null;
        
        try {
            statements = RDFUtils.getStatements( this.myvar , new URIImpl("http://fdp.wikipathways.org/fdp"),  RDFFormat.TURTLE);
            l = this.parse(statements, new URIImpl("http://graphdb.dumontierlab.com/repositories/ncats-red-kg"));
            
        } catch (Exception ex) {
            Logger.getLogger(ReadEntitiesFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     return l;
    }
    

    
    
    public  List<IRIEntity> getClassList(){
            
            List<IRIEntity> classList = new ArrayList();
            
            RepositoryConnection conn = getConnection();
             
            
            //String queryString = "select ?Concept where {[] a "+entity.stringValue()+"}";
            String queryString = "select distinct ?Concept where {[] a ?Concept}";
            
            TupleQuery query = conn.prepareTupleQuery(queryString);
            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    System.out.println("?s = " + solution.getValue("Concept"));
                    //System.out.println("?n = " + solution.getValue("n"));
                    IRIEntity iriEntity = new IRIEntity();
                    
                    iriEntity.setIRI(solution.getValue("Concept").stringValue());
                    iriEntity.setLabel(this.generateLabel(solution.getValue("Concept").stringValue()));
                    
                    classList.add(iriEntity);
                }
              
            } catch (Exception e){
                System.out.println(e);
            } finally {
            
            }
            
            return classList;
    }
    
    public String getSubject(URI subjectURI){
            RepositoryConnection conn = getConnection();
               
            //Repository repository = repositoryManager.getRepository("RepositoryID"); 
            // Open a connection to this repository
            // Open a connection to the database
            
            String queryString = "SELECT ?p ?o \n" +
                "WHERE {\n" +
                "  <"+subjectURI.stringValue()+"> ?p ?o.\n" +
                "}";
            
            
            
            TupleQuery query = conn.prepareTupleQuery(queryString);
            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            //List<IRIEntity> l = new ArrayList();
            
            String s = "";
            
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    //System.out.println("?s = " + solution.getValue("s") + " " + solution.getValue("o"));
                    //System.out.println("?n = " + solution.getValue("n"));
                    //IRIEntity iriEntity = new IRIEntity();
                    //iriEntity.setLabel(solution.getValue("o")+"");
                    s += "<"+subjectURI.stringValue() + "> <" + solution.getValue("p").toString() + "> <" + solution.getValue("o").toString() + ">\n"; 
                    System.out.println(s);
                }
            } catch (Exception e){
                System.out.println(e);
            } finally {
           
            }


        return s;
    }
    
    
    public List<IRIEntity> getSubjectListByClass(URI classURI) {
            RepositoryConnection conn = getConnection();
            List<IRIEntity> l = new ArrayList();
            
            String queryString = "SELECT ?s ?p ?o \n" +
                "WHERE {\n" +
                "  ?s rdf:type <"+classURI.stringValue()+">.\n" +
                //"  ?p ?o.\n" +
                "} ORDER BY ?s";
            
            System.out.println(queryString);
            
            TupleQuery query = conn.prepareTupleQuery(queryString);
            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    //System.out.println("?s = " + solution.getValue("s"));
                    //System.out.println("?n = " + solution.getValue("n"));
                    
                    IRIEntity iriEntity = new IRIEntity();
                    iriEntity.setLabel(this.generateLabel(solution.getValue("s").stringValue()));
                    iriEntity.setIRI(solution.getValue("s").stringValue());
                    l.add(iriEntity);
                }
            } catch (Exception e){
                System.out.println(e);
            } finally {
            
            }
            
            return l;
    }  
    
    
    private String myvar; //legacy to be deleted
    public void getEntityTesting(URI uri) {
        Repository db = new SailRepository(new MemoryStore());
        db.initialize();

        // Open a connection to the database
        try (RepositoryConnection conn = db.getConnection()) {
            String filename = "data.ttl";
            try {
                InputStream input = new ByteArrayInputStream(this.myvar.getBytes(StandardCharsets.UTF_8));
                // .class.getResourceAsStream("/" + filename)) {
                // add the RDF data from the inputstream directly to our database
                conn.add(input, "", RDFFormat.TURTLE);

            } catch (IOException ex) {
                Logger.getLogger(ReadEntitiesFromFile.class.getName()).log(Level.SEVERE, null, ex);
            }

            // We do a simple SPARQL SELECT-query that retrieves all resources of
            // type `ex:Artist`, and their first names.
            String queryString = "PREFIX ex: <http://example.org/> \n";
            queryString += "PREFIX foaf: <" + FOAF.NAMESPACE + "> \n";
            queryString += "SELECT ?s ?n \n";
            queryString += "WHERE { \n";
            queryString += "    ?s a ex:Artist; \n";
            queryString += "       foaf:firstName ?n .";
            queryString += "}";

            TupleQuery query = conn.prepareTupleQuery(queryString);
            // A QueryResult is also an AutoCloseable resource, so make sure it gets
            // closed when done.
            try (TupleQueryResult result = query.evaluate()) {
                // we just iterate over all solutions in the result...
                while (result.hasNext()) {
                    BindingSet solution = result.next();
                    // ... and print out the value of the variable bindings
                    // for ?s and ?n
                    System.out.println("?s = " + solution.getValue("s"));
                    System.out.println("?n = " + solution.getValue("n"));
                }
            }
        } finally {
            // Before our program exits, make sure the database is properly shut down.
            db.shutDown();
        }
    }
    
    private String generateLabel(String str){
        return str.replaceAll("[^a-zA-Z0-9]", "");
    }
    
}
