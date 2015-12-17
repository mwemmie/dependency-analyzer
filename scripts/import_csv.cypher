CREATE INDEX ON :JavaPackage(name);
CREATE INDEX ON :JavaClass(fullyQualifiedName);

LOAD CSV WITH HEADERS FROM "file:javaClasses.csv" AS csvLine 
MERGE (javaPackage:JavaPackage { name: csvLine.javaPackage })
CREATE (javaClass:JavaClass { fullyQualifiedName: csvLine.fullyQualifiedName, name: csvLine.name, linesOfCode: csvLine.linesOfCode, privateInstanceVariables: 
CASE csvLine.csvFormattedPrivateInstanceVariables
WHEN "" THEN NULL
ELSE split(csvLine.csvFormattedPrivateInstanceVariables,";") END
, publicMethods:
CASE csvLine.csvFormattedPublicMethods
WHEN "" THEN NULL
ELSE split(csvLine.csvFormattedPublicMethods,";") END
  })
CREATE (javaPackage)-[:CONTAINS_CLASS]->(javaClass); 


LOAD CSV WITH HEADERS FROM "file:javaPackageDeps.csv" AS csvLine 
MERGE (startPackage:JavaPackage { name: csvLine.startPackage })
MERGE (endPackage:JavaPackage { name: csvLine.endPackage })
MERGE (startPackage)-[:DEPENDS_ON_PACKAGE]->(endPackage);





