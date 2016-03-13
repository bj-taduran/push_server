package com.mttnow.push.api.models;

import java.util.List;

public class ComposeMessageDTO {

    private String title;
    private String message;
    private List<Long> recipientIds;
    private List<Long> tagIds;
    private List<Channel.Type> channelTypes;
    private String appId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Long> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(List<Long> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public List<Channel.Type> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<Channel.Type> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComposeMessageDTO that = (ComposeMessageDTO) o;

        if (appId != null ? !appId.equals(that.appId) : that.appId != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (appId != null ? appId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComposeMessageDTO{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", recipientIds=" + recipientIds +
                ", tagIds=" + tagIds +
                ", channelTypes=" + channelTypes +
                ", appId=" + appId +
                '}';
    }
}
