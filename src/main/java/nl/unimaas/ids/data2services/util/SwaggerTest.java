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
package nl.unimaas.ids.data2services.util;

import nl.unimaas.ids.data2services.service.ReadEntitiesFromFile;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.util.Json;
import java.util.List;
import nl.unimaas.ids.data2services.model.IRIEntity;
import nl.unimaas.ids.data2services.model.NamedQueryEntity;
import nl.unimaas.ids.data2services.model.QueryVariable;
//import nl.unimaas.ids.data2services.service.ReadEntities;
import nl.unimaas.ids.data2services.service.ReadQueriesFromFile;
import nl.unimaas.ids.rdf2api.io.utils.Config;

/**
 *
 * @author nuno
 */
public class SwaggerTest {
    
    private static SwaggerTest singleton = new SwaggerTest();
    
    private Swagger swagger;
     
    public static void main(String[] args) {
        new SwaggerTest().doSwagger();
    }

    private SwaggerTest() {
        doSwagger();
    }

    public Swagger doSwagger() { 
        
        swagger = new Swagger();
        
        Config config = new Config();
        
        //test what happens without a property
        String title = config.getProperty("title").orElse("");
        String description = config.getProperty("description").orElse("");
        String termsOfService = config.getProperty("termsOfService").orElse("");
        String contactName = config.getProperty("contactName").orElse("");
        String contactEmail = config.getProperty("contactEmail").orElse("");
        String licenseName = config.getProperty("licenseName").orElse("");
        String licenseURL = config.getProperty("licenseURL").orElse("");
        
        String host = config.getProperty("host").orElse("127.0.0.1:8080");
        String basePath = config.getProperty("basePath").orElse("/");

        
        swagger.info(new Info()
                .title(title)
                .description(description)
                .termsOfService(termsOfService)
                .contact(new Contact().name(contactName).email(contactEmail))
                .license(new License().name(licenseName)
                        .url(licenseURL)
                )
        ).host(host)
                .basePath(basePath)
                //.consumes("application/json").consumes("application/xml")
                .produces("application/json"); //.produces("application/xml");
        
//.securityDefinition("api_key", apiKeyAuth("api_key", In.HEADER))
        //.securityDefinition("petstore_auth", oAuth2()
        //        .implicit("http://petstore.swagger.io/api/oauth/dialog")
        //        .scope("read:pets", "read your pets")
        //        .scope("write:pets", "modify pets in your account")
        //).tag(tag("query").description("Everything about your queries")
        //.externalDocs(externalDocs().description("Find out more").url("http://swagger.io"))
        //).tag(tag("store").description("Access to Petstore orders")
        //).tag(tag("user").description("Operations about user")
        //        .externalDocs(externalDocs().description("Find out more about our store").url("http://swagger.io"))
        //);

        generateOperations();
        generateQueryOperations();
        
        return swagger;
    }
    
    private void operationMetadataSources(){
        String sPath = "/metadata/sources/";
        String query = "";
        String operationDescription = "test";
        
        ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();

        Operation operation = new Operation();
        operation.description(operationDescription);

        Path path = new Path();
        path.setGet(operation);

        swagger.path(sPath, path);
    }
    
private void operationSource(){
        String sPath = "/{source}/";
        String query = "";
        String operationDescription = "test";
        
        ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();

        PathParameter parameter1 = new PathParameter();
        parameter1.setName("source");
        parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?

        parameter1.setEnum(readEntities.getEntities());
        parameter1.setType("string"); //TODO should it be URL (check types)

        parameter1.setDescription("parameter description");

        parameter1.setIn("path");
       
        Operation operation = new Operation();
        operation.description(operationDescription);
        operation.addParameter(parameter1);

        Path path = new Path();
        path.setGet(operation);

        swagger.path(sPath, path);
    }
    
    private void operationClass(){
            ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();

            List<IRIEntity> entityList;
            entityList = readEntities.getClassList();
            
            
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("source");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter1.setEnum(readEntities.getEntities());
            parameter1.setType("string"); //TODO should it be URL (check types)
            
            parameter1.setDescription("parameter description");
            
            parameter1.setIn("path");
            
            PathParameter parameter2 = new PathParameter();
            parameter2.setName("class");
            parameter2.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter2.setEnum(readEntities.getEntities());
            parameter2.setType("string"); //TODO should it be URL (check types)
            
            parameter2.setDescription("parameter description");
            
            parameter2.setIn("path");

            Operation operation = new Operation();
            operation.addParameter(parameter1);
            operation.addParameter(parameter2);
            operation.description("operation description");
          
            Path path = new Path();
            path.setGet(operation);
            
            swagger.path("/{source}/{rdftype}", path);
    }
    
