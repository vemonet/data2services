package nl.unimaas.ids.data2services.util;

import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.PathParameter;
import io.swagger.util.Json;
import java.util.ArrayList;
import java.util.List;
import nl.unimaas.ids.data2services.model.Query;
import nl.unimaas.ids.data2services.model.QueryVariable;
import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.rdf2api.io.utils.Config;

/**
 *
 * @author nuno
 */
public class SwaggerManager {
    private static SwaggerManager singleton = new SwaggerManager();
    private Swagger swagger;
     
    public static void main(String[] args) {
        new SwaggerManager().doSwagger();
    }

    public SwaggerManager() {
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
        //generateQueryOperations();
        //generateOperations();

        
        return swagger;
    }
    
//    private void operationMetadataSources(){
//        String sPath = "/metadata/sources/";
//        String operationDescription = "test";
//        
//        Operation operation = new Operation();
//        operation.description(operationDescription);
//        
//        //operation.addProduces("json");
//        operation.addResponse("200", new Response());
//
//        Path path = new Path();
//        path.setGet(operation);
//
//        swagger.path(sPath, path);
//    }
    
    private void operationSource(){
        String sPath = "/{source}/";
        String operationDescription = "test";
        
        PathParameter parameter1 = new PathParameter();
        parameter1.setName("source");
        parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?

        parameter1.setDescription("parameter description");

        parameter1.setIn("path");
       
        Operation operation = new Operation();
        operation.description(operationDescription);
        operation.addParameter(parameter1);
        
        operation.addResponse("200", new Response());

        Path path = new Path();
        path.setGet(operation);

        swagger.path(sPath, path);
    }
    
    private void operationClass(){
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("source");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter1.setDescription("parameter description");
            
            parameter1.setIn("path");
            
            PathParameter parameter2 = new PathParameter();
            parameter2.setName("class");
            parameter2.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter2.setDescription("parameter description");
            
            parameter2.setIn("path");

            Operation operation = new Operation();
            operation.addParameter(parameter1);
            operation.addParameter(parameter2);
            operation.description("operation description");
            
            operation.addResponse("200", new Response());
          
            Path path = new Path();
            path.setGet(operation);
            
            swagger.path("/{source}/{rdftype}", path);
            
            
    }
    
    private void operationSubjectList(){
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("subjectClass");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
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
            PathParameter parameter1 = new PathParameter();
            parameter1.setName("id");
            parameter1.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter1.setDescription("parameter description");
            
            parameter1.setIn("path");
            
            PathParameter parameter2 = new PathParameter();
            parameter2.setName("source");
            parameter2.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter2.setDescription("parameter description");
            
            parameter2.setIn("path");
            
            PathParameter parameter3 = new PathParameter();
            parameter3.setName("blid");
            parameter3.setRequired(true); //TODO think about this (should it be empty and list entiies)?
            
            parameter3.setDescription("parameter description");
            
            parameter3.setIn("path");

            Operation operation = new Operation();
            operation.addParameter(parameter1);
            operation.addParameter(parameter2);
            operation.addParameter(parameter3);
            operation.description("operation description");
            
            Path path = new Path();
            path.setGet(operation);
            
            operation.addResponse("200", new Response());
            

            
            swagger.path("/{source}/{rdftype}/{blid}/", path);
    }
    
    private void generateOperations(){
            
           /// operationMetadataSources();
           
            //operationSource();
            //operationClass();            
            //operationSubjectList();
            //operationSubject();
            
            //SharingHolder sharing2 = sharing().pathPrefix("/getItem").tag("getItem");
    }
    
    
    
//    private void generateQueryOperations(){
//        ReadQueriesFromFile readQueriesFromFile = new ReadQueriesFromFile();
//        List<NamedQueryEntity> namedQueryList = readQueriesFromFile.getNamedQueryList();
//    
//       // System.out.println(namedQueryList.);
//        
//        Operation operation = new Operation();
//        
//        String sPath = "/pathwayListBySpecies/";
//        
//        for(NamedQueryEntity nqe : namedQueryList){
//            List<QueryVariable> variableList = nqe.getVariableList();
//            
//            for(QueryVariable queryVariable : variableList){
//                
//                PathParameter parameter = new PathParameter();
//                parameter.setName( queryVariable.getLabel() );
//                parameter.setRequired(true); //TODO think about this (should it be empty and list entiies)?
//
//                //parameter.setEnum(readEntities.getEntities());
//                parameter.setType("string"); 
//
//                parameter.setDescription("parameter description");
//
//                parameter.setIn("path");
//                
//                operation.addParameter(parameter);
//                
//                 
//                sPath += "{"+queryVariable.getLabel()+"}/" ;
//            }
//            
//
//                  
//        }
//        
//        Path path = new Path();
//        path.setGet(operation);
//        //this.swagger.path(sPath, path);
//        
//    }
    
    public void registerOperation(ServiceRealm realm, Query query){
         
         String sPath = query.getPath();
         if(sPath!=null)
         System.out.println("sPath "+sPath);
         else System.out.println("path is null");
         
         //TODO Make util for this method (used in more than one class)
         //List<String> variableList = this.parseVariblesFromPath(sPath);
         List<QueryVariable> variableList2 = query.getVariables();
         
         //String[] pathSegments = (sPath.charAt(0) == '/' ? sPath.substring(1) : sPath).split("/");        
         Operation operation = new Operation();
         
         operation.setDescription(query.getLabel());
         
         for(QueryVariable queryVariable : variableList2){
      
                    PathParameter parameter = new PathParameter();
                    parameter.setName(queryVariable.getLabel()); //TODO improve - logic shouldnt be here
                    parameter.setRequired(true);  //TODO think about this (should it be empty and list entities)?

                    //parameter.setEnum(readEntities.getEntities());
                    parameter.setType("string"); //TODO should it be URL (check types)
                    parameter.setDescription(queryVariable.getLabel());
                    parameter.setIn("path");

                    operation.addParameter(parameter);
             }
            
            operation.addResponse("200", new Response());

            List<String> tagList = query.getTagList();
            for(String tag : tagList)
                operation.addTag(tag);
            
            List<String> contentTypeList = query.getContentTypeList();
            for(String contentType : contentTypeList)
                operation.addProduces(contentType);
            
            Path path = new Path();
            path.setGet(operation);
            
            
            
            String sRealm = realm.getRealm().isPresent() ? "/"+ realm.getRealm().get() : "";
            swagger.path( sRealm + sPath, path);
         }
    
    private List<String> parseVariblesFromPath(String sPath){
        
            sPath = sPath.charAt(0) == '/' ? sPath.substring(1) : sPath;
        
            String[] pathSegments = sPath.split("/");
            
            ArrayList<String> variableList = new ArrayList<String>();
            
            for(String pathSegment : pathSegments){
                if(isParameter(pathSegment))
                      variableList.add(pathSegment);
            }
            
            return variableList;
     }
    
    private boolean isParameter(String pathSegment) {
       return pathSegment!=null && pathSegment.length()>0 && pathSegment.charAt(0)=='{';
    }
    
    public String getSwaggerJson(){
               String jsonOutput = Json.pretty(this.swagger);
               return jsonOutput;
    }


    
    public static SwaggerManager getInstance() {
       return singleton;
    }

}
