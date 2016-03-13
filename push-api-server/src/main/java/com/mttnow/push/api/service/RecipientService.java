package com.mttnow.push.api.service;

import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;

public interface RecipientService {
    Recipient saveRecipient(String receiver, Channel.Type type, String appId);
    void deleteRecipient(String receiver, Channel.Type type, String appId);
}
