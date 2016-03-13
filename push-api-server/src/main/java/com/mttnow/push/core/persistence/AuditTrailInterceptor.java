package com.mttnow.push.core.persistence;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import com.mttnow.push.api.models.AuditableEntity;

/**
 * Customized hibernate interceptor for intercepting changes in table data. This
 * is used for setting the dateCreated, dateUpdated fields.
 * 
 * dateDeleted is set when AuditableEntity.setsetDeleted() is invoked. This is
 * because we're doing only soft delete of data (that is setting the dateDeleted
 * field to a value without actually deleting it)
 * 
 */

public class AuditTrailInterceptor extends EmptyInterceptor {

  private static final long serialVersionUID = 3228159731166019600L;
  
  private static final String DATE_CREATED = "dateCreated";
  private static final String DATE_UPDATED = "dateUpdated";
  private static final String DATE_DELETED = "dateDeleted";
  
  /**
   * Overridden method which is invoked on save of AuditableEntity. When
   * 'dateCreated' field is null then it assumes that it is a new object and
   * sets its value. else when 'dateCreated' is not null and 'dateDeleted' is
   * also not null, the it assumes that the entity is being updated and it sets
   * the 'dateUpdated' field.
   */
  
  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (entity instanceof AuditableEntity) {
      Object dateCreated = getValue(state, propertyNames, DATE_CREATED);
      Object dateDeleted = getValue(state, propertyNames, DATE_DELETED);
      
      if(dateCreated==null){
        setValue(state, propertyNames, DATE_CREATED, new DateTime());
      }else if(dateDeleted == null){
        setValue(state, propertyNames, DATE_UPDATED, new DateTime());
      }
      return true;
    }
    return false;
  }
  
  private void setValue(Object[] state, String[] propertyNames, String propertyToSet, Object value) {
    for(int x=0; x < propertyNames.length; x++){
      if(propertyToSet.equals(propertyNames[x])){
        state[x] = value;
        break;
      }
    }
  }
  
  private Object getValue(Object[] state, String[] propertyNames, String propertyToGet) {
    for(int x=0; x < propertyNames.length; x++){
      if(propertyToGet.equals(propertyNames[x])){
        return state[x];
      }
    }
    return null;
  }

}
