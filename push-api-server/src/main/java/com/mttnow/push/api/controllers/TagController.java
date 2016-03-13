package com.mttnow.push.api.controllers;

import java.util.List;

import com.mttnow.push.api.models.TagDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.mttnow.push.api.models.Tag;
import com.mttnow.push.api.service.TagService;

/**
 * Tag Controller which is actually exposed as a restful service.
 * 
 */

@Controller
public class TagController {

  @Autowired
  TagService tagService;

  @RequestMapping(value="/tag", method = RequestMethod.POST)
  public @ResponseBody Tag addTag(@RequestBody TagDTO tagDTO){
     return tagService.addTag(tagDTO);
  }
  
  @RequestMapping(value={"/tags/{appId}"}, method = RequestMethod.GET)
  public @ResponseBody List<Tag> getTags(@PathVariable("appId") String appId){
    return findAllByNameAndApplication(appId, null);
  }
  
  @RequestMapping(value={"/tags/{appId}/{tagName}"}, method = RequestMethod.GET)
  public @ResponseBody List<Tag> getTags(@PathVariable("appId") String appId, @PathVariable("tagName") String tagName){
    return findAllByNameAndApplication(appId, tagName);
  }
  
  public List<Tag> findAllByNameAndApplication(String appId, String tagName){
    return tagService.findAllByNameAndApplication(appId, tagName);
  }
}
