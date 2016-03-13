package com.mttnow.push.api.service.impl;

import com.google.common.collect.Lists;
import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.*;
import com.mttnow.push.api.service.MessageCompositionService;
import com.mttnow.push.core.persistence.*;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import com.mttnow.push.messaging.api.PushSender;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageCompositionServiceImpl implements MessageCompositionService {
    private static Logger log = LoggerFactory.getLogger(MessageCompositionServiceImpl.class);
    @Autowired
    ApplicationRepository applicationRepository;
    @Autowired
    APNSChannelRepository apnsChannelRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    RecipientRepository recipientRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    PushSender pushSender;


    @Override
    public String sendMessage(ComposeMessageDTO message) throws PushChannelException {
        Message msg = new Message();
        msg.setTitle(message.getTitle());
        msg.setMessage(message.getMessage());
        if((message.getRecipientIds() == null || message.getRecipientIds().isEmpty()) &&
                (message.getTagIds() == null || message.getTagIds().isEmpty())){
            msg.setRecipients(Lists.newArrayList(recipientRepository.findAllByApplication(new Application(message.getAppId()))));
        } else{
            msg.setRecipients(Lists.newArrayList(recipientRepository.findAll(message.getRecipientIds())));
            msg.setTags(Lists.newArrayList(tagRepository.findAll(message.getTagIds())));
        }
        msg.setApplication(applicationRepository.findOne(message.getAppId()));
        msg.setChannelTypes(message.getChannelTypes());
        msg.setStatus(Message.Status.IN_PROGRESS);
        Message savedMessage = messageRepository.save(msg);
        log.info("saved message: {}", savedMessage);
        List<String> deviceTokens = new ArrayList<String>();
        for (Recipient recipient : msg.getRecipients()) {
            deviceTokens.add(recipient.getReceiver());
        }
        if(msg.getTags() != null){
        for (Tag tag : msg.getTags()) {
            for (Recipient recipient : tag.getRecipients()) {
                deviceTokens.add(recipient.getReceiver());
            }
            //TODO separate to a recursive call to get the deviceTokens
        }
        }

        APNSChannel byApplication = apnsChannelRepository.findByApplication(msg.getApplication());
        if(byApplication==null){
          throw new PushChannelException("No channels configured for this application!");
        }
        ApnsMessage apnsMessage = new ApnsMessage();
        apnsMessage.setCertificate(byApplication.getCert());
        apnsMessage.setPassword(byApplication.getPassword());
        apnsMessage.setProduction(msg.getApplication().getMode() == ApplicationMode.PRODUCTION);
        apnsMessage.setMessage(message.getMessage());
        apnsMessage.setDeviceTokens(deviceTokens);
        apnsMessage.setMessageId(savedMessage.getId());
        pushSender.pushMessage(apnsMessage);
        return "Message Successfully Sent!";
    }

    @Override
    public Message updateMessage(ApnsMessage message, Message.Status status, int successCount, int failCount) {
        Message msg = messageRepository.findOne(message.getMessageId());
        msg.setStatus(status);
        msg.setSuccessCount(successCount);
        msg.setFailCount(failCount);
        if(status == Message.Status.SENT){
            msg.setDateSent(new DateTime());
        }
        return messageRepository.save(msg);

    }
}
