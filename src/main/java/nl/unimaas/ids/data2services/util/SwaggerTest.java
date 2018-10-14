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

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.auth.In;
import io.swagger.util.Json;
import nl.unimaas.ids.data2services.util.iface.ReadEntities;

/**
 *
 * @author nuno
 */
public class SwaggerTest {
    private Swagger swagger;
     
    public static void main(String[] args) {
        new SwaggerTest().doSwagger();
    }

    public SwaggerTest() {
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
                .consumes("application/json").consumes("application/xml")
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
        
        return swagger;
    }
    
    private void generateOperations(){
        
        ReadEntities readEntities = new ReadEntitiesFromFile();
        
        List entityList = readEntities.getEntities();
        
        Operation operation = new Operation();
        //operation.addParameter("helloParam");
        
        
        Path path = new Path();
        path.setGet(operation);
        swagger.path("/test2", path);
        

        //SharingHolder sharing2 = sharing().pathPrefix("/getItem").tag("getItem");
                   
    }
    
    public String getSwaggerJson(){
               String jsonOutput = Json.pretty(doSwagger());
               return jsonOutput;
    }

}
