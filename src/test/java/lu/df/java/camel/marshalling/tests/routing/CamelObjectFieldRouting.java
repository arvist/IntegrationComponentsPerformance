package lu.df.java.camel.marshalling.tests.routing;


import lu.df.java.camel.marshalling.model.xml.jaxb.ProjectList;
import lu.df.java.camel.marshalling.utils.Utils;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class CamelObjectFieldRouting extends CamelTestSupport {

    @Test
    public void testObjectFieldRouting() throws Exception {
        File xml = new File("src/test/resources/data_consolidated.xml");
        JAXBContext jc = JAXBContext.newInstance(ProjectList.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        ProjectList projects = (ProjectList) unmarshaller.unmarshal(xml);
        Utils.warmUp();
        Integer routeLimit = 100;

        template.setDefaultEndpointUri("direct:start");
        getMockEndpoint("mock:end").reset();
        getMockEndpoint("mock:end").setExpectedCount(routeLimit);

        Long routeTime = new Long(0);
        for (int i = 0; i < routeLimit; i++) {
            long startTime = System.currentTimeMillis();
            template.sendBody(projects);
            long endTime = System.currentTimeMillis();
            routeTime += (endTime - startTime);
        }
        assertMockEndpointsSatisfied();
        System.out.println("Average Routing Time = " + (routeTime.doubleValue() / routeLimit) + " ms.");

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .choice()
                        .when((Exchange exchange) -> exchange.getIn().getBody(ProjectList.class).getProject().get(3).getNotes().get(0).getHeading().equals(("str1234")))
                        .to("mock:end")
                        .end();
            }
        };
    }
}