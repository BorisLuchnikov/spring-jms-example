package com.bl.sje.jms;

import javax.jms.Destination;

public interface Producer {

    void sendTo(Destination queue, String message);

}
