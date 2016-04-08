package com.github.ramonnteixeira.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import com.github.ramonnteixeira.dto.DependencyException;
import com.github.ramonnteixeira.exception.DuplicatedClassException;

@Mojo(name="duplicated-class", defaultPhase=LifecyclePhase.COMPILE, requiresDependencyResolution=ResolutionScope.RUNTIME)
public class DuplicatedClassAnaliser extends AbstractMojo {

    private Map<String, String> classNames;
    private StringBuilder errors;

    @Parameter
    private List<DependencyException> exceptions;
    
    @Parameter(defaultValue="${project}", required=true, readonly=true)
    private MavenProject project;
    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (exceptions == null) {
            exceptions = new ArrayList<>();
        }

        try {
            getLog().info("Starting process execution");
            validatePom();
            getLog().info("End process execution");
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }
    
    private void validatePom() throws IOException {
        errors = new StringBuilder();
        classNames = new HashMap<>();
        for (Artifact mvnDep : project.getArtifacts()) {
            if ( !(mvnDep.getScope().equalsIgnoreCase("runtime") || mvnDep.getScope().equalsIgnoreCase("compile")) ) {
                continue;
            }

            File dependency = mvnDep.getFile();
            getLog().info(String.format("Verifing '%s'", dependency.getName()));
            try (ZipInputStream zip = new ZipInputStream(new FileInputStream(dependency))) {
                    verifyDependencyEntry(dependency, zip);
            }
        }
        
        if (errors.length() > 0) {
            throw new DuplicatedClassException(errors.toString());
        }
    }

    private void verifyDependencyEntry(File dependency, ZipInputStream zip) throws IOException {
        for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
            if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.');
                className = className.substring(0, className.length() - ".class".length());
                
                String oldDependency = classNames.get(className);
                if (oldDependency != null && !exceptions.contains(new DependencyException(oldDependency, dependency.getName()))) {
                    String conflictMessage = String.format("\n%s conflict with %s.", dependency.getName(), oldDependency);
                    
                    if (errors.indexOf(conflictMessage) < 0) {
                        errors.append(conflictMessage);
                    }

                    getLog().debug(String.format("[ERROR] Class %s of %s already exists in %s.", className, dependency.getName(), oldDependency));
                }
                
                classNames.put(className, dependency.getName());
            }
        }
    }

}
