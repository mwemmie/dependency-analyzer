package com.mattwemmie.depanalyzer;

import java.util.List;

public class JavaClass {

	private String fullyQualifiedName;
    private String name;
	private int linesOfCode;
	
	private List<String> privateInstanceVariables;
	private List<String> publicMethods;
	private String javaPackage;
	
	public JavaClass() {}
	
	public JavaClass(String fullyQualifiedName, String name, List<String> privateInstanceVariables, 
					 List<String> publicMethods, int linesOfCode, String javaPackage) {
		this.fullyQualifiedName = fullyQualifiedName;
		this.name = name;
		this.privateInstanceVariables = privateInstanceVariables;
		this.publicMethods = publicMethods;
		this.linesOfCode = linesOfCode;
		this.javaPackage = javaPackage;
	}
	
	private static String getCsvFormattedStringArray(List<String> list) {
		
		if(list.isEmpty()) {
			return null;
		}
		
		String temp = "";
		
		for(int i = 0; i< list.size(); i++) {
			if(i>0) {
				temp += ";";
			}
			
			temp+=list.get(i);
		}
		
		return temp;
	}
	
	public String getCsvFormattedPrivateInstanceVariables() {
		
		return getCsvFormattedStringArray(privateInstanceVariables);
	}
	
	public String getCsvFormattedPublicMethods() {
		
		return getCsvFormattedStringArray(publicMethods);
	}
	
	public String getFullyQualifiedName() {
		return fullyQualifiedName;
	}

	public void setFullyQualifiedName(String fullyQualifiedName) {
		this.fullyQualifiedName = fullyQualifiedName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
	}

	public List<String> getPrivateInstanceVariables() {
		return privateInstanceVariables;
	}

	public void setPrivateInstanceVariables(List<String> privateInstanceVariables) {
		this.privateInstanceVariables = privateInstanceVariables;
	}

	public List<String> getPublicMethods() {
		return publicMethods;
	}

	public void setPublicMethods(List<String> publicMethods) {
		this.publicMethods = publicMethods;
	}

	public String getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public String toString() {
		return "Fully Qualified Name=" + fullyQualifiedName + ", name=" + name + ", linesOfCode=" + linesOfCode + ", private instance variables=" + privateInstanceVariables + ", public methods=" + publicMethods + ", package=" + javaPackage;
	}
}
