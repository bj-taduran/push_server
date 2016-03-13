package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.*;
import com.mttnow.push.core.persistence.*;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import com.mttnow.push.messaging.api.PushSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageCompositionServiceImplTest {

    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    APNSChannelRepository apnsChannelRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    RecipientRepository recipientRepository;
    @Mock
    MessageRepository messageRepository;
    @Mock
    PushSender pushSender;
    @InjectMocks
    MessageCompositionServiceImpl service;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSendMessage() throws Exception, PushChannelException {
        Application application = new Application("1l");
        application.setName("Push App");
        application.setMode(ApplicationMode.DEVELOPMENT);
        ComposeMessageDTO composeMessageDTO = new ComposeMessageDTO();
        composeMessageDTO.setAppId("1l");
        composeMessageDTO.setTitle("PROMO-1");
        composeMessageDTO.setMessage("50% off on all domestic flights!");
        List<Long> recipientIds = new ArrayList<Long>();
        recipientIds.add(1l);
        List<Long> tagIds = new ArrayList<Long>();
        tagIds.add(1l);
        List<Recipient> recipients = new ArrayList<Recipient>();
        Recipient recipient = new Recipient();
        recipient.setId(1l);
        recipient.setReceiver("sadfhg");
        recipients.add(recipient);

        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = new Tag();
        tag.setId(1l);
        tag.setName("PROMO TAG");
        tags.add(tag);

        Message message = new Message();
        message.setTitle(composeMessageDTO.getTitle());
        message.setMessage(composeMessageDTO.getMessage());
        message.setRecipients(recipients);
        message.setTags(tags);

        APNSChannel apnsChannel = new APNSChannel();
        apnsChannel.setCert("/fake/path/to/cert.p12");
        apnsChannel.setId(1l);
        apnsChannel.setPassword("samplepassword");
        apnsChannel.setName("Push App Demo");
        apnsChannel.setType(Channel.Type.IOS);
        apnsChannel.setApplication(application);

        when(recipientRepository.findAllByApplication(any(Application.class))).thenReturn(recipients);
        when(recipientRepository.findAll(recipientIds)).thenReturn(recipients);
        when(tagRepository.findAllByApplication(any(Application.class))).thenReturn(tags);
        when(tagRepository.findAll(tagIds)).thenReturn(tags);
        when(applicationRepository.findOne("1l")).thenReturn(application);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(apnsChannelRepository.findByApplication(application)).thenReturn(apnsChannel);
        doNothing().when(pushSender).pushMessage(any(ApnsMessage.class));


        assertEquals("Message Successfully Sent!", service.sendMessage(composeMessageDTO));

    }

    @Test
    public void testSendMessageWithRecipients() throws Exception, PushChannelException {
        Application application = new Application("1l");
        application.setName("Push App");
        application.setMode(ApplicationMode.DEVELOPMENT);
        ComposeMessageDTO composeMessageDTO = new ComposeMessageDTO();
        composeMessageDTO.setAppId("1l");
        composeMessageDTO.setTitle("PROMO-1");
        composeMessageDTO.setMessage("50% off on all domestic flights!");
        List<Long> recipientIds = new ArrayList<Long>();
        recipientIds.add(1l);
        List<Long> tagIds = new ArrayList<Long>();
        tagIds.add(1l);
        List<Recipient> recipients = new ArrayList<Recipient>();
        Recipient recipient = new Recipient();
        recipient.setId(1l);
        recipient.setReceiver("sadfhg");
        recipients.add(recipient);
        composeMessageDTO.setRecipientIds(recipientIds);
        composeMessageDTO.setTagIds(tagIds);

        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = new Tag();
        tag.setId(1l);
        tag.setName("PROMO TAG");
        tag.setRecipients(recipients);
        tags.add(tag);

        Message message = new Message();
        message.setTitle(composeMessageDTO.getTitle());
        message.setMessage(composeMessageDTO.getMessage());
        message.setRecipients(recipients);
        message.setTags(tags);

        APNSChannel apnsChannel = new APNSChannel();
        apnsChannel.setCert("/fake/path/to/cert.p12");
        apnsChannel.setId(1l);
        apnsChannel.setPassword("samplepassword");
        apnsChannel.setName("Push App Demo");
        apnsChannel.setType(Channel.Type.IOS);
        apnsChannel.setApplication(application);

        when(recipientRepository.findAllByApplication(any(Application.class))).thenReturn(recipients);
        when(recipientRepository.findAll(recipientIds)).thenReturn(recipients);
        when(tagRepository.findAllByApplication(any(Application.class))).thenReturn(tags);
        when(tagRepository.findAll(tagIds)).thenReturn(tags);
        when(applicationRepository.findOne("1l")).thenReturn(application);
        when(messageRepository.save(any(Message.class))).thenReturn(message);
        when(apnsChannelRepository.findByApplication(application)).thenReturn(apnsChannel);
        doNothing().when(pushSender).pushMessage(any(ApnsMessage.class));

        assertEquals("Message Successfully Sent!", service.sendMessage(composeMessageDTO));

    }
    
    @Test(expected=PushChannelException.class)
    public void shouldThrowPushChannelExceptionWhenChannelIsNotConfigured() throws PushChannelException{
      Application application = new Application("1l");
      ComposeMessageDTO composeMessageDTO = new ComposeMessageDTO();
      List<Long> recipientIds = new ArrayList<Long>();
      List<Long> tagIds = new ArrayList<Long>();
      List<Recipient> recipients = new ArrayList<Recipient>();
      List<Tag> tags = new ArrayList<Tag>();
      Message message = new Message();

      when(recipientRepository.findAllByApplication(any(Application.class))).thenReturn(recipients);
      when(recipientRepository.findAll(recipientIds)).thenReturn(recipients);
      when(tagRepository.findAllByApplication(any(Application.class))).thenReturn(tags);
      when(tagRepository.findAll(tagIds)).thenReturn(tags);
      when(applicationRepository.findOne("1l")).thenReturn(application);
      when(messageRepository.save(any(Message.class))).thenReturn(message);
      when(apnsChannelRepository.findByApplication(application)).thenReturn(null);
      doNothing().when(pushSender).pushMessage(any(ApnsMessage.class));

      service.sendMessage(composeMessageDTO);
    }

    @Test
    public void shouldUpdateMessage(){
        ApnsMessage apnsMessage = new ApnsMessage();
        apnsMessage.setMessageId(1l);
        apnsMessage.setMessage("Hello World!");

        Message message = new Message();
        message.setStatus(Message.Status.SENT);

        Message currentMessage = new Message();
        message.setStatus(Message.Status.IN_PROGRESS);
        when(messageRepository.findOne(1l)).thenReturn(currentMessage);
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message result = service.updateMessage(apnsMessage, Message.Status.SENT, 0,1);

        assertEquals(message,result);
    }
}
