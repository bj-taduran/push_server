package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;
import com.mttnow.push.api.service.RecipientService;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipientServiceImpl implements RecipientService{

    @Autowired
    RecipientRepository recipientRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public Recipient saveRecipient(String receiver, Channel.Type type, String appId) {
        Recipient recipient = new Recipient();
        recipient.setReceiver(receiver);
        recipient.setChannelType(type);
        recipient.setApplication(applicationRepository.findOne(appId));
        return recipientRepository.save(recipient);
    }

    @Override
    public void deleteRecipient(String receiver, Channel.Type type, String appId) {
        Recipient recipient = recipientRepository.findByReceiverAndChannelTypeAndApplication(receiver, type, applicationRepository.findOne(appId));
        if(recipient != null){
            recipientRepository.delete(recipient);
        }
    }
}
