package com.mttnow.push.api.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class ApnsMessageLog {
    private Long messageId;
    private Message.Status status;
    private List<String> successfulDelivery;
    private List<FailedDeviceLog> failedDelivery;

    public ApnsMessageLog(Long messageId, Message.Status status, List<String> successfulDelivery, List<FailedDeviceLog> failedDelivery) {
        this.messageId = messageId;
        this.status = status;
        this.successfulDelivery = successfulDelivery;
        this.failedDelivery = failedDelivery;
    }

    public Long getMessageId() {
        return messageId;
    }

    public Message.Status getStatus() {
        return status;
    }

    public List<String> getSuccessfulDelivery() {
        return successfulDelivery;
    }

    public List<FailedDeviceLog> getFailedDelivery() {
        return failedDelivery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApnsMessageLog that = (ApnsMessageLog) o;

        if (failedDelivery != null ? !failedDelivery.equals(that.failedDelivery) : that.failedDelivery != null)
            return false;
        if (messageId != null ? !messageId.equals(that.messageId) : that.messageId != null) return false;
        if (status != that.status) return false;
        if (successfulDelivery != null ? !successfulDelivery.equals(that.successfulDelivery) : that.successfulDelivery != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId != null ? messageId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (successfulDelivery != null ? successfulDelivery.hashCode() : 0);
        result = 31 * result + (failedDelivery != null ? failedDelivery.hashCode() : 0);
        return result;
    }

    public static class FailedDeviceLog {
        private String deviceId;
        private String reason;

        public FailedDeviceLog(String deviceId, String reason) {
            this.deviceId = deviceId;
            this.reason = reason;
        }

        public FailedDeviceLog() {
        }

        public String getDeviceId() {
            return deviceId;
        }



        public String getReason() {
            return reason;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FailedDeviceLog that = (FailedDeviceLog) o;

            if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
            if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = deviceId != null ? deviceId.hashCode() : 0;
            result = 31 * result + (reason != null ? reason.hashCode() : 0);
            return result;
        }
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public static class Builder {
        private Long messageId;
        private Message.Status status;
        private List<String> successfulDelivery;
        private List<ApnsMessageLog.FailedDeviceLog> failedDelivery;

        public Builder() {
        }

        public Builder setMessageId(Long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setStatus(Message.Status status) {
            this.status = status;
            return this;
        }

        public Builder setSuccessfulDelivery(List<String> successfulDelivery) {
            this.successfulDelivery = successfulDelivery;
            return this;
        }

        public Builder setFailedDelivery(List<ApnsMessageLog.FailedDeviceLog> failedDelivery) {
            this.failedDelivery = failedDelivery;
            return this;
        }

        public ApnsMessageLog createApnsMessageLog() {
            return new ApnsMessageLog(messageId, status, successfulDelivery, failedDelivery);
        }
    }
}
