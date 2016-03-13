package com.mttnow.push.messaging.api.impl.camel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mttnow.push.api.models.ApnsMessage;
import com.mttnow.push.api.models.ApnsMessageLog;
import com.mttnow.push.api.models.Message;
import com.mttnow.push.api.service.MessageCompositionService;
import javapns.Push;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ApnsService {

    @Autowired
    MessageCompositionService messageCompositionService;

    private static Logger log = LoggerFactory.getLogger(ApnsService.class);
    private static Logger apnsTrackLog = LoggerFactory.getLogger("apns-track");

    public void process(Exchange exchange) throws Exception {
        ApnsMessage body = exchange.getIn().getBody(ApnsMessage.class);
        int recipientCount = body.getDeviceTokens().size();
        try {
            PushedNotifications pushedNotifications = Push.combined(body.getMessage(), 1, "default", body.getCertificate(), body.getPassword(), body.isProduction(), body.getDeviceTokens());

            int failedNotificationCount = pushedNotifications.getFailedNotifications().size();
            int successfulNotificationCount = pushedNotifications.getSuccessfulNotifications().size();
            Message.Status status;
            if ((recipientCount == failedNotificationCount) && recipientCount != 0) {
                status = Message.Status.FAILED;
            } else if(recipientCount != 0 && failedNotificationCount != 0 && successfulNotificationCount != 0 ){
                status = Message.Status.PARTIALLY_SENT;
            }else{
                status = Message.Status.SENT;
            }

            apnsTrackLog.info("APNS RESULT: {}", buildApnsLogMessage(body.getMessageId(), status, pushedNotifications));
            messageCompositionService.updateMessage(body, status, successfulNotificationCount, failedNotificationCount);

        } catch (Exception ex) {
            messageCompositionService.updateMessage(body, Message.Status.FAILED, 0, recipientCount );
            log.error(ex.getMessage(),ex);
            apnsTrackLog.error("APNS RESULT: {}", buildApnsLogMessage(body.getMessageId(), Message.Status.FAILED,ex,body.getDeviceTokens()));
        }


    }

    private String buildApnsLogMessage(Long messageId, Message.Status status, PushedNotifications pushedNotifications) throws JsonProcessingException {

        ApnsMessageLog.Builder builder = new ApnsMessageLog.Builder();
        ArrayList<ApnsMessageLog.FailedDeviceLog> failedDeviceLogs = new ArrayList<ApnsMessageLog.FailedDeviceLog>();
        PushedNotifications failedNotifications = pushedNotifications.getFailedNotifications();
        for (PushedNotification failedNotification: failedNotifications){
            String token = failedNotification.getDevice().getToken();
            String message = null;
            if(failedNotification.getResponse() != null){
                message = failedNotification.getResponse().getMessage();
            }
            if(message == null && failedNotification.getException() != null){
                message = failedNotification.getException().getMessage();
            }
            failedDeviceLogs.add(new ApnsMessageLog.FailedDeviceLog(token,message));
        }

        ArrayList<String> successfulDelivery = new ArrayList<String>();
        for (PushedNotification pushedNotification : pushedNotifications.getSuccessfulNotifications()) {
            successfulDelivery.add(pushedNotification.getDevice().getToken());
        }

        return builder.setMessageId(messageId).setStatus(status).setFailedDelivery(failedDeviceLogs).setSuccessfulDelivery(successfulDelivery).createApnsMessageLog().toJsonString();
    }
    private String buildApnsLogMessage(Long messageId, Message.Status status, Exception ex, List<String> deviceTokens) throws JsonProcessingException {
        ApnsMessageLog.Builder builder = new ApnsMessageLog.Builder();
        ArrayList<ApnsMessageLog.FailedDeviceLog> failedDeviceLogs = new ArrayList<ApnsMessageLog.FailedDeviceLog>();

        final String message = ex.getMessage();
        for (String deviceToken : deviceTokens){
            failedDeviceLogs.add(new ApnsMessageLog.FailedDeviceLog(deviceToken,message));
        }

        return builder.setMessageId(messageId).setStatus(status).setFailedDelivery(failedDeviceLogs).createApnsMessageLog().toJsonString();
    }
}
