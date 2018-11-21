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
package nl.unimaas.ids.data2services.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

import nl.unimaas.ids.data2services.model.IRIEntity;
import nl.unimaas.ids.data2services.registry.AbstractPathHandler;
import nl.unimaas.ids.data2services.registry.RegistryPathHandler;
import nl.unimaas.ids.data2services.registry.TestPathHandler;
import nl.unimaas.ids.data2services.service.ReadEntitiesFromEndPoint;
import nl.unimaas.ids.data2services.util.SwaggerTest;

@Path("/")
public class BasicOperationsController {
    private RegistryPathHandler registryPathHandler;
    private SwaggerTest swaggerTest;
    private ReadEntitiesFromEndPoint readEntities =  new ReadEntitiesFromEndPoint();

    public BasicOperationsController(){
        swaggerTest = SwaggerTest.getInstance();
        
        registryPathHandler = new RegistryPathHandler();
        registryPathHandler.registerHandler(new TestPathHandler());
    }
    
    @GET
    @Path("/swag")
    public String swag(@Context HttpServletRequest request) {
        return swaggerTest.getSwaggerJson();
    }

    // TODO: finish
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/metadata/sources/")
    public List<IRIEntity> classList(@PathParam("id") String id, @PathParam("id2") String id2, @Context HttpServletRequest request) {
        List<IRIEntity> list = new ArrayList<IRIEntity>();

        IRIEntity iriEntity = new IRIEntity();
        iriEntity.setIRI("https://www.drugbank.ca/");
        iriEntity.setLabel("drugbank");
        list.add(iriEntity);

        return list;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{source}/{class}")
    public String getClassesForSource(@PathParam("source") String source, @PathParam("class") String sClass, @Context HttpServletRequest request) {
        String classList = readEntities.getClassList();

        return classList;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{source}/")
    public String classListBySource(@PathParam("source") String source, @Context HttpServletRequest request) {
        String txt = this.readEntities.getClassList();

        return txt;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/subjectList/{classId}")
    public String subjectList(@PathParam("classId") String classId, @Context HttpServletRequest request) {

        //String url = request.getRequestURL().toString();
        //URI uri = new URIImpl(url);
        //readEntities.getSubject(uri);
        try {
            classId = new String(Base64.getDecoder().decode(classId), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(classId);

        String txt = "";
        try {
            txt = readEntities.getSubjectListByClass(SimpleValueFactory.getInstance().createIRI(classId));
        } catch (Exception ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return txt;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{source}/{rdftype}/{blid}")
    public String subject(@PathParam("id") String id, @PathParam("id2") String id2, @Context HttpServletRequest request) throws Exception {
        String url = request.getRequestURL().toString();

        IRI uri = SimpleValueFactory.getInstance().createIRI(url);

        readEntities.getSubject(uri);

        try {
            id = new String(Base64.getDecoder().decode(id), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        String s = "";
        try {
            s = readEntities.getSubject(SimpleValueFactory.getInstance().createIRI(id));
        } catch (Exception ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return s;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/datasources")
    public String datasources() {
        return readEntities.getSources();
    }

    //generalize
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pathwayListBySpecies/{species}")
    public String genericMethod(@PathParam("species") String species, @Context HttpServletRequest request) {
        
        String s = "";
        //String s = this.queries.executeQuery(species);

        return s;
    }

    //allow method to be extended
    @GET
    @Path("{path:.*}")
    public String genericPathHandler(@PathParam("path") List<PathSegment> segments) throws Exception {
        
        String pathDomain = "";
        String path = ""; 

        for (int i = 0; i < segments.size(); i++) {
            if(i!=0)
                path += segments.get(i).getPath();
            else
                pathDomain = "/" + segments.get(i).getPath();
        }

        AbstractPathHandler pathHandler = registryPathHandler.getHandler(pathDomain, path);
        
        String result = pathHandler.process(path);
        
        return result;
    }

}
