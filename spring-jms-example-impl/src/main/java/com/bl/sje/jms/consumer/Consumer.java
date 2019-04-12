package com.bl.sje.jms.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Slf4j
@Component
public class Consumer {

    @JmsListener(containerFactory = "jmsListenerContainerFactory", destination = "${jms.consumer.example.queue}",
            concurrency = "${jms.consumer.example.concurrency}")
    public void onMessage(Message jmsMessage) {
        log.info(jmsMessage.toString());
    }
}
