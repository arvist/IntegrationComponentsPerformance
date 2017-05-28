package lu.df.java.camel.marshalling.tests.split;

import lu.df.java.camel.marshalling.utils.Utils;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class CamelTokenizeSplit  extends CamelTestSupport {

    @Test
    public void testTokenizeSplit() throws Exception {
        File xml = new File("src/test/resources/data_consolidated.xml");
        String xmlContents = FileUtils.readFileToString(xml);
        Utils.warmUp();
        Integer routeLimit = 1000;

        template.setDefaultEndpointUri("direct:start");

        Long routeTime = new Long(0);
        for (int i = 0; i < routeLimit; i++) {
            getMockEndpoint("mock:end").reset();
            getMockEndpoint("mock:end").setExpectedCount(5);
            long startTime = System.currentTimeMillis();
            template.sendBody(xmlContents);
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
                        .split().tokenizeXML("project")
                        .to("mock:end")
                        .end();
            }
        };
    }
}
