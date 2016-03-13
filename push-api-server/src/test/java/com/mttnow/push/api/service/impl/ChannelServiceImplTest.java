package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.GCMChannel;
import com.mttnow.push.core.persistence.APNSChannelRepository;
import com.mttnow.push.core.persistence.GCMChannelRepository;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceImplTest {
    @Mock
    APNSChannelRepository apnsChannelRepository;
    @Mock
    GCMChannelRepository gcmChannelRepository;
    @InjectMocks
    ChannelServiceImpl service;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testFindByApplicationId() throws Exception {

        //Given
        Application application = new Application("1l");
        application.setName("Push App");
        application.setMode(ApplicationMode.DEVELOPMENT);
        APNSChannel apnsChannel = new APNSChannel();
        apnsChannel.setCert("/fake/path/to/cert.p12");
        apnsChannel.setId(1l);
        apnsChannel.setPassword("samplepassword");
        apnsChannel.setName("Push App Demo");
        apnsChannel.setType(Channel.Type.IOS);
        apnsChannel.setApplication(application);

        when(apnsChannelRepository.findByApplication(any(Application.class))).thenReturn(apnsChannel);
        GCMChannel gcmChannel = new GCMChannel();
        gcmChannel.setType(Channel.Type.ANDROID);
        gcmChannel.setName("Push Android");
        gcmChannel.setPassword("password");
        gcmChannel.setGcmKey("alksflkajshfd12i9u48912u4askdfjla=-");
        gcmChannel.setId(2l);
        gcmChannel.setApplication(application);
        when(gcmChannelRepository.findByApplication(any(Application.class))).thenReturn(gcmChannel);

        List<Channel> expected = new ArrayList<Channel>();
        expected.add(apnsChannel);
        expected.add(gcmChannel);
        //When
        List<Channel> actual = service.findByApplicationId("1l");

        //Then
        assertEquals(expected,actual);

    }

    @Test
    public void testSaveApnsChannel() throws Exception {
        //Given
        Application application = new Application("1l");
        application.setName("Push App");
        application.setMode(ApplicationMode.DEVELOPMENT);
        APNSChannel apnsChannel = new APNSChannel();
        apnsChannel.setCert("/fake/path/to/cert.p12");
        apnsChannel.setId(1l);
        apnsChannel.setPassword("samplepassword");
        apnsChannel.setName("Push App Demo");
        apnsChannel.setType(Channel.Type.IOS);
        apnsChannel.setApplication(application);
        when(apnsChannelRepository.save(any(APNSChannel.class))).thenReturn(apnsChannel);
        //When
        APNSChannel actual = service.saveApnsChannel(apnsChannel);
        //Then
        assertEquals(apnsChannel,actual);

    }
}
