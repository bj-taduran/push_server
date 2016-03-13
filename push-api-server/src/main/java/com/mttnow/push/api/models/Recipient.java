package com.mttnow.push.api.models;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;

import javax.persistence.*;

@Entity
public class Recipient extends AuditableEntity {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column
    private String receiver;
    
    @Enumerated(EnumType.STRING)
    private Channel.Type channelType;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "APP_ID")
    private Application application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Channel.Type getChannelType() {
        return channelType;
    }

    public void setChannelType(Channel.Type channelType) {
        this.channelType = channelType;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "id='" + id + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", dateDeleted=" + dateDeleted +
                ", receiver='" + receiver + '\'' +
                ", channelType=" + channelType +
                ", application=" + application +
                '}';
    }
}
