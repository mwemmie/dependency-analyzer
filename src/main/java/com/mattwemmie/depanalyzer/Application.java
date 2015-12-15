package com.mattwemmie.depanalyzer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Configuration
	@ComponentScan("com.mattwemmie.depanalyzer")
	@EnableNeo4jRepositories(basePackages = "com.mattwemmie.depanalyzer")
	static class ApplicationConfig extends Neo4jConfiguration {

		public ApplicationConfig() {
			setBasePackage("com.mattwemmie.depanalyzer");
		}

		@Bean
		GraphDatabaseService graphDatabaseService() {
			//return new GraphDatabaseFactory().newEmbeddedDatabase("accessingdataneo4j.db");
			// TODO:  Added dependency to spring rest graph in pom.xml but when i try it get weird runtime errors
			// likely due to version mismatches.
			return new SpringRestGraphDatabase("http://localhost:7474/db/data");
		}
		
		@Bean
		public Neo4jTemplate neo4jTemplate() {
			return new Neo4jTemplate(graphDatabaseService());
		}
	}

	@Autowired JavaPackageRepository javaPackageRepository;

	@Autowired GraphDatabase graphDatabase;
	
	@Autowired Neo4jTemplate neo4jTemplate;

	public void run(String... args) throws Exception {

		/*JavaPackage javaPackage1 = new JavaPackage("com.mattwemmie.test1");
		JavaPackage javaPackage2 = new JavaPackage("com.mattwemmie.test2");*/
		
		System.out.println("Before linking up with Neo4j...");

		Transaction tx = graphDatabase.beginTx();
		
		try {

			//Path startingDir = Paths.get("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer");
			Path startingDir = Paths.get("/Users/mattwemmie/Desktop/Java/Git");
			
			Files.walkFileTree(startingDir, new SourceCodeVisitor(javaPackageRepository, graphDatabase, neo4jTemplate));
			
/*			JavaPackage a = new JavaPackage("A");
			javaPackageRepository.save(a);
			
			JavaPackage b = new JavaPackage("B");
			javaPackageRepository.save(b);
			
			PackageDependency dep = new PackageDependency(a, b);
			
			neo4jTemplate.save(dep);
			
			// seems like it recognizes the presence of relationship type, but "name" is null
			JavaPackage retrievedA = javaPackageRepository.findByName("A");
			System.out.println(retrievedA.toString());*/
			
			
			/*javaPackageRepository.save(javaPackage1);
			javaPackageRepository.save(javaPackage2);
			
			greg = personRepository.findByName(greg.name);
			greg.worksWith(roy);
			greg.worksWith(craig);
			personRepository.save(greg);

			roy = personRepository.findByName(roy.name);
			roy.worksWith(craig);
			// We already know that roy works with greg
			personRepository.save(roy);

			// We already know craig works with roy and greg

			System.out.println("Lookup each java package by name...");
			for (String name : new String[] { javaPackage1.name, javaPackage2.name }) {
				System.out.println(javaPackageRepository.findByName(name));
			}*/

/*			System.out.println("Looking up who works with Greg...");
			for (Person person : personRepository.findByTeammatesName("Greg")) {
				System.out.println(person.name + " works with Greg.");
			}*/

			tx.success();
		} finally {
			tx.close();
		}
	}

	public static void main(String[] args) throws Exception {
		FileUtils.deleteRecursively(new File("accessingdataneo4j.db"));

		SpringApplication.run(Application.class, args);
	}
}
