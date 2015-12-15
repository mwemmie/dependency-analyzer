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
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.VariableDeclarator;
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
	        	
	        	//System.out.println("File=" + file.getFileName());
	        	//System.out.println("contains " + cu.getEndLine() + " lines of code");
	        	
	        	String packageName = new PackageDeclarationVisitor().visit(cu,  null);
	        	System.out.println("Package=" + packageName);
	        	
	        	if(packageName == null) {
	        		packageName = "ROOT";
	        	}
	        	
	        	JavaPackage javaPackage = new JavaPackage(packageName);
	    		// currently this inserts a new one for each duplicate - if don't, it blows up later when building relationships (not sure why)
	    		
	    		javaPackageRepository.save(javaPackage);
	    			
	    		// class
	    		String className = new ClassOrInterfaceDeclarationVisitor().visit(cu, null);
	    		String fullyQualifiedClass = packageName + "." + className;
	    		//System.out.println("Fully qualified class=" + fullyQualifiedClass);
	    		
	    		List<String> fields = new ArrayList<>();
	    		//System.out.println("Fields:");
	        	new FieldDeclarationVisitor().visit(cu, fields);
	        	
	        	List<String> methods = new ArrayList<>();
	        	new MethodDeclarationVisitor().visit(cu, methods);
	        	
	    		JavaClass javaClass = new JavaClass(fullyQualifiedClass, className, fields, methods);
	        	javaClass.linesOfCode = cu.getEndLine();
	        	System.out.println("JavaClass=" + javaClass.toString());
	        	neo4jTemplate.save(javaClass);
		    		
	        	List<String> imports = new ArrayList<>();
	        	new ImportDeclarationVisitor().visit(cu,  imports);
	        	System.out.println("imports in " + file.getFileName() + " are " + imports);
	        	for(String importedPackage : imports) {
	        		
	        		if(importedPackage != null) {
		        		// filter out specific dependencies (don't create nodes/rels for them)
		        		if(!importedPackage.startsWith("java")) {
				        	JavaPackage dependencyPackage = new JavaPackage(importedPackage);
			        		// currently this inserts a new one for each duplicate
			        		javaPackageRepository.save(dependencyPackage);
		
				    		PackageDependency dep = new PackageDependency(javaPackage, dependencyPackage);
				    		neo4jTemplate.save(dep);		
		        		}
	        		} else {
	        			System.out.println("nulll imported package for " + importedPackage);
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
    		if(n==null || n.getName()==null || n.getName().toString()==null) {
    			return "ROOT";
    		}
    		
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
    
    private class FieldDeclarationVisitor extends VoidVisitorAdapter<List<String>> {
    	
    	@Override
    	public void visit(FieldDeclaration n, List<String> fields) {

    		//System.out.println(n.toStringWithoutComments());
    		// prints "@GraphId Long id;"
    		
    		//System.out.println("modifiers=" + n.getModifiers() + ", Type=" + n.getType() + ", variables=" + n.getVariables());
    		
    		if(n.getModifiers() == ModifierSet.PRIVATE) { 
	    		if(n.getVariables().size() == 0) {
	    			fields.add(n.getType() + " " + n.getVariables().get(0));
	    		} else {
	    			for(VariableDeclarator variable : n.getVariables()) {
	    				fields.add(n.getType() + " " + variable);
	    			}
	    		}
    		}
    		// modifiers 0=package private, 1=public, 2=private
    		// prints "modifiers=0, Type=Long, variables=[id]"
    		
    		/*
    		 * Could use above info to filter appropriately for just what we care about based on 
    		 * modifier, types, variable name, annotation, etc.
    		 */
    	}
    }
    
    private class MethodDeclarationVisitor extends VoidVisitorAdapter<List<String>> {
    	
    	@Override
    	public void visit(MethodDeclaration n, List<String> fields) {

    		if(n.getModifiers() == ModifierSet.PUBLIC) {
    			fields.add(n.getDeclarationAsString(false, false));
    			
    			//System.out.println(n.getDeclarationAsString(false, false)); // Object clone()
    			
        		// Can do it however we want - some other options include:
        		//System.out.println(n.getDeclarationAsString());  // protected Object clone() throws CloneNotSupportedException
        		//System.out.println(n.getDeclarationAsString(true, false, true)); // protected Object clone()
    		}


    	}
    }
}
