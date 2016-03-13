package com.mttnow.push.core.persistence;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.GCMChannel;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * Spring Data Repository for GCMChannel
 * Contains the CRUD and other possible persistence calls.
 * Implementation is provided magically by spring unless needed see impl package
 */
public interface GCMChannelRepository extends PagingAndSortingRepository<GCMChannel,Long> {

    /**
     * Finds a GCM channel by application.
     * @param application application to search channels for
     * @return GCMChannel instance associated with the application
     */
    GCMChannel findByApplication(Application application);
}
