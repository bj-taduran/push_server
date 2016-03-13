package com.mttnow.push.messaging.api;

import com.mttnow.push.api.models.ApnsMessage;

public interface PushSender {

    void pushMessage(ApnsMessage apnsMessage);
}
