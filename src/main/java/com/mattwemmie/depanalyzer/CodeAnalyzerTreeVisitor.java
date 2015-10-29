package com.mattwemmie.depanalyzer;

import com.sun.source.tree.ClassTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

public class CodeAnalyzerTreeVisitor extends TreePathScanner<Object, Trees>  {

	@Override
	public Object visitClass(ClassTree classTree, Trees trees) {
		
		System.out.println("in visitClass");
		System.out.println(classTree.getClass());
		
		return super.visitClass(classTree, trees);
	}
}
