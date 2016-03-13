package com.mttnow.push.api.models;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * Parent class of all auditable (with dateCreated, dateDeleted, dateUpdated)
 * entities. We're not providing a way to set the dateCreated and dateUpdated
 * field since it is being set via AuditTrailInterceptor class.
 */
@MappedSuperclass
public abstract class AuditableEntity {
  
  @Column
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  protected DateTime dateCreated;
  
  @Column
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  protected DateTime dateUpdated;
  
  @Column
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  protected DateTime dateDeleted;
  
  /**
   * Call this method to set the entity as deleted. Note that we're only using
   * "soft" delete on the database.
   */
  public void setDeleted(){
    this.dateDeleted = new DateTime();
  }

  public DateTime getDateCreated() {
    return dateCreated;
  }

  public DateTime getDateUpdated() {
    return dateUpdated;
  }

  public DateTime getDateDeleted() {
    return dateDeleted;
  }
  
}
