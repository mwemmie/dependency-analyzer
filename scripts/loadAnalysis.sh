#!/bin/bash
bin/neo4j stop
rm -r data/graph.db
bin/neo4j-shell -path data/graph.db -file import_csv.cypher
bin/neo4j start
