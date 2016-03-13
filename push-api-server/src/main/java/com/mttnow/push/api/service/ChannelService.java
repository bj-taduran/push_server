package com.mttnow.push.api.service;


import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Channel;

import java.util.List;


/**
 * Service layer for Channel-related operations.
 */
public interface ChannelService {

    /**
     * Finds the list of channels by application ID.
     * @param appId application Id.
     */
    List<Channel> findByApplicationId(String appId);

    APNSChannel saveApnsChannel(APNSChannel apnsChannel);
}
