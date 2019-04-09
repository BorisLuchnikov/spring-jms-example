package com.bl.sje.configuration;

import com.bl.sje.jms.Producer;
import com.bl.sje.jms.ProducerImpl;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import java.util.Objects;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class JmsConfiguration {

    private final Environment environment;

    @Bean
    public MQQueueConnectionFactory ibmMqConnectionFactory() {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        try {
            connectionFactory.setQueueManager(environment.getProperty("jms.ibm.mq.queue-manager"));
            connectionFactory.setTransportType(Integer.parseInt(Objects.requireNonNull(
                    environment.getProperty("jms.ibm.mq.transport-type"))));
            connectionFactory.setHostName(environment.getProperty("jms.ibm.mq.host"));
            connectionFactory.setPort(Integer.parseInt(Objects.requireNonNull(
                    environment.getProperty("jms.ibm.mq.port"))));
            connectionFactory.setChannel(environment.getProperty("jms.ibm.mq.channel"));
        } catch (JMSException e) {
            throw new JmsRuntimeException("Error creating bean MQQueueConnectionFactory.", e);
        }
        return connectionFactory;
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter(
            MQQueueConnectionFactory ibmMqConnectionFactory) {
        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        adapter.setTargetConnectionFactory(ibmMqConnectionFactory);
        adapter.setUsername(environment.getProperty("jms.ibm.mq.username"));
        adapter.setPassword(environment.getProperty("jms.ibm.mq.password"));
        return adapter;
    }

    @Bean
    public JmsTemplate jmsTemplate(UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactoryAdapter);
        return jmsTemplate;
    }

    @Bean
    public Producer jmsProducer(JmsTemplate jmsTemplate) {
        return new ProducerImpl(jmsTemplate);
    }

    private static class JmsRuntimeException extends RuntimeException {
        public JmsRuntimeException(String s, Throwable t) {
            super(s, t);
        }
    }

}
