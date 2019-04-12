package com.bl.sje.configuration;

import com.bl.sje.jms.producer.Producer;
import com.bl.sje.jms.producer.ProducerImpl;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import javax.jms.JMSException;
import java.util.Objects;

@Configuration
@EnableJms
@RequiredArgsConstructor
public class JmsConfiguration {

    private final Environment environment;

    @Bean
    public MQQueueConnectionFactory ibmMqConnectionFactory() throws JMSException {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setQueueManager(environment.getProperty("jms.ibm.mq.queue-manager"));
        connectionFactory.setTransportType(Integer.parseInt(Objects.requireNonNull(
                environment.getProperty("jms.ibm.mq.transport-type"))));
        connectionFactory.setHostName(environment.getProperty("jms.ibm.mq.host"));
        connectionFactory.setPort(Integer.parseInt(Objects.requireNonNull(
                environment.getProperty("jms.ibm.mq.port"))));
        connectionFactory.setChannel(environment.getProperty("jms.ibm.mq.channel"));
        return connectionFactory;
    }

    @Bean
    public UserCredentialsConnectionFactoryAdapter connectionFactoryAdapter(
            MQQueueConnectionFactory ibmMqConnectionFactory) {
        UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
        adapter.setTargetConnectionFactory(ibmMqConnectionFactory);
        adapter.setUsername(environment.getProperty("jms.ibm.mq.user"));
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

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(UserCredentialsConnectionFactoryAdapter connectionFactory,
                                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
