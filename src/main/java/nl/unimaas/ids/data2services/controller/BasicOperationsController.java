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
import nl.unimaas.ids.data2services.model.IRIEntity;
import nl.unimaas.ids.data2services.service.ReadEntitiesFromFile;
import nl.unimaas.ids.data2services.service.ReadQueriesFromFile;
import nl.unimaas.ids.data2services.util.SwaggerTest;
import nl.unimaas.ids.data2services.util.SwaggerTest1;
import org.eclipse.rdf4j.model.URI;
import org.eclipse.rdf4j.model.impl.URIImpl;
import static org.eclipse.rdf4j.model.vocabulary.DCTERMS.URI;

/**
 *
 * @author nuno
 */
@Path("/{fullPath}")
public class BasicOperationsController
{
    
    private ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();
    private ReadQueriesFromFile queries = new ReadQueriesFromFile();
    
    @GET
    @Path("/swag")
    public String swag(@Context HttpServletRequest request) {
        SwaggerTest swaggerTest = new SwaggerTest();
        return swaggerTest.getSwaggerJson();
    }
    
    @GET
    @Path("/swag1")
    public String swag1(@Context HttpServletRequest request) {
        SwaggerTest1 swaggerTest1 = new SwaggerTest1();
        return swaggerTest1.getSwaggerJson();
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/class")
    public List<IRIEntity> classList(@PathParam("id") String id, @PathParam("id2") String id2, @Context HttpServletRequest request) {
        List<IRIEntity>  iriEntityList = this.readEntities.getClassList();
                        
        return iriEntityList;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/subjectList/{classId}")
    public List<IRIEntity> subjectList(@PathParam("classId") String classId, @Context HttpServletRequest request) {
        
        //String url = request.getRequestURL().toString();
        //URI uri = new URIImpl(url);
        //readEntities.getSubject(uri);
        
        try {
            classId = new String( Base64.getDecoder().decode(classId), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(classId);
        
        List<IRIEntity> list = readEntities.getSubjectListByClass(new URIImpl(classId));
                
        return list;
    }
    
        @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/subject/{id}")
    public String subject(@PathParam("id") String id, @PathParam("id2") String id2, @Context HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        
        URI uri = new URIImpl(url);
        
        readEntities.getSubject(uri);
        
        try {
            id = new String( Base64.getDecoder().decode(id), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String s  = readEntities.getSubject(new URIImpl(id));
                
        return s;
    }
    
    
    //generalize
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/pathwayListBySpecies/{species}")
    public String genericMethod(@PathParam("species") String species, @Context HttpServletRequest request) {
        
        String s = this.queries.executeQuery(species);
        
        return s;
    }
}

