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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.PathSegment;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.registry.AbstractPathHandler;

import nl.unimaas.ids.data2services.registry.RegistryPathHandler;
import nl.unimaas.ids.data2services.registry.TestPathHandler;
import nl.unimaas.ids.data2services.util.SwaggerManager;

@Path("/")
public class BasicOperationsController {
    private RegistryPathHandler registryPathHandler;
    private SwaggerManager swaggerTest;
    //private ReadEntitiesFromEndPoint readEntities =  new ReadEntitiesFromEndPoint();

    public BasicOperationsController(){
                        
        swaggerTest = SwaggerManager.getInstance();
        
        registryPathHandler = new RegistryPathHandler();
        registryPathHandler.registerHandler(new TestPathHandler());
       //registryPathHandler.registerHandler(new MetaDataSourcesPathHandler());
    }
    
    @GET
    @Path("/swag")
    public String swag(@Context HttpServletRequest request) {
        return swaggerTest.getSwaggerJson();
    }
    
    @GET
    @Path("/")   
    public String homeRedirect(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        
        response.setHeader("refresh","0;https://www.google.com/");
        //return swaggerTest.getSwaggerJson();
        
        return "";
    }

 //   @GET
 //   @Produces(MediaType.APPLICATION_JSON)
 //   @Path("/metadata/sources/")
 //   public String getMetadataSources(@PathParam("id") String id, @PathParam("id2") String id2, @Context HttpServletRequest request) {
 //       
 //       return readEntities.metadataSources();
 //  }
    
    //commented to test dynamic libraries
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{source}/")
//    public String getSource(@PathParam("source") String source, @Context HttpServletRequest request) {
//        String txt = this.readEntities.source(source);
//
//        return txt;
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{source}/{class}")
//    public String getClassesForSource(@PathParam("source") String source, @PathParam("class") String sClass, @Context HttpServletRequest request) {
//        String classList = readEntities.getSourceClass(source, sClass);
//
//        return classList;
//    }
//
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/subjectList/{classId}")
//    public String subjectList(@PathParam("classId") String classId, @Context HttpServletRequest request) {
//
//        try {
//            classId = new String(Base64.getDecoder().decode(classId), "UTF-8");
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        System.out.println(classId);
//
//        String txt = "";
//        try {
//            txt = readEntities.getSubjectListByClass(SimpleValueFactory.getInstance().createIRI(classId));
//        } catch (Exception ex) {
//            Logger.getLogger(BasicOperationsController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return txt;
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/{source}/{class}/{id}")
//    public String subject(@PathParam("source") String source, @PathParam("class") String sClass, @PathParam("id") String id, @Context HttpServletRequest request) throws Exception {
//        //String url = request.getRequestURL().toString();
//
//        String result = readEntities.sourceClassId(source, sClass, id);
//
//        return result;
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/datasources")
//    public String datasources() {
//        return readEntities.getSources();
//    }
//
//    //generalize
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path("/pathwayListBySpecies/{species}")
//    public String genericMethod(@PathParam("species") String species, @Context HttpServletRequest request) {
//        
//        String s = "";
//        //String s = this.queries.executeQuery(species);
//
//        return s;
//    }

    //allow method to be extended
    @GET
    @Path("{path:.*}")
    public String genericPathHandler(@PathParam("path") List<PathSegment> segments, @Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        
        
        if(segments.size()==1){

           response.setHeader("refresh","0; ../webjars/swagger-ui/3.19.0/?url=/data2services/rest/swag");

            //return swaggerTest.getSwaggerJson();
            return "";
        }
        
        String serviceRealm = "";
        String path = ""; 

        for (int i = 0; i < segments.size(); i++) {
            if(i!=0)
                path += "/"+segments.get(i).getPath();
            else
                serviceRealm = segments.get(i).getPath();
        }

        String acceptHeader = request.getHeader("Accept");
        //del*
        //System.out.println("AAAAAAAA "+acceptHeader);
        
        AbstractPathHandler pathHandler = registryPathHandler.getHandler(new ServiceRealm(serviceRealm), path);
        
        String result = pathHandler.process(path, acceptHeader);
        
        return result;
    }

}
