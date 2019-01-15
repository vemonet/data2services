# Example query0 from file
# /metadata/sources
PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
PREFIX dcat: <http://www.w3.org/ns/dcat#>
PREFIX dctypes: <http://purl.org/dc/dcmitype/>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX idot: <http://identifiers.org/idot/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX void: <http://rdfs.org/ns/void/>
SELECT ?source ?graph 
WHERE {
    GRAPH <http://data2services/metadata/datasets>
    {
        ?dataset a dctypes:Dataset ;
            idot:preferredPrefix ?source ;
            dcat:accessURL ?graph .
        ?version dct:isVersionOf ?dataset ; 
            dcat:distribution [ a void:Dataset ] .  
    }
}

#Example 2 from file
#/{source}
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
SELECT ?class ?classLabel ?classCount
WHERE
{
    {
        select ?g ?class (count(?class) as ?classCount)  
        where {
            graph ?g {
                [] a ?class .
            }
            # Should be a variable (source)
            FILTER(?g = <?_source>)
        }
        group by ?g ?class
        order by desc(?classCount)
    }
    optional {
        ?class rdfs:label ?classLabel .
    }
}

# Example 2 from file
# /{source}/{type}
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://bioentity.io/vocab/>
SELECT ?item
WHERE 
{
    # Should be a variable (source)
    GRAPH <?_source> 
    {
        # Should be a variable (type)
        ?item a ?_type .
    }
}

# Example3 from file
# /{source}/{class}/{id}
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://bioentity.io/vocab/>
SELECT ?predicate ?object
WHERE 
{
    # Should be a variable (source)
    GRAPH <?_source> 
    {
        # Should be a variable (type)
        ?item a ?_type .
        # Should be a variable (id)
        ?item bl:id "?_object" .
        ?item ?predicate ?object .
    }
}
