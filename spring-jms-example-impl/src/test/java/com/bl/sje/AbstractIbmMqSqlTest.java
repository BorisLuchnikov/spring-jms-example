package com.bl.sje;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Boris_Luchnikov
 *
 * Contatiner of testcontainers.
 */
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = AbstractIbmMqSqlTest.Initializer.class)
public abstract class AbstractIbmMqSqlTest {

    private static GenericContainer ibmMq = new GenericContainer("ibmcom/mq")
            .withExposedPorts(1414)
            .withExposedPorts(9443)
            .withEnv("LICENSE", "accept")
            .withEnv("MQ_QMGR_NAME", "QM1");


    static {
        ibmMq.start();
    }

    /**
     * Programmatic initialization of the <code>application context</code>.
     */
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        private static final String IBM_MQ_HOST = ibmMq.getContainerIpAddress();
        private static final Integer IBM_MQ_PORT = ibmMq.getFirstMappedPort();

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(springApplicationProperties());
        }

        private static List<String> springApplicationProperties() {
            List<String> properties = new ArrayList<>();
            properties.add("jms.ibm.mq.host=" + IBM_MQ_HOST);
            properties.add("jms.ibm.mq.port=" + IBM_MQ_PORT);

            return properties;
        }
    }
}
