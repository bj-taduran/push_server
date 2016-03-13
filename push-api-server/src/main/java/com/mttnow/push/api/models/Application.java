package com.mttnow.push.api.models;

import com.mttnow.push.core.persistence.enums.ApplicationMode;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
public class Application extends AuditableEntity {

    @Id
    @GeneratedValue(generator="uuid", strategy=GenerationType.IDENTITY)
    @GenericGenerator(name="uuid", strategy = "uuid2")
    private String id;
    @Column
    private String name;
    @Enumerated(EnumType.STRING)
    private ApplicationMode mode;

    public Application() {

    }

    public Application(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationMode getMode() {
        return mode;
    }

    public void setMode(ApplicationMode mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        if (!id.equals(that.id)) return false;
        if (mode != that.mode) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + mode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mode=" + mode +
                ", dateCreated=" + dateCreated +
                ", dateDeleted=" + dateDeleted +
                '}';
    }
}
