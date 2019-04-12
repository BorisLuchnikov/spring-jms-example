package com.bl.sje.jms.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

@Slf4j
@RequiredArgsConstructor
public class ProducerImpl implements Producer {

    private final JmsTemplate jmsTemplate;

    public void sendTo(Destination queue, String message) {
        log.trace("jms send message: {}", message);
        jmsTemplate.send(queue, session -> session.createTextMessage(message));
    }

}
