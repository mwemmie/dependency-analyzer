package com.mattwemmie.depanalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import com.github.javaparser.ParseException;

public class OldDirectoryProcessor {

/*	public static void main(String args[]) {
        //listRecursive(new File("/Users/mattwemmie/Documents/workspace/depanalyzer/dependency-analyzer"));
		listRecursive(new File("/Users/mattwemmie/Desktop/Java/Git"));

	}*/
 
    /**
     * This method recursively lists all
     * .txt and .java files in a directory
     */
    private static void listRecursive(File dir) {
        Arrays.stream(dir.listFiles((f, n) ->
                     !n.startsWith(".")
                  &&
                     (new File(f, n).isDirectory()
                  ||  n.endsWith(".txt")
                  ||  n.endsWith(".java"))
              ))
              .forEach(unchecked((file) -> {
                  System.out.println(
                      file.getCanonicalPath()
                          .substring(new File(".")
                              .getCanonicalPath()
                              .length()));
 
                  if (file.isDirectory()) {
                      listRecursive(file);
                  } else {
                	  processFile(file);
                  }
              }));
    }
    
    private static void processFile(File file) throws ParseException, IOException {
    	FileAnalyzer.analyze(file);
    }
    
    
 
    /**
     * This utility simply wraps a functional
     * interface that throws a checked exception
     * into a Java 8 Consumer
     */
    private static <T> Consumer<T>
    unchecked(CheckedConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
 
    @FunctionalInterface
    private interface CheckedConsumer<T> {
        void accept(T t) throws Exception;
    }
}
