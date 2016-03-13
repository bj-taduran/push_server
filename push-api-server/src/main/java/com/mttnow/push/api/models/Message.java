package com.mttnow.push.api.models;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Message extends AuditableEntity implements Serializable {

    private static final long serialVersionUID = 4012472844543733617L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String message;
    
    @Column
    private String title;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_RECIPIENTS")
    private List<Recipient> recipients;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "MESSAGE_TAGS")
    private List<Tag> tags;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Channel.Type> channelTypes;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateSent;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime dateScheduled;

    @ManyToOne(fetch = FetchType.EAGER)
    private Application application;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "success_count")
    private int successCount;
    @Column(name = "fail_count")
    private int failCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Channel.Type> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<Channel.Type> channelTypes) {
        this.channelTypes = channelTypes;
    }


    public DateTime getDateScheduled() {
        return dateScheduled;
    }

    public void setDateScheduled(DateTime dateScheduled) {
        this.dateScheduled = dateScheduled;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(DateTime dateSent) {
        this.dateSent = dateSent;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (failCount != message1.failCount) return false;
        if (successCount != message1.successCount) return false;
        if (!application.equals(message1.application)) return false;
        if (!channelTypes.equals(message1.channelTypes)) return false;
        if (dateScheduled != null ? !dateScheduled.equals(message1.dateScheduled) : message1.dateScheduled != null)
            return false;
        if (dateSent != null ? !dateSent.equals(message1.dateSent) : message1.dateSent != null) return false;
        if (!id.equals(message1.id)) return false;
        if (!message.equals(message1.message)) return false;
        if (status != message1.status) return false;
        if (title != null ? !title.equals(message1.title) : message1.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + channelTypes.hashCode();
        result = 31 * result + (dateSent != null ? dateSent.hashCode() : 0);
        result = 31 * result + (dateScheduled != null ? dateScheduled.hashCode() : 0);
        result = 31 * result + application.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + successCount;
        result = 31 * result + failCount;
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", recipients=" + recipients +
                ", tags=" + tags +
                ", channelTypes=" + channelTypes +
                ", dateSent=" + dateSent +
                ", dateScheduled=" + dateScheduled +
                ", application=" + application +
                ", status=" + status +
                ", successCount=" + successCount +
                ", failCount=" + failCount +
                '}';
    }

    public enum Status{
        IN_PROGRESS,SENT, FAILED , PARTIALLY_SENT
    }
}
