package lu.df.java.camel.marshalling.tests.split;

import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class CamelClassSplit extends CamelTestSupport {

    @Test
    public void testClassSplit() throws Exception {
        File xml = new File("src/test/resources/data_consolidated.xml");

        JAXBContext jc = JAXBContext.newInstance(ProjectList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ProjectList projects = (ProjectList) unmarshaller.unmarshal(xml);

        Utils.warmUp();
        Integer routeLimit = 1000;

        template.setDefaultEndpointUri("direct:start");

        Long routeTime = new Long(0);
        for (int i = 0; i < routeLimit; i++) {
            getMockEndpoint("mock:end").reset();
            getMockEndpoint("mock:end").setExpectedCount(5);
            long startTime = System.currentTimeMillis();
            template.sendBody(projects.getProject());
            long endTime = System.currentTimeMillis();
            assertMockEndpointsSatisfied();
            routeTime += (endTime - startTime);
        }
        System.out.println("Average Routing Time = " + (routeTime.doubleValue() / routeLimit) + " ms.");

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .split().body()
                        .to("mock:end")
                        .end();
            }
        };
    }
}
