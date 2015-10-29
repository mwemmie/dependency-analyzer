package com.mattwemmie.depanalyzer;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class JavaClass {

	@GraphId Long id;
    @Indexed(unique = true) public String name;
	public int linesOfCode;
	
	public JavaClass() {}
	
	public JavaClass(String name) {
		this.name = name;
	}
}
