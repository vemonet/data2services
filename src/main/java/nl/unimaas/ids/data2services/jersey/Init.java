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

package nl.unimaas.ids.data2services.jersey;

import com.github.tminglei.swagger.SharingHolder;
import com.github.tminglei.swagger.SwaggerContext;
import com.github.tminglei.swagger.bind.MappingConverterImpl;
import com.github.tminglei.swagger.fake.DataWriterImpl;
import com.github.tminglei.swagger.route.RouteFactoryImpl;
import com.github.tminglei.swagger.route.TreeRouterImpl;
import io.swagger.models.HttpMethod;
import io.swagger.models.Swagger;
import io.swagger.models.auth.In;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static com.github.tminglei.bind.Constraints.required;
import static com.github.tminglei.bind.Mappings.*;
import static com.github.tminglei.bind.Simple.*;
import static com.github.tminglei.swagger.SwaggerContext.*;

@Path("/sparql")
@Produces({"application/json", "application/xml"})
public class Init extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static {
        // Feed it a Swagger Java Object
        /*Swagger swaggerObject = new Swagger();
        swaggerObject.info(info().title("Bio2RDF")
                .description("Bio2RDF is an open source project to generate and provide linked data for the life sciences.")
                .termsOfService("https://github.com/bio2rdf/bio2rdf-scripts/wiki/Terms-of-use")
                .contact(contact().name("Michel Dumontier").email("michel.dumontier@gmail.com"))
                .license(license().name("MIT License")
                        .url("https://github.com/bio2rdf/bio2rdf-scripts/blob/master/MIT-LICENSE.txt")
                )
        ).host("localhost:8080")
                .basePath("/api")
                .consumes("application/json").consumes("application/xml")
                .produces("application/json").produces("application/xml")
                .securityDefinition("api_key", apiKeyAuth("api_key", In.HEADER))
                .securityDefinition("petstore_auth", oAuth2()
                        .implicit("http://petstore.swagger.io/api/oauth/dialog")
                        .scope("read:pets", "read your pets")
                        .scope("write:pets", "modify pets in your account")
                ).tag(tag("pet").description("Everything about your Pets")
                .externalDocs(externalDocs().description("Find out more").url("http://swagger.io"))
        ).tag(tag("store").description("Access to Petstore orders")
        ).tag(tag("user").description("Operations about user")
                .externalDocs(externalDocs().description("Find out more about our store").url("http://swagger.io"))
        );

        SwaggerContext.setInstance(new SwaggerContext(swaggerObject, new MappingConverterImpl(), new TreeRouterImpl(),
                new RouteFactoryImpl(), new DataWriterImpl()));*/


        swagger().info(info()
                .title("Bio2RDF")
                .description("Bio2RDF is an open source project to generate and provide linked data for the life sciences.")
                .termsOfService("https://github.com/bio2rdf/bio2rdf-scripts/wiki/Terms-of-use")
                .contact(contact().name("Michel Dumontier").email("michel.dumontier@gmail.com"))
                .license(license().name("MIT License")
                        .url("https://github.com/bio2rdf/bio2rdf-scripts/blob/master/MIT-LICENSE.txt")
                )
        ).host("localhost:8080")
                .basePath("/api")
                .consumes("application/json").consumes("application/xml")
                .produces("application/json").produces("application/xml")
                .securityDefinition("api_key", apiKeyAuth("api_key", In.HEADER))
                .securityDefinition("petstore_auth", oAuth2()
                        .implicit("http://petstore.swagger.io/api/oauth/dialog")
                        .scope("read:pets", "read your pets")
                        .scope("write:pets", "modify pets in your account")
                ).tag(tag("query").description("Everything about your queries")
                .externalDocs(externalDocs().description("Find out more").url("http://swagger.io"))
        ).tag(tag("store").description("Access to Petstore orders")
        ).tag(tag("user").description("Operations about user")
                .externalDocs(externalDocs().description("Find out more about our store").url("http://swagger.io"))
        );
    }


    static Mapping<?> getItemMapping = $(mapping(
            field("id", $(longv()).desc("pet id").example(3).$$),
            field("name", $(text(required())).desc("pet name").$$),
            field("item category", $(text()).desc("category belonged to").$$)
    )).refName("ItemRef").desc("Item info").$$;


    static SharingHolder sharing2 = sharing().pathPrefix("/getItem").tag("getItem");

    static {
        sharing2.operation(HttpMethod.GET, "/{prefix}:{identifier}")
                .summary("A item query.")
                .parameter(param(longv()).in("path").name("prefix").example(1l))
                .parameter(param(longv()).in("path").name("identifier").example(1l))
                .response(200, response(getItemMapping))
                .response(404, response().description("item not found"))
        ;
    }


    // To describe the object returned by the API
    static Mapping<?> sparqlMapping = $(mapping(
            field("id", $(longv()).desc("pet id").example(3).$$),
            field("name", $(text(required())).desc("pet name").$$),
            field("category", $(mapping(
                    field("id", longv(required())),
                    field("name", text(required()))
            )).desc("category belonged to").$$),
            field("photoUrls", $(list(text())).desc("pet's photo urls").example(Arrays.asList("http://example.com/photo1")).$$),
            field("tags", $(list(text())).desc("tags for the pet").example(Arrays.asList("tag1", "tag2")).$$)
    )).refName("SparqlRef").desc("Sparql info").$$;


    static SharingHolder sharing = sharing().pathPrefix("/sparql").tag("sparql");

    static {
        sharing.operation(HttpMethod.GET, "/:query<[0-9]+>")
                .summary("A valid SPARQL query.")
                .parameter(param(longv()).in("path").name("query").example(1l))
                .response(200, response(sparqlMapping))
                .response(404, response().description("sparql not found"))
        ;
    }

    @GET
    @Path("/{query}")
    public Response getSparql(@PathParam("query") String query) {
        return Response.ok().entity("<xml>Success</xml>").build();
    }


