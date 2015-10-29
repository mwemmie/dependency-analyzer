package com.mattwemmie.depanalyzer;



import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Run {

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Get an instance of java compiler
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

		//Get a new instance of the standard file manager implementation
		StandardJavaFileManager fileManager = compiler.
		        getStandardFileManager(null, null, null);
		        
		// Get the list of java file objects, in this case we have only 
		// one file, TestClass.java
		Iterable<? extends JavaFileObject> compilationUnits1 = 
		        fileManager.getJavaFileObjects("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer/src/main/java/com/mattwemmie/depanalyzer/TestClass.java");
		
		CompilationTask task = compiler.getTask(null, fileManager, null,
                null, null, compilationUnits1);
                
		//Perform the compilation task.
		task.call();
	}*/

}
