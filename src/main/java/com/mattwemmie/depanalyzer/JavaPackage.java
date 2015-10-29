package com.mattwemmie.depanalyzer;

import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class JavaPackage {

	@GraphId Long id;
    @Indexed(unique = true) public String name;
    
    // not sure on this but it appears to cause infinite recursion
/*    @RelatedTo(type = "PACKAGE_DEPENDENCY", direction = Direction.INCOMING)
    @Fetch
    public Set<JavaPackage> incomingJavaPackages;*/

    @RelatedTo(type = "PACKAGE_DEPENDENCY", direction = Direction.OUTGOING)
    @Fetch
    public Set<JavaPackage> outGoingJavaPackages;
    
    public JavaPackage() {}
    public JavaPackage(String name) { this.name = name; }
    
    public PackageDependency dependsOn(JavaPackage endPackage) {
    	return new PackageDependency(this, endPackage);
    }
    
    @Override
    public String toString() {
    	return "[Package name=" + name + ", outgoing Packages=" + outGoingJavaPackages + "]";
    }
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		JavaPackage other = (JavaPackage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
    
    
}
