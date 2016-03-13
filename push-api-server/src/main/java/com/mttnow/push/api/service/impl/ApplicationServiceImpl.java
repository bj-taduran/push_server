package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mttnow.push.api.service.ApplicationService;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.UserRepository;

import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    ApplicationRepository applicationRepository;
    
    @Autowired
    UserRepository userRepository;

    public Application findById(String id) {
        return applicationRepository.findOne(id);
    }

    @Cacheable("applications")
    public List<Application> findAllByUser(String username) {
        //TODO create custom query on repository to findByNameAndDateDeletedNullAndUser RDBFU-100
      User user = userRepository.findByUsername(username);
      if(user!=null){
        return user.getApplications();
      }
      return null;
    }

    @CacheEvict(value = "applications", allEntries = true)
    public Application save(Application application, String username) throws PushApplicationException {
        //TODO create custom query on repository to findByNameAndDateDeletedNotNullAndUser RDBFU-95
        List<Application> resultList = applicationRepository.findByName(application.getName());
        
        if (CollectionUtils.isEmpty(resultList)) {
            Application savedApplication = applicationRepository.save(application);
            attachApplicationToUser(savedApplication, username);
            LOGGER.info("saved app: " + savedApplication.toString());
            return savedApplication;
        } else {
            for (Application application1 : resultList) {
                LOGGER.info("####################Application: {}", application1);
                if (application1.getDateDeleted() == null) {
                    throw new PushApplicationException("Application already exists. Please check your application details.");
                }
            }

            Application savedApplication = applicationRepository.save(application);            
            attachApplicationToUser(savedApplication, username);
            LOGGER.info("saved app: " + savedApplication.toString());
            return savedApplication;
        }
    }

    @CacheEvict(value = "applications", allEntries = true)
    public Application update(Application application) throws PushApplicationException {
        Application savedApplication = applicationRepository.save(application);
        LOGGER.info("############### UPDATED APP: {} ", savedApplication);
        return savedApplication;
    }

    @CacheEvict(value = "applications", allEntries = true)
    public void delete(String id) {
        LOGGER.debug("deleted app: {}", id);
        Application one = applicationRepository.findOne(id);
        one.setDeleted();
        Application deleted = applicationRepository.save(one);
        LOGGER.debug("deleted record: {}", deleted);
    }
    
    private void attachApplicationToUser(Application application, String username){
      User user = userRepository.findByUsername(username);
      if(user!=null){
        user.getApplications().add(application);
        user = userRepository.save(user);
        LOGGER.info("updated user: " + user.toString());
      }
    }
}
