package lu.df.java.camel.marshalling.tests.marshalling;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import lu.df.java.camel.marshalling.model.xml.jaxb.Note;
import lu.df.java.camel.marshalling.model.xml.jaxb.Person;
import lu.df.java.camel.marshalling.model.xml.jaxb.Project;
import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class XStreamMarshalling {

    @Test
    public void testXStreamMarshalling() throws Exception{

        Utils.warmUp(); // Useless method to warm JVM up, so that each cycle would have equivalent execution conditions

        XStream xstream = new XStream(new StaxDriver()); // does not require XPP3 library starting with Java 6
        xstream.alias("ProjectList",ProjectList.class);
        xstream.alias("note", Note.class);
        xstream.alias("person", Person.class);
        xstream.alias("title",String.class);
        xstream.alias("inCharge",Person.class);
        xstream.alias("to",String.class);
        xstream.alias("from",String.class);
        xstream.alias("heading",String.class);
        xstream.alias("body",String.class);
        xstream.addImplicitCollection(Project.class,"notes",Note.class);
        xstream.addImplicitCollection(ProjectList.class,"project",Project.class);

        File xml = new File("src/test/resources/data.xml");
        String xmlContents = FileUtils.readFileToString(xml);


        Integer processLimit = 100;
        Long unmarshallTime = new Long(0);

        for (int i = 0; i < processLimit; i++) { // Long running process
            System.out.println(i);
            long startTimeUnmarshall = System.currentTimeMillis();
            ProjectList projects = (ProjectList) xstream.fromXML(xmlContents);
            long endTimeUnmarshall = System.currentTimeMillis();
            unmarshallTime += (endTimeUnmarshall - startTimeUnmarshall);
        }

        System.out.println("Average UnmarshallTime = " + (unmarshallTime.doubleValue()/processLimit) + " ms.");

    }
}
