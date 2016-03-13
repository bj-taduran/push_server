package com.mttnow.push.api.models;

import com.mttnow.push.api.models.Channel;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;

public class TagDTO {
  
    private String appId;
    private String tagName;
    private String receiver;
    private Channel.Type type;

    public TagDTO() {
    }
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Channel.Type getType() {
        return type;
    }

    public void setType(Channel.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "appId=" + appId +
                ", tagName='" + tagName + '\'' +
                ", receiver='" + receiver + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDTO tagDTO = (TagDTO) o;

        if (appId != null ? !appId.equals(tagDTO.appId) : tagDTO.appId != null) return false;
        if (receiver != null ? !receiver.equals(tagDTO.receiver) : tagDTO.receiver != null) return false;
        if (tagName != null ? !tagName.equals(tagDTO.tagName) : tagDTO.tagName != null) return false;
        if (type != tagDTO.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = appId != null ? appId.hashCode() : 0;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        result = 31 * result + (receiver != null ? receiver.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
