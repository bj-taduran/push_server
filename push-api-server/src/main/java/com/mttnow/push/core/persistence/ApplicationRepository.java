package com.mttnow.push.core.persistence;

import java.util.List;

import com.mttnow.push.api.models.Application;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Spring Data Repository for Application
 * Contains the CRUD and other possible persistence calls.
 * Implementation is provided magically by spring unless needed see impl package
 */
public interface ApplicationRepository extends PagingAndSortingRepository<Application, String> {
  
  List<Application> findByName(String name);

}
