/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.util;

import com.google.common.base.Preconditions;
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
import static java.util.Collections.list;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.unimaas.ids.rdf2api.io.utils.RDFUtils;
//import nl.unimaas.ids.rdf2api.io.utils.RDFUtils;
import org.eclipse.rdf4j.model.impl.SimpleIRI;
import org.eclipse.rdf4j.model.impl.URIImpl;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import nl.unimaas.ids.data2services.util.iface.ReadEntities;

/**
 *
 * @author nuno
 */
public class ReadEntitiesFromFile implements ReadEntities{

    
    
 public String myvar = "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ."+
"@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> ."+
"@prefix dcat: <http://www.w3.org/ns/dcat#> ."+
"@prefix xsd: <http://www.w3.org/2001/XMLSchema#> ."+
"@prefix owl: <http://www.w3.org/2002/07/owl#> ."+
"@prefix dcterms: <http://purl.org/dc/terms/> ."+
"@prefix fdp: <http://rdf.biosemantics.org/ontologies/fdp-o#> ."+
"@prefix r3d: <http://www.re3data.org/schema/3-0#> ."+
"@prefix lang: <http://id.loc.gov/vocabulary/iso639-1/> ."+
""+
"<http://fdp.wikipathways.org/fdp> dcterms:title \"WikiPathways\" ;"+
"	rdfs:label \"WikiPathways\" ;"+
"	dcterms:hasVersion \"1.0\" ;"+
"	fdp:metadataIssued \"2018-06-18T08:06:07.395Z\"^^xsd:dateTime ;"+
"	fdp:metadataIdentifier <http://fdp.wikipathways.org/fdp#metadataID> ."+
""+
"<http://fdp.wikipathways.org/fdp#metadataID> a <http://purl.org/spar/datacite/ResourceIdentifier> ;"+
"	dcterms:identifier \"34f59c2f-6e26-4bc2-88a4-8f87e66b501d\" ."+
""+
"<http://fdp.wikipathways.org/fdp> fdp:metadataModified \"2018-06-19T12:41:15.246Z\"^^xsd:dateTime ;"+
"	dcterms:language lang:en ;"+
"	dcterms:publisher <http://www.wikipathways.org/> ."+
""+
"<http://www.wikipathways.org/> a <http://xmlns.com/foaf/0.1/Agent> ."+
""+
"<http://fdp.wikipathways.org/fdp> dcterms:description \"FDP of fdp.wikipathways.org\" ;"+
"	dcterms:license <http://rdflicense.appspot.com/rdflicense/cc-by-nc-nd3.0> ;"+
"	dcterms:conformsTo <http://rdf.biosemantics.org/fdp/shex/fdpMetadata> ;"+
"	a r3d:Repository ;"+
"	rdfs:seeAlso <http://fdp.wikipathways.org/fdp/swagger-ui.html> ;"+
"	r3d:repositoryIdentifier <http://fdp.wikipathways.org/fdp#repositoryID> ."+
""+
"<http://fdp.wikipathways.org/fdp#repositoryID> a <http://purl.org/spar/datacite/Identifier> ;"+
"	dcterms:identifier \"0820887e-5ba7-460e-8572-cd453d98c0b1\" ."+
""+
"<http://fdp.wikipathways.org/fdp> r3d:institutionCountry <http://lexvo.org/id/iso3166/NL> ;"+
"	r3d:dataCatalog <http://fdp.wikipathways.org/fdp/catalog/catalog> .";

    public ReadEntitiesFromFile() {
    }
	
    public static void main(String[] args){
        System.out.println("Starting...");
        

        
       
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
                        System.out.println(" "+ object.stringValue());
                    } 
                //}
        }
        
        return l;
    }
    
    @Override
    public void setSource(URL url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getEntities(){
        
        List<Statement> statements;
        List<String> l = null;
        
        try {
            statements = RDFUtils.getStatements( this.myvar , new URIImpl("http://fdp.wikipathways.org/fdp"),  RDFFormat.TURTLE);
            l = this.parse(statements, new URIImpl("http://fdp.wikipathways.org/fdp"));
            
        } catch (Exception ex) {
            Logger.getLogger(ReadEntitiesFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     return l;
    }
    
}
