package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Recipient;
import com.mttnow.push.api.models.TagDTO;
import com.mttnow.push.core.persistence.APNSChannelRepository;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.RecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mttnow.push.api.models.Tag;
import com.mttnow.push.api.service.TagService;
import com.mttnow.push.core.persistence.TagRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

  @Autowired
  TagRepository tagRepository;
    @Autowired
    RecipientRepository recipientRepository;
    @Autowired
    APNSChannelRepository apnsChannelRepository;
    @Autowired
    ApplicationRepository applicationRepository;

    @Override
    public List<Tag> findAllByNameAndApplication(String appId, String tagName) {
      Application application = new Application(appId);
      if(tagName == null){
        return tagRepository.findAllByApplication(application);
      }
      return tagRepository.findAllByApplicationAndNameLikeIgnoreCase(application, '%' + tagName + '%');
    }
    
    @Override
    public Tag addTag(TagDTO tagDTO) {
        Application application = applicationRepository.findOne(tagDTO.getAppId());
        Tag tag = tagRepository.findByNameAndApplication(tagDTO.getTagName(), application);

        if(tag == null){
            tag = new Tag();
            tag.setName(tagDTO.getTagName());
            tag.setApplication(application);
        }

        List<Recipient> recipientList = tag.getRecipients();
        if(recipientList == null){
            recipientList = new ArrayList<Recipient>();
        }

        Recipient recipient = recipientRepository.findByReceiverAndChannelTypeAndApplication(tagDTO.getReceiver(), tagDTO.getType(), application);

        if(recipient == null){
            recipient = new Recipient();
            recipient.setReceiver(tagDTO.getReceiver());
            recipient.setChannelType(tagDTO.getType());
            recipient.setApplication(application);
            recipient = recipientRepository.save(recipient);
        }


        if(!recipientList.contains(recipient)){
            recipientList.add(recipient);
        }
        tag.setRecipients(recipientList);
        Tag savedTag = tagRepository.save(tag);
        return savedTag;
    }

}
