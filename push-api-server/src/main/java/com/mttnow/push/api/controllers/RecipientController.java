package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;
import com.mttnow.push.api.service.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recipient")
public class RecipientController {

    @Autowired
    RecipientService recipientService;



    @RequestMapping(value={"/register"}, method = RequestMethod.POST)
    public @ResponseBody Recipient saveRecipient(@RequestParam("receiver") String receiver, @RequestParam("type") Channel.Type type, @RequestParam("appId") String appid) {
        return recipientService.saveRecipient(receiver,type,appid);
    }

    @RequestMapping(value={"{appId}/{receiver}/{type}"}, method = RequestMethod.DELETE)
    public @ResponseBody void delete(@PathVariable String appId, @PathVariable String receiver, @PathVariable Channel.Type type) {
        recipientService.deleteRecipient(receiver,type,appId);
    }

}
