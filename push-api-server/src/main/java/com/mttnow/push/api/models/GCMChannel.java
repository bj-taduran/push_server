package com.mttnow.push.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class GCMChannel extends Channel {
    @Column
    private String gcmKey;
    public String getGcmKey() {
        return gcmKey;
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GCMChannel that = (GCMChannel) o;

        if (gcmKey != null ? !gcmKey.equals(that.gcmKey) : that.gcmKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (gcmKey != null ? gcmKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GCMChannel{" +
                "gcmKey='" + gcmKey + '\'' +
                "} " + super.toString();
    }
}
