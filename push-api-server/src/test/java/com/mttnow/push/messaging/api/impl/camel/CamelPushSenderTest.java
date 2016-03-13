package com.mttnow.push.messaging.api.impl.camel;

import com.mttnow.push.api.models.ApnsMessage;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

@RunWith(MockitoJUnitRunner.class)
public class CamelPushSenderTest {
    @Mock
    ProducerTemplate producerTemplate;
    @InjectMocks
    CamelPushSender camelPushSender;
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testPushMessage() throws Exception {

        doNothing().when(producerTemplate).sendBody(anyString(), anyObject());
        camelPushSender.pushMessage(new ApnsMessage());
    }
}
