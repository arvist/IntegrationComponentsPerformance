package lu.df.java.camel.marshalling.tests.marshalling;


import com.google.gson.Gson;
import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.junit.Test;

import java.io.FileReader;

public class GsonMarshalling {

    @Test
    public void testJacksonMarshalling() throws Exception{

        Utils.warmUp(); // Useless method to warm JVM up, so that each cycle would have equivalent execution conditions

        Gson gson = new Gson();
        Integer processLimit = 100;
        Long unmarshallTime = new Long(0);

        for (int i = 0; i < processLimit; i++) { // Long running process
            long startTimeUnmarshall = System.currentTimeMillis();
            ProjectList projects = (ProjectList) gson.fromJson(new FileReader("src/test/resources/data.json"), ProjectList.class);
            long endTimeUnmarshall = System.currentTimeMillis();
            unmarshallTime += (endTimeUnmarshall - startTimeUnmarshall);
        }

        System.out.println("Average UnmarshallTime = " + (unmarshallTime.doubleValue()/processLimit) + " ms.");

    }
}
