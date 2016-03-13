package com.mttnow.push.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class APNSChannel extends Channel {
    @Column
    private String cert;

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APNSChannel)) return false;
        if (!super.equals(o)) return false;

        APNSChannel that = (APNSChannel) o;

        if (cert != null ? !cert.equals(that.cert) : that.cert != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (cert != null ? cert.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "APNSChannel{" +
                "cert='" + cert + '\'' +
                "} " + super.toString();
    }
}
