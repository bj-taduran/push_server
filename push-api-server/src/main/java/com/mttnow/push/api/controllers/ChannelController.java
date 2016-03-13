package com.mttnow.push.api.controllers;

import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Controller for messaging operations.
 */
@Controller
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    ChannelService channelService;

    /**
     * Retrieves available channels for the given app.
     *
     * @param appId application id
     * @return List of available channels.
     */
    @RequestMapping(value = "/{appId}", method = RequestMethod.GET)
    public @ResponseBody List<Channel> getChannels(@PathVariable String appId) {
        return channelService.findByApplicationId(appId);
    }

}