package com.mttnow.push.api.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
public class Tag extends AuditableEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;
    
    @ManyToMany
    @JoinTable(name="TAGS_RECIPIENTS")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Recipient> recipients;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "TAGS_PARENT")
    private List<Tag> parents;
    
    @ManyToOne
    @JoinColumn(name = "APP_ID")
    private Application application;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Recipient> recipients) {
        this.recipients = recipients;
    }

    public List<Tag> getParents() {
        return parents;
    }

    public void setParents(List<Tag> parents) {
        this.parents = parents;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (!application.equals(tag.application)) return false;
        if (!dateCreated.equals(tag.dateCreated)) return false;
        if (dateDeleted != null ? !dateDeleted.equals(tag.dateDeleted) : tag.dateDeleted != null) return false;
        if (dateUpdated != null ? !dateUpdated.equals(tag.dateUpdated) : tag.dateUpdated != null) return false;
        if (!id.equals(tag.id)) return false;
        if (!name.equals(tag.name)) return false;
        if (parents != null ? !parents.equals(tag.parents) : tag.parents != null) return false;
        if (recipients != null ? !recipients.equals(tag.recipients) : tag.recipients != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + dateCreated.hashCode();
        result = 31 * result + (dateUpdated != null ? dateUpdated.hashCode() : 0);
        result = 31 * result + (dateDeleted != null ? dateDeleted.hashCode() : 0);
        result = 31 * result + (recipients != null ? recipients.hashCode() : 0);
        result = 31 * result + (parents != null ? parents.hashCode() : 0);
        result = 31 * result + application.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", dateDeleted=" + dateDeleted +
                ", recipients=" + recipients +
                ", parents=" + parents +
                ", application=" + application +
                '}';
    }
}
