package com.mttnow.push.api.service;

import java.util.List;

import com.mttnow.push.api.models.Tag;
import com.mttnow.push.api.models.TagDTO;

public interface TagService {

  List<Tag> findAllByNameAndApplication(String appId, String tagName);
  
  Tag addTag(TagDTO tagDTO);
}
