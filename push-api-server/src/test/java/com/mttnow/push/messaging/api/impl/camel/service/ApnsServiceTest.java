package com.mttnow.push.messaging.api.impl.camel.service;

import com.mttnow.push.api.models.ApnsMessage;
import com.mttnow.push.api.service.MessageCompositionService;
import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.*;
import org.apache.camel.Message;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.ConnectException;
import java.util.ArrayList;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Push.class)
public class ApnsServiceTest {


    @Mock
    MessageCompositionService messageCompositionService;
    @Mock
    PushedNotifications pushedNotifications;
    @InjectMocks
    ApnsService apnsService;

    @Before
    public void setUp() throws Exception {
        mockStatic(Push.class);

    }

    @Test
    public void testSuccessfullySent() throws Exception {

        Device device = new BasicDevice();
        device.setDeviceId("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        device.setToken("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        Payload payload = new PushNotificationPayload();

        MTTPushedNotification failedNotification = new MTTPushedNotification(device, payload);
        failedNotification.setTransmissionCompleted(false);
        MTTResponsePacket responsePacket = new MTTResponsePacket();
        responsePacket.setCommand(8);
        responsePacket.setStatus(8);
        failedNotification.setResponse(responsePacket);
        MTTPushedNotification failedNotification2 = new MTTPushedNotification(device, payload);
        failedNotification2.setTransmissionCompleted(false);


        MTTPushedNotification successNotification = new MTTPushedNotification(device,payload);
        successNotification.setResponse(null);
        successNotification.setTransmissionCompleted(true);

        PushedNotifications failedNotifications = new PushedNotifications();

        PushedNotifications successfulNotifications = new PushedNotifications();
        successfulNotifications.add(successNotification);
        successfulNotifications.add(successNotification);

        when(Push.combined(anyString(), anyInt(), anyString(), anyObject(), anyString(), anyBoolean(), anyObject())).thenReturn(pushedNotifications);
        when(pushedNotifications.getFailedNotifications()).thenReturn(failedNotifications);
        when(pushedNotifications.getSuccessfulNotifications()).thenReturn(successfulNotifications);

        com.mttnow.push.api.models.Message successMsg= new com.mttnow.push.api.models.Message();
        successMsg.setStatus(com.mttnow.push.api.models.Message.Status.SENT);

        ApnsMessage apnsMessage = new ApnsMessage();
        ArrayList<String> deviceTokens = new ArrayList<String>();
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        apnsMessage.setDeviceTokens(deviceTokens);
        when(messageCompositionService.updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.SENT,2,0)).thenReturn(successMsg);

        DefaultExchange exchange = new DefaultExchange(new JmsEndpoint());
        Message camelMessage = new DefaultMessage();
        camelMessage.setBody(apnsMessage);
        exchange.setIn(camelMessage);

        //When
        apnsService.process(exchange);

        verify(messageCompositionService).updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.SENT,2,0);


    }

    @Test
    public void testFailedNotification() throws Exception{

        Device device = new BasicDevice();
        device.setDeviceId("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        device.setToken("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        Payload payload = new PushNotificationPayload();

        MTTPushedNotification failedNotification = new MTTPushedNotification(device, payload);
        failedNotification.setTransmissionCompleted(false);
        MTTResponsePacket responsePacket = new MTTResponsePacket();
        responsePacket.setCommand(8);
        responsePacket.setStatus(8);
        failedNotification.setResponse(responsePacket);
        MTTPushedNotification failedNotification2 = mock(MTTPushedNotification.class);
        when(failedNotification2.getDevice()).thenReturn(device);
        when(failedNotification2.getPayload()).thenReturn(payload);
        when(failedNotification2.isTransmissionCompleted()).thenReturn(false);
        when(failedNotification2.getException()).thenReturn(new CommunicationException("Cannot connect to apple servers",new ConnectException()));


        MTTPushedNotification successNotification = new MTTPushedNotification(device,payload);
        successNotification.setResponse(null);
        successNotification.setTransmissionCompleted(true);

        PushedNotifications failedNotifications = new PushedNotifications();
        failedNotifications.add(failedNotification);
        failedNotifications.add(failedNotification2);

        PushedNotifications successfulNotifications = new PushedNotifications();


        when(Push.combined(anyString(), anyInt(), anyString(), anyObject(), anyString(), anyBoolean(), anyObject())).thenReturn(pushedNotifications);

        com.mttnow.push.api.models.Message failedMsg = new com.mttnow.push.api.models.Message();
        failedMsg.setStatus(com.mttnow.push.api.models.Message.Status.FAILED);

        ApnsMessage apnsMessage = new ApnsMessage();
        ArrayList<String> deviceTokens = new ArrayList<String>();
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac8");
        apnsMessage.setDeviceTokens(deviceTokens);

        when(messageCompositionService.updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.FAILED,0,2)).thenReturn(failedMsg);

        DefaultExchange exchange = new DefaultExchange(new JmsEndpoint());
        Message camelMessage = new DefaultMessage();
        camelMessage.setBody(apnsMessage);
        exchange.setIn(camelMessage);


        when(pushedNotifications.getFailedNotifications()).thenReturn(failedNotifications);
        when(pushedNotifications.getSuccessfulNotifications()).thenReturn(successfulNotifications);

        //When
        apnsService.process(exchange);

        //Then
        verify(messageCompositionService).updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.FAILED,0,2);
    }

    @Test
    public void testPartialSent() throws Exception{

        Device device = new BasicDevice();
        device.setDeviceId("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        device.setToken("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        Payload payload = new PushNotificationPayload();

        MTTPushedNotification failedNotification = new MTTPushedNotification(device, payload);
        failedNotification.setTransmissionCompleted(false);
        MTTResponsePacket responsePacket = new MTTResponsePacket();
        responsePacket.setCommand(8);
        responsePacket.setStatus(8);
        failedNotification.setResponse(responsePacket);
        MTTPushedNotification failedNotification2 = new MTTPushedNotification(device, payload);
        failedNotification2.setTransmissionCompleted(false);


        MTTPushedNotification successNotification = new MTTPushedNotification(device,payload);
        successNotification.setResponse(null);
        successNotification.setTransmissionCompleted(true);

        PushedNotifications failedNotifications = new PushedNotifications();
        failedNotifications.add(failedNotification);
        failedNotifications.add(failedNotification2);

        PushedNotifications successfulNotifications = new PushedNotifications();
        successfulNotifications.add(successNotification);

        when(Push.combined(anyString(), anyInt(), anyString(), anyObject(), anyString(), anyBoolean(), anyObject())).thenReturn(pushedNotifications);

        com.mttnow.push.api.models.Message failedMsg = new com.mttnow.push.api.models.Message();
        failedMsg.setStatus(com.mttnow.push.api.models.Message.Status.FAILED);

        ApnsMessage apnsMessage = new ApnsMessage();
        ArrayList<String> deviceTokens = new ArrayList<String>();
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        apnsMessage.setDeviceTokens(deviceTokens);
        when(messageCompositionService.updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.PARTIALLY_SENT,1,1)).thenReturn(failedMsg);

        DefaultExchange exchange = new DefaultExchange(new JmsEndpoint());
        Message camelMessage = new DefaultMessage();
        camelMessage.setBody(apnsMessage);
        exchange.setIn(camelMessage);


        when(pushedNotifications.getFailedNotifications()).thenReturn(failedNotifications);
        when(pushedNotifications.getSuccessfulNotifications()).thenReturn(successfulNotifications);

        //When
        apnsService.process(exchange);

        //Then
        verify(messageCompositionService).updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.PARTIALLY_SENT,1,2);
    }

    @Test
    public void testFailDueToException() throws Exception {
        //Given
        Device device = new BasicDevice();
        device.setDeviceId("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        device.setToken("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        Payload payload = new PushNotificationPayload();

        MTTPushedNotification failedNotification = new MTTPushedNotification(device, payload);
        failedNotification.setTransmissionCompleted(false);
        MTTResponsePacket responsePacket = new MTTResponsePacket();
        responsePacket.setCommand(8);
        responsePacket.setStatus(8);
        failedNotification.setResponse(responsePacket);
        MTTPushedNotification failedNotification2 = new MTTPushedNotification(device, payload);
        failedNotification2.setTransmissionCompleted(false);


        MTTPushedNotification successNotification = new MTTPushedNotification(device,payload);
        successNotification.setResponse(null);
        successNotification.setTransmissionCompleted(true);

        PushedNotifications failedNotifications = new PushedNotifications();
        failedNotifications.add(failedNotification);
        failedNotifications.add(failedNotification2);


        when(Push.combined(anyString(), anyInt(), anyString(), anyObject(), anyString(), anyBoolean(), anyObject())).thenReturn(pushedNotifications);

        com.mttnow.push.api.models.Message failedMsg = new com.mttnow.push.api.models.Message();
        failedMsg.setStatus(com.mttnow.push.api.models.Message.Status.FAILED);
        com.mttnow.push.api.models.Message successMsg= new com.mttnow.push.api.models.Message();
        failedMsg.setStatus(com.mttnow.push.api.models.Message.Status.SENT);

        ApnsMessage apnsMessage = new ApnsMessage();
        ArrayList<String> deviceTokens = new ArrayList<String>();
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac7");
        deviceTokens.add("a9b7bf87b18894bfbbea91a491155b24fdc28ba0bda68d85faf8ec8f96516ac8");
        apnsMessage.setDeviceTokens(deviceTokens);
        when(messageCompositionService.updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.FAILED,0,1)).thenReturn(failedMsg);
        when(messageCompositionService.updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.SENT,1,0)).thenReturn(successMsg);

        DefaultExchange exchange = new DefaultExchange(new JmsEndpoint());
        Message camelMessage = new DefaultMessage();
        camelMessage.setBody(apnsMessage);
        exchange.setIn(camelMessage);

        when(Push.combined(anyString(), anyInt(), anyString(), anyObject(), anyString(), anyBoolean(), anyObject())).thenThrow(new CommunicationException("Cannot connect to apple server", new ConnectException()));


        //When
        apnsService.process(exchange);

        //Then
        verify(messageCompositionService).updateMessage(apnsMessage, com.mttnow.push.api.models.Message.Status.FAILED,0,2);
    }

    private class MTTPushedNotification extends PushedNotification{
        protected MTTPushedNotification(Device device, Payload payload) {
            super(device, payload);
        }

        @Override
        protected void setResponse(ResponsePacket response) {
            super.setResponse(response);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setTransmissionAttempts(int transmissionAttempts) {
            super.setTransmissionAttempts(transmissionAttempts);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setExpiry(long expiry) {
            super.setExpiry(expiry);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setIdentifier(int identifier) {
            super.setIdentifier(identifier);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setDevice(Device device) {
            super.setDevice(device);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setPayload(Payload payload) {
            super.setPayload(payload);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setTransmissionCompleted(boolean completed) {
            super.setTransmissionCompleted(completed);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public boolean isSuccessful() {
            if (!this.isTransmissionCompleted()) return false;
            if (this.getResponse()== null) return true;
            if (!this.getResponse().isValidErrorMessage()) return true;
            return false;
        }


    }



    private class MTTResponsePacket extends ResponsePacket {
        @Override
        protected void setCommand(int command) {
            super.setCommand(command);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        protected void setStatus(int status) {
            super.setStatus(status);    //To change body of overridden methods use File | Settings | File Templates.
        }

        /**
         * Returns a humand-friendly error message, as documented by Apple.
         *
         * @return a humand-friendly error message
         */
        public String getMessage() {
            if (this.getCommand() == 8) {
                String prefix = "APNS: [" + this.getIdentifier() + "] "; //APNS ERROR FOR MESSAGE ID #" + identifier + ": ";
                if (this.getStatus() == 0) return prefix + "No errors encountered";
                if (this.getStatus() == 1) return prefix + "Processing error";
                if (this.getStatus() == 2) return prefix + "Missing device token";
                if (this.getStatus() == 3) return prefix + "Missing topic";
                if (this.getStatus() == 4) return prefix + "Missing payload";
                if (this.getStatus() == 5) return prefix + "Invalid token size";
                if (this.getStatus() == 6) return prefix + "Invalid topic size";
                if (this.getStatus() == 7) return prefix + "Invalid payload size";
                if (this.getStatus() == 8) return prefix + "Invalid token";
                if (this.getStatus() == 255) return prefix + "None (unknown)";
                return prefix + "Undocumented status code: " + this.getStatus();
            }
            return "APNS: Undocumented response command: " + this.getCommand();
        }
    }
}
