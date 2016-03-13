package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.GCMChannel;
import com.mttnow.push.api.service.ChannelService;
import com.mttnow.push.core.persistence.APNSChannelRepository;
import com.mttnow.push.core.persistence.GCMChannelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Channel service implementation.
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    APNSChannelRepository apnsChannelRepository;

    @Autowired
    GCMChannelRepository gcmChannelRepository;

    @Override
    public List<Channel> findByApplicationId(String appId) {
        List<Channel> channels = new ArrayList<Channel>();
        Application application = new Application(appId);

        APNSChannel apnsChannel = apnsChannelRepository.findByApplication(application);

        if (apnsChannel != null) {
            channels.add(apnsChannel);
        }

        GCMChannel gcmChannel = gcmChannelRepository.findByApplication(application);

        if (gcmChannel != null) {
            channels.add(gcmChannel);
        }

        return channels;
    }

    @Override
    public APNSChannel saveApnsChannel(APNSChannel apnsChannel) {
        return apnsChannelRepository.save(apnsChannel);
    }
}
