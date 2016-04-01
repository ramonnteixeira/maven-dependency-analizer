package com.github.ramonnteixeira.service;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

import com.github.ramonnteixeira.exception.DuplicatedClassException;

public class AnaliserServiceTest {

    @Test(expected=DuplicatedClassException.class)
    public void validatePomWithDuplicatedClass() throws IOException {
        AnaliserService service = new AnaliserService();
        service.validatePom(new File(getClass().getResource("/pom_duplicated_class.xml").getFile()));
    }

    @Test
    public void validatePomWithoutDuplicatedClass() throws IOException {
        AnaliserService service = new AnaliserService();
        service.validatePom(new File(getClass().getResource("/pom_without_duplicates.xml").getFile()));
    }
    
}