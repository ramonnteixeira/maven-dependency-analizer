package com.github.ramonnteixeira.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

public class AnaliserService {

    public void validatePom(File pom) throws IOException {
        Map<String, String> classNames = new HashMap<>();
        
        File[] dependencies = Maven.resolver().loadPomFromFile(pom).importRuntimeDependencies().resolve().withTransitivity().asFile();
        for (File dependency : dependencies) {
            try (ZipInputStream zip = new ZipInputStream(new FileInputStream(dependency))) {
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace('/', '.');
                        className = className.substring(0, className.length() - ".class".length());
                        
                        String oldDependency = classNames.get(className);
                        if (oldDependency != null) {
                            throw new RuntimeException(String.format("Class %s of %s already exists in %s", classNames, dependency.getName(), oldDependency));
                        }
                        
                        classNames.put(className, dependency.getName());
                    }
                }
            }
        }
    }
    
}
