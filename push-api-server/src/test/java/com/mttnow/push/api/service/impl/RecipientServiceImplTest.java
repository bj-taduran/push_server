package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.RecipientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipientServiceImplTest {


    @Mock
    RecipientRepository recipientRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @InjectMocks
    RecipientServiceImpl service;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSaveRecipient() throws Exception {
        //Given

        String receiver = "asdfghjklqwertyuiop1234567890";
        Channel.Type type = Channel.Type.IOS;
        String appId = "asfd-asdhg-1234-sadgf-21345";
        Application application = new Application();
        application.setId(appId);

        Recipient expected = new Recipient();
        expected.setId(1l);
        expected.setApplication(application);
        expected.setChannelType(type);
        expected.setReceiver(receiver);

        when(applicationRepository.findOne(appId)).thenReturn(application);
        when(recipientRepository.save(any(Recipient.class))).thenReturn(expected);
        //When

        Recipient result = service.saveRecipient(receiver, type, appId);
        //Then

        assertEquals(expected,result);
        verify(recipientRepository).save(any(Recipient.class));

    }

    @Test
    public void testDeleteRecipient() throws Exception {

        //Given

        String receiver = "asdfghjklqwertyuiop1234567890";
        Channel.Type type = Channel.Type.IOS;
        String appId = "asfd-asdhg-1234-sadgf-21345";

        Application application = new Application();
        application.setId(appId);

        Recipient recipient = new Recipient();
        recipient.setId(1l);
        recipient.setApplication(application);
        recipient.setChannelType(type);
        recipient.setReceiver(receiver);

        when(applicationRepository.findOne(appId)).thenReturn(application);
        when(recipientRepository.findByReceiverAndChannelTypeAndApplication(receiver,type,application)).thenReturn(recipient);

        //When
        service.deleteRecipient(receiver,type,appId);
        //Then

        verify(recipientRepository).delete(recipient);
    }
}
