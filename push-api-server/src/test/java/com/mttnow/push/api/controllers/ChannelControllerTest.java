package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.service.ChannelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelControllerTest {

    @InjectMocks
    ChannelController controller;
    @Mock
    ChannelService service;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetChannels() throws Exception {
        //Given
        List<Channel> expected = new ArrayList<Channel>();
        expected.add(new APNSChannel());
        when(service.findByApplicationId(anyString())).thenReturn(expected);
        //When
        List<Channel> actual= controller.getChannels("1l");
        //Then
        assertTrue(expected.equals(actual));
    }
}
