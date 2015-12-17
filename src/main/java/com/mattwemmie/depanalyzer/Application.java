package com.mattwemmie.depanalyzer;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public void run(String... args) throws Exception {

		ICsvBeanWriter javaClassCsvWriter = new CsvBeanWriter(new FileWriter("/Users/mattwemmie/Desktop/neo4j/javaClasses.csv"),
                CsvPreference.STANDARD_PREFERENCE);
		
		ICsvBeanWriter javaPackageDependencyCsvWriter = new CsvBeanWriter(new FileWriter("/Users/mattwemmie/Desktop/neo4j/javaPackageDeps.csv"),
                CsvPreference.STANDARD_PREFERENCE);

        try {
        	
        	final String[] javaClassHeader = new String[] { "fullyQualifiedName", "name", "csvFormattedPublicMethods", "linesOfCode", "csvFormattedPrivateInstanceVariables", "javaPackage" };
        	javaClassCsvWriter.writeHeader(javaClassHeader);
        	
        	final String[] javaPackageDependencyHeader = new String[] { "startPackage", "endPackage" };
        	javaPackageDependencyCsvWriter.writeHeader(javaPackageDependencyHeader);
        	
			//Path startingDir = Paths.get("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer");
			Path startingDir = Paths.get("/Users/mattwemmie/Desktop/Java/Git/swagger-springmvc-example");
			//Path startingDir = Paths.get("/Users/mattwemmie/Desktop/Java/Git");

			
			Files.walkFileTree(startingDir, new SourceCodeVisitor(javaClassCsvWriter, javaPackageDependencyCsvWriter));
			
		} finally {

			if( javaClassCsvWriter != null ) {
                javaClassCsvWriter.close();
			}
			
			if( javaPackageDependencyCsvWriter != null ) {
                javaPackageDependencyCsvWriter.close();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
