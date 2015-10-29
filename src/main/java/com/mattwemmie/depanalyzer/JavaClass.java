package com.mattwemmie.depanalyzer;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class JavaClass {

	private int linesOfCode;
}
