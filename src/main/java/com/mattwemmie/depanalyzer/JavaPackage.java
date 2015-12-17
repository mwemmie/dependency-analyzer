package com.mattwemmie.depanalyzer;

import java.util.Set;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class JavaPackage {

	private String name;
    
    //@RelatedTo(type = "PACKAGE_DEPENDENCY", direction = Direction.OUTGOING)
    private Set<JavaPackage> outGoingJavaPackages;
    
    public JavaPackage(String name) { 
    	this.name = name; 
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<JavaPackage> getOutGoingJavaPackages() {
		return outGoingJavaPackages;
	}
	public void setOutGoingJavaPackages(Set<JavaPackage> outGoingJavaPackages) {
		this.outGoingJavaPackages = outGoingJavaPackages;
	}
}
