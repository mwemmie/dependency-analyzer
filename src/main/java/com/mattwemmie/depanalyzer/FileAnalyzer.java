package com.mattwemmie.depanalyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FileAnalyzer {

/*	public static void main(String[] args) throws ParseException, IOException {
	
		analyze(new File("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer/src/main/java/com/mattwemmie/depanalyzer/TestClass.java"));
        
	}*/
	
	public static void analyze(File file) throws ParseException, IOException {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(file);

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        new PackageDeclarationVisitor().visit(cu,  null);
        new ClassOrInterfaceVisitor().visit(cu,  null);
        new MethodDeclarationVisitor().visit(cu,  null);
	}
	
    private static class MethodDeclarationVisitor extends VoidVisitorAdapter<Object> {
    	
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
        	System.out.println("visiting method declaration");
            System.out.println(n.getName());
        }
    }
    
    private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter<Object> {

    	@Override
    	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
    		System.out.println("visiting class or interface declaration");
    		System.out.println(n.getName());
    	}
    }
    
    private static class PackageDeclarationVisitor extends VoidVisitorAdapter<Object> {
    	
    	@Override
    	public void visit(PackageDeclaration n, Object arg) {
    		System.out.println("visiting package declaration");
    		System.out.println(n.getName());
    	}
    }
    

}
