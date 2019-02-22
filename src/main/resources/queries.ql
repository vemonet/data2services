# Retrieve all sources (graph)
# /explore
# explore
PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX dctypes: <http://purl.org/dc/dcmitype/>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX idot: <http://identifiers.org/idot/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX void: <http://rdfs.org/ns/void#>
PREFIX bl: <http://w3id.org/biolink/vocab/>
SELECT ?source
WHERE {
        ?dataset a dctypes:Dataset ;
            idot:preferredPrefix ?source .
        ?version dct:isVersionOf ?dataset ; 
            dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
}

# Retrieving all classes in the triplestore (all rdf:type) with count
# /explore/{source}/classes
# explore
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://w3id.org/biolink/vocab/>
SELECT ?source ?class ?classLabel ?count
WHERE
{
    {
        SELECT ?source ?class (count(?class) as ?count)  
        WHERE {
            ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .
            ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
            FILTER(?source = "?_source") # Get graph URI for provided source

            graph ?graph {
                [] a ?class .
            }
        }
        group by ?graph ?class
        order by desc(?count)
    }
    OPTIONAL {
        ?class rdfs:label ?classLabel .
    }
}

# Retrieving all classes in a source (and adding a /all keyword?) (no count at the moment? Use explore for that)
# /{source}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://w3id.org/biolink/vocab/>
PREFIX dctypes: <http://purl.org/dc/dcmitype/>
PREFIX idot: <http://identifiers.org/idot/>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX void: <http://rdfs.org/ns/void#>
SELECT ?source ?class ?classLabel ?count
WHERE
{
    {
        SELECT ?source ?class (count(?class) as ?count)  
        WHERE {
            ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .
            ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
            FILTER(?source = "?_source") # Get graph URI for provided source
            
            graph ?graph {
                [] a ?class .
            }
        }
        group by ?source ?class
        order by desc(?count)
    }
    optional {
        ?class rdfs:label ?classLabel .
    }
}

# Retrieving the list of entities corresponding to the asked type
# /{source}/{class}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://w3id.org/biolink/vocab/>
SELECT ?source ?class ?entity
WHERE 
{   
    ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .
    ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
    FILTER(?source = "?_source") # Get graph URI for provided source

    GRAPH ?graph 
    {
        ?entityUri a bl:Drug .
        ?entityUri a ?class .
        ?entityUri bl:id ?entity
    }
}

# If the provided is a class retrieving the item filtering by ID.
# /{source}/{class}/{id}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://w3id.org/biolink/vocab/>
PREFIX dctypes: <http://purl.org/dc/dcmitype/>
PREFIX idot: <http://identifiers.org/idot/>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX void: <http://rdfs.org/ns/void#>
SELECT ?source ?class ?entity ?property #?value should we put value also?
WHERE
{
    ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .
    ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
    FILTER(?source = "?_source") # Get graph URI for provided source

    GRAPH ?graph
    {
        ?entityUri a ?_class .
        ?entityUri a ?class .
        ?entityUri bl:id ?entity .
        ?entityUri ?property ?value .
        FILTER(?entity = "?_id")
    }
}

# Get the property of the retrieved entity.
# /{source}/{class}/{id}/{property}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://w3id.org/biolink/vocab/>
PREFIX dctypes: <http://purl.org/dc/dcmitype/>
PREFIX idot: <http://identifiers.org/idot/>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX void: <http://rdfs.org/ns/void#>
SELECT ?source ?class ?entity ?property ?value
WHERE
{
    ?dataset a dctypes:Dataset ; idot:preferredPrefix ?source .
    ?version dct:isVersionOf ?dataset ; dcat:distribution [ a void:Dataset ; dcat:accessURL ?graph ] . 
    FILTER(?source = "?_source") # Get graph URI for provided source

    GRAPH ?graph
    {
        ?entityUri a ?_class .
        ?entityUri a ?class .
        ?entityUri bl:id ?entity .
        ?entityUri ?property ?value .
        FILTER(?entity = "?_id")
        FILTER(?property = ?_property)
    }
}