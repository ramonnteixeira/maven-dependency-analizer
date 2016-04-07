package com.github.ramonnteixeira.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import com.github.ramonnteixeira.dto.DependencyException;
import com.github.ramonnteixeira.exception.DuplicatedClassException;

public class AnaliserService {

    private static final Logger LOG = Logger.getLogger(AnaliserService.class.getSimpleName());
    private Map<String, String> classNames;
    
    public void validatePom(File pom, final Collection<DependencyException> exceptions) throws IOException {
        classNames = new HashMap<>();

        File[] dependencies = Maven.resolver().loadPomFromFile(pom).importCompileAndRuntimeDependencies().resolve().withTransitivity().asFile();
        for (File dependency : dependencies) {
            LOG.info(String.format("Verifing '%s'", dependency.getName()));
            try (ZipInputStream zip = new ZipInputStream(new FileInputStream(dependency))) {
                for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                    verifyDependencyEntry(dependency, entry, exceptions);
                }
            }
        }
    }

    private void verifyDependencyEntry(File dependency, ZipEntry entry, Collection<DependencyException> exceptions) {
        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
            String className = entry.getName().replace('/', '.');
            className = className.substring(0, className.length() - ".class".length());
            
            String oldDependency = classNames.get(className);
            if (oldDependency != null && !exceptions.contains(new DependencyException(oldDependency, dependency.getName()))) {
                throw new DuplicatedClassException(String.format("Class %s of %s already exists in %s", className, dependency.getName(), oldDependency));
            }
            
            classNames.put(className, dependency.getName());
//            LOG.info(String.format("----> %s\n", className));
        }
    }

}
