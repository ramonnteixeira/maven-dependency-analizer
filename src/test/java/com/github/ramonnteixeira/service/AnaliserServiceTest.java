package com.github.ramonnteixeira.service;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class AnaliserServiceTest {

    @Test
    public void validatePomWithDuplicatedClass() throws IOException {
        AnaliserService service = new AnaliserService();
        service.validatePom(new File(getClass().getResource("/pom_duplicated_class.xml").getFile()));
    }
    
}
