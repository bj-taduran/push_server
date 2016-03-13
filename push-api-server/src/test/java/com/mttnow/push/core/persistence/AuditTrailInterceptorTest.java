package com.mttnow.push.core.persistence;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.User;

public class AuditTrailInterceptorTest {

  AuditTrailInterceptor interceptor;
  String[] propertyNames;
  
  @Before
  public void setUp(){
    interceptor = new AuditTrailInterceptor();
    propertyNames = new String[] {"id", "dateCreated", "dateUpdated", "dateDeleted"};
  }
  
  @Test
  public void shouldSetDateCreatedWhenNull(){
    Object[] state = new Object[]{"abc123", null, null, null};
    boolean modified = interceptor.onSave(new Application("abc123"), Application.class, state, propertyNames, null);
    assertTrue("modified", modified);
    assertNotNull("dateCreated", state[1]);
    assertTrue("modified", state[1] instanceof DateTime);
  }
  
  @Test
  public void shouldSetDateUpdatedWhenDateCreatedIsNotNullAndDateDeletedIsNull(){
    Object[] state = new Object[]{"abc123", new DateTime(), null, null};
    boolean modified = interceptor.onSave(new Application("abc123"), Application.class, state, propertyNames, null);
    assertTrue("modified", modified);
    assertNotNull("dateUpdated", state[2]);
    assertTrue("modified", state[2] instanceof DateTime);
  }
  
  @Test
  public void shouldDoNothingWhenEntityIsNotAuditable(){
    Object[] state = new Object[]{"abc123", null, null, null};
    boolean modified = interceptor.onSave(new User(), Application.class, state, propertyNames, null);
    assertFalse("modified", modified);
  }
  
}