/* Code for OpenAPI (swagger-models 2.0.0, but still only a release candidate, and 1.5 is newer. So OpenAPI not supported
  public static void main(String[] args) {
    OpenAPI api = generateApi();

    System.out.println(api);
  }

  private static OpenAPI generateApi() {
    OpenAPI api = new OpenAPI();
    api.setInfo(generateInfo());
    api.setServers(generateServers());
    api.setPaths(generatePaths());

    return api;
  }

  private static Paths generatePaths() {
    Paths paths = new Paths();

    // here we have the Info from our Database extracted and we generate one set of
    // Paths for each Object
    paths.addPathItem("getItem", generatePathFor("getItem"));
    paths.addPathItem("sparql", generatePathFor("query"));

    return paths;
  }

  private static PathItem generatePathFor(String objectName) {
    PathItem pathItem = new PathItem();
    switch (objectName) {
      case "getItem":
        pathItem.addParametersItem(generateParameterItem("prefix", "path", "a valid prefix (see http://prefixcommons.org)"));
        pathItem.addParametersItem(generateParameterItem("identifier", "path", "a valid identifier for the resource"));
        break;
      case "query":
        pathItem.addParametersItem(generateParameterItem("query", "query", "A valid SPARQL query."));
        break;
      default:
        throw new RuntimeException("I don't know the Object \"" + objectName + "\"");
    }

    return pathItem;
  }

  private static Parameter generateParameterItem(String name, String in, String description) {
    Parameter parameter = new Parameter();
    parameter.setName(name);
    parameter.setIn(in);
    parameter.setDescription(description);
    parameter.setSchema(getSchemaFor(name));

    return parameter;

  }

  private static Schema getSchemaFor(String name) {
    Schema schema = new Schema();

    switch (name) {
      case "prefix":
      case "identifier":
      case "query":
        schema.setType(name);
        break;
      case "output":
        schema.setType(name);
        schema.addEnumItemObject((Object)"text/plain");
        schema.addEnumItemObject((Object)"text/html");
        schema.addEnumItemObject((Object)"application/rdf+xml");
      default:
        throw new RuntimeException("I don't know the Parameter \"" + name + "\"");
    }

    return schema;
  }

  private static List<Server> generateServers() {
    List<Server> servers = new ArrayList();

    servers.add(generateServer());

    return servers;
  }

  private static Server generateServer() {
    Server server = new Server();
    server.setUrl("http://bio2rdf.org");
    server.setDescription("Bio2RDF Production server");
    server.addExtension("x-location", "Maastricht, Netherlands");
    server.addExtension("x-maturity", "production");

    return server;
  }

  private static Info generateInfo() {
    Info info = new Info();
    info.setTitle("Bio2RDF");
    info.setDescription(
            "Bio2RDF is an open source project to generate and provide linked data for the life sciences.");
    info.setTermsOfService("https://github.com/bio2rdf/bio2rdf-scripts/wiki/Terms-of-use");

    info.setLicense(generateLicense());
    info.setContact(generateContact());

    info.setVersion("4.0.0");
    info.addExtension("x-implementationLanguage", "Java");
    info.addExtension("x-accessRestriction", "none");

    return info;
  }

  private static Contact generateContact() {
    Contact contact = new Contact();
    contact.setName("Michel Dumontier");
    contact.setEmail("michel.dumontier@gmail.com");

    contact.addExtension("x-role", "responsible-developer");
    contact.addExtension("x-id", "https://orcid.org/0000-0003-4727-9435");

    return contact;
  }

  private static License generateLicense() {
    License license = new License();
    license.setName("MIT License");
    license.setUrl("https://github.com/bio2rdf/bio2rdf-scripts/blob/master/MIT-LICENSE.txt");
    return license;
  }*/

}