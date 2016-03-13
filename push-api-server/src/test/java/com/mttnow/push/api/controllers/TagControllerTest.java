package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.Tag;
import com.mttnow.push.api.models.TagDTO;
import com.mttnow.push.api.service.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagControllerTest {
    @Mock
    TagService tagService;
    TagController controller;

    @Before
    public void setUp() throws Exception {
        controller = new TagController();
        controller.tagService = tagService;

    }

    @Test
    public void testAddTag() {
        Tag tag = new Tag();
        tag.setName("Sample Tag");
        TagDTO tagDTO = new TagDTO();
        tagDTO.setTagName("Sample Tag");

        when(tagService.addTag(tagDTO)).thenReturn(tag);
        Tag actual = controller.addTag(tagDTO);
        assertTrue(tag.equals(actual));
    }

    @Test
    public void shouldFindTagsByApplicationId() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Sample Tag");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Another Sample Tag");

        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag1);
        tags.add(tag2);

        when(tagService.findAllByNameAndApplication(eq("abc123"), (String) eq(null))).thenReturn(tags);
        List<Tag> actualTags = controller.getTags("abc123");

        assertTrue(tags.equals(actualTags));
    }

    @Test
    public void shouldFindTagsByApplicationIdAndTagName() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Sample Tag");

        List<Tag> tags = new ArrayList<Tag>();
        tags.add(tag1);

        when(tagService.findAllByNameAndApplication(eq("abc123"), (String) eq("Sample"))).thenReturn(tags);
        List<Tag> actualTags = controller.getTags("abc123", "Sample");
        assertTrue(tags.equals(actualTags));

    }

}
