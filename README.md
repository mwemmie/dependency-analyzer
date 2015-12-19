# dependency-analyzer

# Overview
This project will build a basic graph of Java packages and classes, the relationships between them, and also capture basic class level information such as public methods and private instance variables. 

While there are other tools out there that build graphs against Java, it seems like most either require you to physically compile java source code or even have all those classes in an IDE such as Eclipse.  The goal of this project is to do graph analysis based on source code and then be able to export the graph somewhere useful - initially the Neo4j graph database.  I will caveat that I am not an expert in navigating the Java AST, nor am I an expert at graph databases.  I know there are challenges and pitfalls for attempting to built a graph against source code in the Java language - my hope is to create something that is useful for many use cases and be "good enough". 

In this intial implementation, a root folder is specified, then all subfolders are traversed, analyzed, and then built into a graph, which is exported to .csv files that can then be loaded into a neo4j database using "scripts/import_csv.cypher".

Word of caution, this is still in very early development, the code is not very clean, yet, and there are a lot of hard coded paths, etc. that eventually should be cleaned up, externalized, etc.  Just trying to get to a working prototype quickly at this point to see if this effort should continue :)

# Usage
* Clone this repo
* Edit source code to change paths as necessary (hope to externalize this config soon!)
* Run "mvn package" to build an executable jar file
* Execute the built jar file using "java -jar <filename" to perform static analysis

# Todo (non-exhaustive list)
* Remove hard coded paths
* Add better doc
* Externalize configuration
* Tune neo4j load queries for better performance

# Screenshots
Below is an example of a graph built with this project that was loaded into a neo4j database and then viewed using the built in neo4j app.
![Alt text](images/neo4jgraph.png?raw=true "sample neo4j graph created")
