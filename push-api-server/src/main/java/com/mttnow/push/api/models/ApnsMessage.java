package com.mttnow.push.api.models;

import java.io.Serializable;
import java.util.List;

public class ApnsMessage implements Serializable {
  
    private static final long serialVersionUID = -526888640569846714L;
    private Long messageId;
    private String message;
    private boolean production;
    private String certificate;
    private String password;
    private List<String> deviceTokens;

    public ApnsMessage(){

    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeviceTokens(List<String> deviceTokens) {
        this.deviceTokens = deviceTokens;
    }

    public void setProduction(boolean production) {
        this.production = production;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public boolean isProduction() {
        return production;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getDeviceTokens() {
        return deviceTokens;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
