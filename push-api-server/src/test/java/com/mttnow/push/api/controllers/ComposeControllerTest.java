package com.mttnow.push.api.controllers;

import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.ComposeMessageDTO;
import com.mttnow.push.api.service.MessageCompositionService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ComposeControllerTest {

    private ComposeController controller;
    private MessageCompositionService service;

    @Before
    public void before(){
       controller = new ComposeController();
       service = mock(MessageCompositionService.class);
       controller.setMessageCompositionService(service);
    }
    @Test
    public void testSendMessage() throws PushChannelException {
        //Given
        String expected = "Message Successfully Sent!";
        ComposeMessageDTO newComposeMsg = new ComposeMessageDTO();
        when(service.sendMessage(any(ComposeMessageDTO.class))).thenReturn(expected);
        //When
        String actual = controller.sendMessage(newComposeMsg, "");
        //Then
        assertEquals(expected, actual);
        verify(service, times(1)).sendMessage(newComposeMsg);
    }

    @Test
    public void testAppIdIsSet() throws PushChannelException {
        //Given
        ComposeMessageDTO newComposeMsg = new ComposeMessageDTO();
        String override_me = "override me";
        newComposeMsg.setAppId(override_me);
        String expectedAppId = "expected-app-id";

        //When
        controller.sendMessage(newComposeMsg, expectedAppId);

        //Then
        assertEquals(newComposeMsg.getAppId(), expectedAppId);
        verify(service, times(1)).sendMessage(newComposeMsg);
        assertThat(newComposeMsg.getAppId(), not(equalTo(override_me)));
    }
}
