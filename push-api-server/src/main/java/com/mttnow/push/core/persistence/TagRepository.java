package com.mttnow.push.core.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Tag;

/**
 * Spring Data Repository for Tag
 * Contains the CRUD and other possible persistence calls.
 * Implementation is provided magically by spring unless needed see impl package
 */
public interface TagRepository extends PagingAndSortingRepository<Tag,Long> {

  /**
   * Finds tags by application.
   * @param application with id
   * @return Set of channels
   */
  List<Tag> findAllByApplication(Application application);
  
  /**
   * Finds tags by application id and matching name.
   * @param application with id
   * @param name tag name
   * @return Set of channels
   */
  List<Tag> findAllByApplicationAndNameLikeIgnoreCase(Application application, String name);

    Tag findByNameAndApplication(String name, Application application);
}
