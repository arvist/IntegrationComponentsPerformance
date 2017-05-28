package lu.df.java.camel.marshalling.tests.routing;


import lu.df.java.camel.marshalling.utils.Utils;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

public class CamelHeaderRouting extends CamelTestSupport {

    @Test
    public void testHeaderRouting() throws Exception {
        File xml = new File("src/test/resources/data_consolidated.xml");
        String xmlContents = FileUtils.readFileToString(xml);
        Utils.warmUp();
        Integer routeLimit = 100;

        template.setDefaultEndpointUri("direct:start");
        getMockEndpoint("mock:end").reset();

        Long routeTime = new Long(0);
        for (int i = 0; i < routeLimit; i++) {
            String fileType;

            if(i % 2 == 0) fileType = "XML";
                    else fileType = "JSON";

            long startTime = System.currentTimeMillis();
            template.sendBodyAndHeader(xmlContents, "FILE_TYPE", fileType);
            long endTime = System.currentTimeMillis();
            routeTime += (endTime - startTime);
        }
        System.out.println("Average Routing Time = " + (routeTime.doubleValue()/routeLimit) + " ms.");

    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .choice()
                        .when(header("FILE_TYPE").isEqualTo("XML"))
                        .to("mock:end")
                        .end();
            }
        };
    }
}


