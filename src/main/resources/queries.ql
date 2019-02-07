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
SELECT ?source ?graph 
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
select ?graph ?class ?classLabel ?count
from named <?_graph> # Remove it to get for all graphs
where
{
    {
        select ?graph ?class (count(?class) as ?count)  
        where {
            graph ?graph {
                [] a ?class .
            }
        }
        group by ?graph ?class
        order by desc(?count)
    }
    optional {
        ?class rdfs:label ?classLabel .
    }
}

# Retrieving all classes in a source (and adding a /all keyword?) (no count at the moment? Use explore for that)
# /{source}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
select ?graph ?class ?classLabel ?count
from named <?_graph> # Remove it to get for all graphs
where
{
    {
        select ?graph ?class (count(?class) as ?count)  
        where {
            graph ?graph {
                [] a ?class .
            }
        }
        group by ?graph ?class
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
PREFIX bl: <http://bioentity.io/vocab/>
select ?graph ?class ?entity
from named <?_graph> # Remove it to get for all graphs
where 
{
    graph ?graph 
    {
        ?entity a ?_class .
        ?entity a ?class .
    }
}

# If the provided is a class retrieving the item filtering by ID.
# /{source}/{class}/{id}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://bioentity.io/vocab/>
select ?graph ?class ?entity ?property #?value should we put value also?
from named <?_graph>
where
{
    GRAPH ?graph
    {
        ?entity a ?_class .
        ?entity a ?class .
        ?entity bl:id ?_id .
        ?entity ?property ?value .
    }
}

# Get the property of the retrieved entity.
# /{source}/{class}/{id}/{property}
# query
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX dct: <http://purl.org/dc/terms/>
PREFIX bl: <http://bioentity.io/vocab/>
select ?graph ?class ?entity ?property ?value
from named <?_graph>
where
{
    GRAPH ?graph
    {
        ?entity a ?_class .
        ?entity a ?class .
        ?entity bl:id ?_id .
        ?entity ?property ?value .
    }
}