package com.mttnow.push.api.service;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.Application;

import java.util.List;

public interface ApplicationService {
  
  public Application findById(String id);
  
  public List<Application> findAllByUser(String user);

  public Application save(Application application, String userName) throws PushApplicationException;
  
  public Application update(Application application) throws PushApplicationException;
  
  public void delete(String id);

}
