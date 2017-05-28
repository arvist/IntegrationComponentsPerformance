package lu.df.java.camel.marshalling.tests.marshalling;

import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class JAXBMarshalling {

    @Test
    public void testJAXBMarshalling() throws Exception{

        Utils.warmUp(); // Useless method to warm JVM up, so that each cycle would have equivalent execution conditions

        JAXBContext jc = JAXBContext.newInstance(ProjectList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File xml = new File("src/test/resources/data.xml");

        Integer processLimit = 100;
        Long unmarshallTime = new Long(0);

        for (int i = 0; i < processLimit; i++) { // Long running process
            long startTimeUnmarshall = System.currentTimeMillis();
            ProjectList projects = (ProjectList) unmarshaller.unmarshal(xml);
            long endTimeUnmarshall = System.currentTimeMillis();
            unmarshallTime += (endTimeUnmarshall - startTimeUnmarshall);
        }

        System.out.println("Average UnmarshallTime = " + (unmarshallTime.doubleValue()/processLimit) + " ms.");

    }
}
