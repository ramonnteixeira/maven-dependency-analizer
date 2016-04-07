package com.github.ramonnteixeira.dto;


public class DependencyException {

    private String dependency1;
    private String dependency2;
    
    public DependencyException(String dependency1, String dependency2) {
        this.dependency1 = dependency1;
        this.dependency2 = dependency2;
    }

    public String getDependency1() {
        return dependency1;
    }
    
    public void setDependency1(String dependency1) {
        this.dependency1 = dependency1;
    }
    
    public String getDependency2() {
        return dependency2;
    }
    
    public void setDependency2(String dependency2) {
        this.dependency2 = dependency2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dependency1 == null) ? 0 : dependency1.hashCode());
        result = prime * result + ((dependency2 == null) ? 0 : dependency2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass() || dependency1 == null || dependency2 == null) {
            return false;
        }
        
        DependencyException other = (DependencyException) obj;
        return (dependency1.equals(other.dependency1) && dependency2.equals(other.dependency2)) || (dependency1.equals(other.dependency2) && dependency2.equals(other.dependency1));
    }

    @Override
    public String toString() {
        return String.format("%s;%s", dependency1, dependency2);
    }
}
