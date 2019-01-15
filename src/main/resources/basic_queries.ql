# Example query1
# /path/{var}
SELECT ?s ?p ?o WHERE { ?s rdf:type <?_var> ?p ?o.} ORDER BY ?s";

# Example query 2
# /path/{var}/{var2}
SELECT ?x ?p ?t WHERE { ?x rdf:type <?_var>  <?_var2> ?p ?t.} ORDER BY ?x";