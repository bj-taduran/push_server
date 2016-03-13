package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.*;
import com.mttnow.push.core.persistence.APNSChannelRepository;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.RecipientRepository;
import com.mttnow.push.core.persistence.TagRepository;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceImplTest {
    @Mock
    TagRepository tagRepository;
    @Mock
    RecipientRepository recipientRepository;
    @Mock
    APNSChannelRepository apnsChannelRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @InjectMocks
    TagServiceImpl tagService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAddTag() throws Exception {
        //Given
        TagDTO tagDTO = new TagDTO();
        tagDTO.setTagName("Sample Tag");
        tagDTO.setAppId("1l");
        tagDTO.setReceiver("123467asdfjg");
        tagDTO.setTagName(Channel.Type.IOS.name());


        Application app = new Application();
        app.setMode(ApplicationMode.DEVELOPMENT);
        app.setName("Push Sample App");
        app.setId("1l");

        Recipient recipient = new Recipient();
        recipient.setReceiver("123467asdfjg");
        recipient.setId(1l);

        Tag expected = new Tag();
        expected.setId(1l);

        when(applicationRepository.findOne("1l")).thenReturn(app);

        when(tagRepository.findByNameAndApplication("Sample Tag", app)).thenReturn(null);

        when(recipientRepository.findByReceiverAndChannelTypeAndApplication("123467asdfjg", Channel.Type.IOS, app)).thenReturn(null);

        when(recipientRepository.save(any(Recipient.class))).thenReturn(recipient);

        when(tagRepository.save(any(Tag.class))).thenReturn(expected);

        //When
        Tag actual = tagService.addTag(tagDTO);

        //Then
        assertEquals(expected,actual);
    }
}
