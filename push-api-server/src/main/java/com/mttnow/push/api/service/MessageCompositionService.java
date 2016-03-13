package com.mttnow.push.api.service;

import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.ApnsMessage;
import com.mttnow.push.api.models.ComposeMessageDTO;
import com.mttnow.push.api.models.Message;

public interface MessageCompositionService {

    String sendMessage(ComposeMessageDTO message) throws PushChannelException;
    Message updateMessage(ApnsMessage message, Message.Status status, int successCount, int failCount);
}
