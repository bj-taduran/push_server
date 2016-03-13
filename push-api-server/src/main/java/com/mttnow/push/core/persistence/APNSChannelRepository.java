package com.mttnow.push.core.persistence;

import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * Spring Data Repository for APNSChannel
 * Contains the CRUD and other possible persistence calls.
 * Implementation is provided magically by spring unless needed see impl package
 */
public interface APNSChannelRepository extends PagingAndSortingRepository<APNSChannel,Long> {

    /**
     * Finds an APNS channel by application.
     * @param application application to search channels for
     * @return APNSChannel instance
     */
    APNSChannel findByApplication(Application application);
}
