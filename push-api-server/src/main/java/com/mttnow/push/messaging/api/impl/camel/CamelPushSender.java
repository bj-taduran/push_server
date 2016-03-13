package com.mttnow.push.messaging.api.impl.camel;

import com.mttnow.push.api.models.ApnsMessage;
import com.mttnow.push.messaging.api.PushSender;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CamelPushSender implements PushSender {
    @Autowired
    ProducerTemplate camelTemplate;
    @Override
    public void pushMessage(ApnsMessage apnsMessage) {
        camelTemplate.sendBody("jms:queue:apns", apnsMessage);
    }
}
