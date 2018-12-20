/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import nl.unimaas.ids.rdf2api.io.utils.HttpURLConnect;

/**
 *
 * @author nuno
 */
public class ReadEntitiesFromEndPoint {
	private static final Logger logger = Logger.getLogger(ReadEntitiesFromEndPoint.class.getName());

	private HttpURLConnect httpConnect;
	private String endpointURL;

	public static void main(String[] args) {
		System.out.println("Starting...");
		ReadEntitiesFromEndPoint entities = new ReadEntitiesFromEndPoint();

		//String e = entities.getClassList();
		//System.out.println(e);
		// entities.getSubject(new
		// URIImpl("http://identifiers.org/wikipathways/WP1000_r80835"));
	}

	public ReadEntitiesFromEndPoint() {
		this.httpConnect = new HttpURLConnect();
		this.endpointURL = "http://graphdb.dumontierlab.com/repositories/ncats-red-kg";
	}

	// @Override
	public void setSource(URL url) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}


	public String getSources() {
		String queryString = "select distinct ?g where {graph ?g {?s ?p ?o}}";
		return post(queryString);
	}

	public String getSubject(IRI subjectURI) throws Exception {

		// Repository repository = repositoryManager.getRepository("RepositoryID");
		// Open a connection to this repository
		// Open a connection to the database

		String queryString = "SELECT ?p ?o \n" + "WHERE {\n" + "  <" + subjectURI.stringValue() + "> ?p ?o.\n" + "}";

		String result = post(queryString);
		return result;
	}

	public String getSubjectListByClass(IRI classURI) throws Exception {

		String queryString = "SELECT ?s ?p ?o \n" + "WHERE {\n" + "  ?s rdf:type <" + classURI.stringValue() + ">.\n" +
		// " ?p ?o.\n" +
				"} ORDER BY ?s";

		System.out.println(queryString);
		String result = post(queryString);

		return result;
	}

	

	public void getEntityTesting(IRI uri) {
            
                String myvar = ""; 
            
		Repository db = new SailRepository(new MemoryStore());
		db.initialize();

		// Open a connection to the database
		try (RepositoryConnection conn = db.getConnection()) {
			try {
				InputStream input = new ByteArrayInputStream(myvar.getBytes(StandardCharsets.UTF_8));
				// .class.getResourceAsStream("/" + filename)) {
				// add the RDF data from the inputstream directly to our database
				conn.add(input, "", RDFFormat.TURTLE);

			} catch (IOException ex) {
				logger.log(Level.SEVERE, null, ex);
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

	private String post(String data) {
		try {
			String r = this.httpConnect.sendPost(this.endpointURL, data);
			return r;
		} catch (Exception ex) {
			Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
			return "error"; // replace with custom error
		}
	}

    // https://github.com/vemonet/insert-data2services/blob/master/services-queries/get_datasets.rq
    public String metadataSources() {
        String queryString = "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n" +
                                "PREFIX dcat: <http://www.w3.org/ns/dcat#>\n" +
                                "PREFIX dctypes: <http://purl.org/dc/dcmitype/>\n" +
                                "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                                "PREFIX idot: <http://identifiers.org/idot/>\n" +
                                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                "PREFIX void: <http://rdfs.org/ns/void/>\n" +
                                "\n" +
                                "# Add ?classCount\n" +
                                "SELECT ?source ?graph \n" +
                                "WHERE {\n" +
                                "    GRAPH <http://data2services/metadata/datasets>\n" +
                                "    {\n" +
                                "        ?dataset a dctypes:Dataset ;\n" +
                                "            idot:preferredPrefix ?source ;\n" +
                                "            dcat:accessURL ?graph .\n" +
                                "        ?version dct:isVersionOf ?dataset ; \n" +
                                "            dcat:distribution [ a void:Dataset ] .  \n" +
                                "    }\n" +
                                "}";

	String result = post(queryString);
	return result;
    }
    
    // https://github.com/vemonet/insert-data2services/blob/master/services-queries/classes_by_datasets.rq
    public String source(String source) {
            try {
                //String queryString = "select distinct ?Concept where {[] a ?Concept}";
                source = URLDecoder.decode(source, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                                "\n" +
                                "SELECT ?class ?classLabel ?classCount\n" +
                                "WHERE\n" +
                                "{\n" +
                                "    {\n" +
                                "        select ?g ?class (count(?class) as ?classCount)  \n" +
                                "        where {\n" +
                                "            graph ?g {\n" +
                                "                [] a ?class .\n" +
                                "            }\n" +
                                "            # Should be a variable (source)\n" +
                                //<http://data2services/biolink/drugbank>
                                //%3Chttp%3A//data2services/biolink/drugbank%3E
                                "            FILTER(?g = "+ source +")\n" +
                                "        }\n" +
                                "        group by ?g ?class\n" +
                                "        order by desc(?classCount)\n" +
                                "    }\n" +
                                "    \n" +
                                "    optional {\n" +
                                "        ?class rdfs:label ?classLabel .\n" +
                                "    }\n" +
                                "}";

        String result = "";
        try {
                result = post(queryString);
        } catch (Exception ex) {
                Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(result);

        return result;
    }
    
    public String getSourceClass(String source, String type) {
        
            try {
                source = URLDecoder.decode(source, "UTF-8");
                type = URLDecoder.decode(type, "UTF-8");

            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
   
       String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                        "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                        "PREFIX bl: <http://bioentity.io/vocab/>\n" +
                        "\n" +
                        "SELECT ?item\n" +
                        "WHERE \n" +
                        "{\n" +
                        "    # Should be a variable (source)\n" +
                      //"    GRAPH <http://data2services/biolink/drugbank> \n" +
                      //     %3Chttp%3A//data2services/biolink/drugbank%3E
                        "    GRAPH "+source+ " \n" +
                        "    {\n" +
                        "        # Should be a variable (type)\n" +
                      //"        ?item a bl:Drug .\n" +
                      //         bl%3ADrug
                        "        ?item a "+type+" .\n" +
                       "    }";
       
       
       String result = "";
       result = post(queryString);
       return result;
    }

    // https://github.com/vemonet/insert-data2services/blob/master/services-queries/class_get_item_by_id.rq
    public String sourceClassId(String source, String sClass, String id) {
            try {
                source = URLDecoder.decode(source, "UTF-8");
                sClass = URLDecoder.decode(sClass, "UTF-8");
                id = URLDecoder.decode(id, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(ReadEntitiesFromEndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
       
            String queryString = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                                    "PREFIX dct: <http://purl.org/dc/terms/>\n" +
                                    "PREFIX bl: <http://bioentity.io/vocab/>\n" +
                                    "\n" +
                                    "SELECT ?predicate ?object\n" +
                                    "WHERE \n" +
                                    "{\n" +
                                    "    # Should be a variable (source)\n" +
                                    //"    GRAPH <http://data2services/biolink/drugbank> \n" +
                                    "    GRAPH "+source+" \n" +
                                    "    {\n" +
                                    "        # Should be a variable (type)\n" +
                                    //"        ?item a bl:Drug .\n" +
                                    "        ?item "+ sClass +" .\n" +
                                    "        # Should be a variable (id)\n" +
                                    //"        ?item bl:id \"DB11571\" .\n" +
                                    "        ?item bl:id \""+id+"\" .\n" +
                                    "        \n" +
                                    "        ?item ?predicate ?object .\n" +
                                    "    }\n" +
                                    "}";
            
            String result = post(queryString);
            return result;
            
    }
    
    String execute(String query){
        return post(query);
    }
}
