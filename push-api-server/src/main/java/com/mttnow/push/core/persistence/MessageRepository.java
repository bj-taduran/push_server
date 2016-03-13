package com.mttnow.push.core.persistence;

import com.mttnow.push.api.models.Message;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long> {
}
