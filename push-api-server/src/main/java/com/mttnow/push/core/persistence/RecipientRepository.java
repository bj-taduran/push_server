package com.mttnow.push.core.persistence;

import java.util.List;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.Recipient;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipientRepository extends PagingAndSortingRepository<Recipient,Long> {
  
  /**
   * Finds recipients by application.
   * @param application with id
   * @return list of recipients
   */
  List<Recipient> findAllByApplication(Application application);
  
  /**
   * Finds recipients by application id and matching name.
   * @param appId application with id
   * @param name recipients name
   * @return Set of channels
   */
  List<Recipient> findAllByApplicationAndReceiverLikeIgnoreCase(Application application, String name);

    Recipient findByReceiverAndChannelType(String receiver, Channel.Type channelType);
    Recipient findByReceiverAndChannelTypeAndApplication(String receiver, Channel.Type channelType, Application application);
}
