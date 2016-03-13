package com.mttnow.push.core.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.mttnow.push.api.models.User;

/**
 * Spring Data Repository for User
 * Contains the CRUD and other possible persistence calls.
 * Implementation is provided magically by spring unless needed see impl package
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  User findByUsername(String username);
  
}
