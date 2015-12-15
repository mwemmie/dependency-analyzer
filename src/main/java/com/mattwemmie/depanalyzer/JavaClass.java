package com.mattwemmie.depanalyzer;

import java.util.List;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class JavaClass {

	@GraphId Long id;
    @Indexed(unique = true) public String fullyQualifiedName;
    public String name;
	public int linesOfCode;
	
	public List<String> privateInstanceVariables;
	public List<String> publicMethods;
	
	public JavaClass() {}
	
	public JavaClass(String fullyQualifiedName, String name, List<String> privateInstanceVariables, List<String> publicMethods) {
		this.fullyQualifiedName = fullyQualifiedName;
		this.name = name;
		this.privateInstanceVariables = privateInstanceVariables;
		this.publicMethods = publicMethods;
	}@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	public String toString() {
		return "Fully Qualified Name=" + fullyQualifiedName + ", name=" + name + ", linesOfCode=" + linesOfCode + ", private instance variables=" + privateInstanceVariables + ", public methods=" + publicMethods;
	}
}
