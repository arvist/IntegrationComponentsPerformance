package lu.df.java.camel.marshalling.tests.marshalling;

import com.fasterxml.jackson.databind.ObjectMapper;
import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.junit.Test;

import java.io.File;

public class JacksonMarshalling {

    @Test
    public void testJacksonMarshalling() throws Exception{

        Utils.warmUp(); // Useless method to warm JVM up, so that each cycle would have equivalent execution conditions


        File xml = new File("src/test/resources/data.json");
        ObjectMapper mapper = new ObjectMapper();

        Integer processLimit = 100;
        Long unmarshallTime = new Long(0);

        for (int i = 0; i < processLimit; i++) { // Long running process
            long startTimeUnmarshall = System.currentTimeMillis();
            ProjectList projects = (ProjectList) mapper.readValue(xml, ProjectList.class);
            long endTimeUnmarshall = System.currentTimeMillis();
            unmarshallTime += (endTimeUnmarshall - startTimeUnmarshall);
        }

        System.out.println("Average UnmarshallTime = " + (unmarshallTime.doubleValue()/processLimit) + " ms.");

    }
}