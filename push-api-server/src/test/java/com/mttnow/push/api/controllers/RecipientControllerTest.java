package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;
import com.mttnow.push.api.service.RecipientService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipientControllerTest {
    @Mock
    RecipientService recipientService;
    RecipientController controller;
    @Before
    public void setUp() throws Exception {
        controller = new RecipientController();
        controller.recipientService = recipientService;

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSaveRecipient() throws Exception {
        //Given
        String receiver = "21345769asdfghj";
        Channel.Type type = Channel.Type.IOS;
        String appId = "1l";
        Recipient expected = new Recipient();
        expected.setChannelType(type);
        expected.setId(1l);
        Application app = new Application();
        app.setId("1l");
        expected.setApplication(app);
        when(recipientService.saveRecipient(receiver,  type, appId)).thenReturn(expected);
        //When
        Recipient actual = controller.saveRecipient(receiver, type, appId);
        //Then
        assertEquals(expected,actual);


    }

    @Test
    public void testDeleteRecipient(){
        //Given
        String receiver = "21345769asdfghj";
        Channel.Type type = Channel.Type.IOS;
        String appId = "1l";
        doNothing().when(recipientService).deleteRecipient(receiver, type, appId);
        //When
        controller.delete(appId, receiver, type);
        //Then
        verify(recipientService).deleteRecipient(receiver, type, appId);
    }
}
