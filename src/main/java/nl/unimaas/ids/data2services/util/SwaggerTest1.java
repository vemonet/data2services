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

/**
 *
 * @author nuno
 */
public class SwaggerTest1 {
    private Swagger swagger;
     
    public static void main(String[] args) {
        new SwaggerTest().doSwagger();
    }

    public SwaggerTest1() {
    }

    public Swagger doSwagger() {

        swagger = new Swagger();

        swagger.info(new Info()
                .title("Bio2RDF")
                .description("Bio2RDF is an open source project to generate and provide linked data for the life sciences.")
                .termsOfService("https://github.com/bio2rdf/bio2rdf-scripts/wiki/Terms-of-use")
                .contact(new Contact().name("Michel Dumontier").email("michel.dumontier@gmail.com"))
                .license(new License().name("MIT License")
                        .url("https://github.com/bio2rdf/bio2rdf-scripts/blob/master/MIT-LICENSE.txt")
                )
        ).host("localhost:8080")
                .basePath("/api")
                //.consumes("application/json").consumes("application/xml")
                .produces("application/json").produces("application/xml");
        
                
                
        

        
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
    
    private void generateOperations(){
        
        ReadEntitiesFromFile readEntities = new ReadEntitiesFromFile();
        
        List<IRIEntity> entityList = readEntities.getClassList();
        
            for(IRIEntity iriEntity : entityList){
        
                PathParameter parameter1 = new PathParameter();
                parameter1.setName("entityURI");
                parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?

                parameter1.setEnum(readEntities.getEntities());
                parameter1.setType("string"); //TODO should it be URL (check types)

                parameter1.setDescription("parameter description");

                parameter1.setIn("path");


                PathParameter parameter2 = new PathParameter();
                parameter2.setName("URI");
                parameter2.setRequired(false); 

                //parameter2.setEnum(readEntities.getEntities());
                parameter2.setType("string"); //TODO should it be URL (check types)

                parameter2.setDescription("parameter description");

                parameter2.setIn("path");




                Operation operation = new Operation();
                operation.addParameter(parameter1);
                operation.description("operation description");

                operation.addParameter(parameter2);


                Path path = new Path();
                path.setGet(operation);

                swagger.path("/"+iriEntity.getLabel()+"/{entityURI}", path);
            
            }

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
        this.swagger.path(sPath, path);
        
    }
    
    public String getSwaggerJson(){
               String jsonOutput = Json.pretty(doSwagger());
               return jsonOutput;
    }

}
