package com.mattwemmie.depanalyzer;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type = "PACKAGE_DEPENDENCY")
public class PackageDependency {

	@Fetch @StartNode private String startPackage;
	@Fetch @EndNode private String endPackage;
	
	public PackageDependency(String startPackage, String endPackage) {
		this.startPackage = startPackage;
		this.endPackage = endPackage;
	}
	
	public String getStartPackage() {
		return startPackage;
	}

	public void setStartPackage(String startPackage) {
		this.startPackage = startPackage;
	}

	public String getEndPackage() {
		return endPackage;
	}

	public void setEndPackage(String endPackage) {
		this.endPackage = endPackage;
	}



	@Override
	public String toString() {
		return startPackage + " -> " + endPackage;
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
}
