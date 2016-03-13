package com.mttnow.push.api.controllers;

import com.mttnow.push.api.exceptions.PushChannelException;
import com.mttnow.push.api.models.ComposeMessageDTO;
import com.mttnow.push.api.service.MessageCompositionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/message")
public class ComposeController {

    @Autowired
    MessageCompositionService messageCompositionService;

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public @ResponseBody
    String sendMessage(@RequestBody ComposeMessageDTO message, @PathVariable String id) throws PushChannelException {
        message.setAppId(id);
        return messageCompositionService.sendMessage(message);
    }

    protected void setMessageCompositionService(MessageCompositionService messageCompositionService) {
        this.messageCompositionService = messageCompositionService;
    }
    
}

