package com.mattwemmie.depanalyzer;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "PACKAGE_DEPENDENCY")
public class PackageDependency {
	@GraphId Long id;
	@Fetch @StartNode JavaPackage startPackage;
	@Fetch @EndNode JavaPackage endPackage;
	
	public PackageDependency() {}
	
	public PackageDependency(JavaPackage startPackage, JavaPackage endPackage) {
		this.startPackage = startPackage;
		this.endPackage = endPackage;
	}
	
	@Override
	public String toString() {
		return startPackage.name + " -> " + endPackage.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endPackage == null) ? 0 : endPackage.hashCode());
		result = prime * result + ((startPackage == null) ? 0 : startPackage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PackageDependency other = (PackageDependency) obj;
		if (endPackage == null) {
			if (other.endPackage != null)
				return false;
		} else if (!endPackage.equals(other.endPackage))
			return false;
		if (startPackage == null) {
			if (other.startPackage != null)
				return false;
		} else if (!startPackage.equals(other.startPackage))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public JavaPackage getStartPackage() {
		return startPackage;
	}

	public JavaPackage getEndPackage() {
		return endPackage;
	}
	
	
}
