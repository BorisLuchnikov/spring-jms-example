package com.bl.sje;

import com.bl.sje.jms.Producer;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.wmq.WMQConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;


@RunWith(SpringRunner.class)
public class ProducerTest extends AbstractIbmMqSqlTest {

    @Autowired
    private Producer producer;

    @Test
    public void test() throws JMSException {
        MQQueue queue = new MQQueue("queue");
        queue.setBooleanProperty(WMQConstants.WMQ_MQMD_WRITE_ENABLED, true);
        producer.sendTo(queue, "my message");
    }
}