    private void operationSubjectList(){
            ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();

            List<IRIEntity> entityList = readEntities.getClassList();
        
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("subjectClass");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter1.setEnum(readEntities.getEntities());
            parameter1.setType("string"); //TODO should it be URL (check types)
            
            parameter1.setDescription("parameter description");
            
            parameter1.setIn("path");

            Operation operation = new Operation();
            operation.addParameter(parameter1);
            operation.description("operation description");
            
            Path path = new Path();
            path.setGet(operation);
            
            swagger.path("/subjectList/{subjectClass}/", path);
    }
    
    private void operationSubject(){
            ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();

            List<IRIEntity> entityList = readEntities.getClassList();
        
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("id");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter1.setEnum(readEntities.getEntities());
            parameter1.setType("string"); //TODO should it be URL (check types)
            
            parameter1.setDescription("parameter description");
            
            parameter1.setIn("path");
            
            PathParameter parameter2 = new PathParameter();
            parameter2.setName("source");
            parameter2.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter2.setEnum(readEntities.getEntities());
            parameter2.setType("string"); //TODO should it be URL (check types)
            
            parameter2.setDescription("parameter description");
            
            parameter2.setIn("path");
            
            PathParameter parameter3 = new PathParameter();
            parameter3.setName("blid");
            parameter3.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter3.setEnum(readEntities.getEntities());
            parameter3.setType("string"); //TODO should it be URL (check types)
            
            parameter3.setDescription("parameter description");
            
            parameter3.setIn("path");

            Operation operation = new Operation();
            operation.addParameter(parameter1);
            operation.addParameter(parameter2);
            operation.addParameter(parameter3);
            operation.description("operation description");
            
            Path path = new Path();
            path.setGet(operation);
            
            swagger.path("/{source}/{rdftype}/{blid}/", path);
    }
    
    private void generateOperations(){
            
            operationMetadataSources();
            operationSource();
            operationClass();            
            operationSubjectList();
            operationSubject();
            
            //SharingHolder sharing2 = sharing().pathPrefix("/getItem").tag("getItem");
    }
    
    
    
    private void generateQueryOperations(){
        ReadQueriesFromFile readQueriesFromFile = new ReadQueriesFromFile();
        List<NamedQueryEntity> namedQueryList = readQueriesFromFile.getNamedQueryList();
    
       // System.out.println(namedQueryList.);
        
        Operation operation = new Operation();
        
        String sPath = "/pathwayListBySpecies/";
        
        for(NamedQueryEntity nqe : namedQueryList){
            List<QueryVariable> variableList = nqe.getVariableList();
            
            for(QueryVariable queryVariable : variableList){
                
                PathParameter parameter = new PathParameter();
                parameter.setName( queryVariable.getLabel() );
                parameter.setRequired(true); //TODO think about this (should it be empty and list entiies)?

                //parameter.setEnum(readEntities.getEntities());
                parameter.setType("string"); 

                parameter.setDescription("parameter description");

                parameter.setIn("path");
                
                operation.addParameter(parameter);
                
                 
                sPath += "{"+queryVariable.getLabel()+"}/" ;
            }
            

                  
        }
        
        Path path = new Path();
        path.setGet(operation);
        //this.swagger.path(sPath, path);
        
    }
    
    public void registerOperation(String domain, String sPath){
     
         sPath = "/{source}/{rdftype}/{blid}/";
         
         String[] pathSegments = sPath.split("/");
         
         //String[] pathSegments = (sPath.charAt(0) == '/' ? sPath.substring(1) : sPath).split("/");        
         
         Operation operation = new Operation();
         
         for(String pathSegment : pathSegments){
             if(isParameter(pathSegment)){
                 
                PathParameter parameter = new PathParameter();
                parameter.setName(pathSegment);
                parameter.setRequired(true); //TODO think about this (should it be empty and list entities)?

                //parameter.setEnum(readEntities.getEntities());
                parameter.setType("string"); //TODO should it be URL (check types)
                parameter.setDescription("parameter description");
                parameter.setIn("path");
                
                operation.addParameter(parameter);
             };
 
            Path path = new Path();
            path.setGet(operation);
                   
            swagger.path(sPath, path);
         }
    }
    
    public String getSwaggerJson(){
               String jsonOutput = Json.pretty(this.swagger);
               return jsonOutput;
    }

    private boolean isParameter(String pathSegment) {
       System.out.println(pathSegment);
       // create logic
       return true;
    }
    
   public static SwaggerTest getInstance( ) {
      return singleton;
   }

}
