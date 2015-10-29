package com.mattwemmie.depanalyzer;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Component // doesn't seem to be registering...
public class SourceCodeVisitor extends SimpleFileVisitor<Path> {
	
	@Autowired
	public SourceCodeVisitor(JavaPackageRepository javaPackageRepository, GraphDatabase graphDatabase, Neo4jTemplate neo4jTemplate) {
		this.javaPackageRepository = javaPackageRepository;
		this.graphDatabase = graphDatabase;
		this.neo4jTemplate = neo4jTemplate;
	}

	private JavaPackageRepository javaPackageRepository;
	private GraphDatabase graphDatabase;
	private Neo4jTemplate neo4jTemplate;
	
/*	public static void main(String[] args) throws IOException {
		
		Path startingDir = Paths.get("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer");
		//Path startingDir = Paths.get("/Users/mattwemmie/Desktop/Java/Git");

		Files.walkFileTree(startingDir, new MyFileVisitor());
	}*/
		
	private final PathMatcher matcher = FileSystems.getDefault()
            .getPathMatcher("glob:*.java");
	
    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file,
                                   BasicFileAttributes attr) throws IOException {
    	
    	if(isJava(file)) {
	        FileInputStream in = new FileInputStream(file.toFile());

	        try {
	            // parse the file
	        	CompilationUnit cu = JavaParser.parse(in);
	        	
	        	System.out.println("File=" + file.getFileName());
	        	System.out.println("contains " + cu.getEndLine() + " lines of code");
	        	
	        	
	        	
	        	String packageName = new PackageDeclarationVisitor().visit(cu,  null);
	        	System.out.println("Package=" + packageName);
	        	JavaPackage javaPackage = new JavaPackage(packageName);
	    		// currently this inserts a new one for each duplicate - if don't, it blows up later when building relationships (not sure why)
	    		javaPackageRepository.save(javaPackage);
	    		
	    		// class
	    		String fullyQualifiedClass = packageName + "." + new ClassOrInterfaceDeclarationVisitor().visit(cu, null);
	    		System.out.println("Fully qualified class=" + fullyQualifiedClass);
	    		JavaClass javaClass = new JavaClass(fullyQualifiedClass);
	        	javaClass.linesOfCode = cu.getEndLine();
	        	neo4jTemplate.save(javaClass);
		    		
	        	List<String> imports = new ArrayList<>();
	        	new ImportDeclarationVisitor().visit(cu,  imports);
	        	System.out.println("imports in " + file.getFileName() + " are " + imports);
	        	for(String importedPackage : imports) {
	        		
	        		// filter out specific dependencies (don't create nodes/rels for them)
	        		if(!importedPackage.startsWith("java")) {
			        	JavaPackage dependencyPackage = new JavaPackage(importedPackage);
		        		// currently this inserts a new one for each duplicate
		        		javaPackageRepository.save(dependencyPackage);
	
			    		PackageDependency dep = new PackageDependency(javaPackage, dependencyPackage);
			    		neo4jTemplate.save(dep);		
	        		}
	        	}
	        	
	        } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            in.close();
	        }
    	}
        
        
        return CONTINUE;
    }
    
    private boolean isJava(Path file) {
       Path name = file.getFileName();
       return name != null && matcher.matches(name);
    }
    
    private void debugVisitFile(Path file,
                                   BasicFileAttributes attr) {
    	if (attr.isSymbolicLink()) {
            System.out.format("Symbolic link: %s ", file);
        } else if (attr.isRegularFile()) {
            System.out.format("Regular file: %s ", file);
        } else {
            System.out.format("Other: %s ", file);
        }
        
        System.out.println("(" + attr.size() + "bytes)");
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir,
                                          IOException exc) {
        //System.out.format("Directory: %s%n", dir);
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException 
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                       IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
	
    private class PackageDeclarationVisitor extends GenericVisitorAdapter<String, Object> {
    	
    	@Override
    	public String visit(PackageDeclaration n, Object arg) {
    		//System.out.println("visiting package declaration");
    		//System.out.println("Package=" + n.getName());
    		JavaPackage javaPackage = new JavaPackage(n.getName().toString());
    		return n.getName().toString();
    		// currently this inserts a new one for each duplicate
    		//javaPackageRepository.save(javaPackage);
    	}
    }
    
    private class ImportDeclarationVisitor extends VoidVisitorAdapter<List<String>> {
    	
    	@Override
    	public void visit(ImportDeclaration n, List<String> imports) {

    		//System.out.println("Import from ImportDeclVisitor=" + n.getName());
    		String importDeclaration = n.getName().toString();
    		importDeclaration = importDeclaration.substring(0, importDeclaration.lastIndexOf('.'));
    		//System.out.println(importDeclaration);
    		imports.add(importDeclaration);
    		
    		
    	}
    }
    
    private class ClassOrInterfaceDeclarationVisitor extends GenericVisitorAdapter<String, Object> {
    	
    	@Override
    	public String visit(ClassOrInterfaceDeclaration n, Object arg) {
    		return n.getName();
    	}
    }
}
