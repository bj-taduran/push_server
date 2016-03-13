package com.mttnow.push.api.controllers;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.*;
import com.mttnow.push.api.service.ApplicationService;
import com.mttnow.push.api.service.ChannelService;
import com.mttnow.push.api.utils.FileUploader;
import com.mttnow.push.api.utils.PushObjectMapper;
import com.mttnow.push.api.validators.ApplicationValidator;
import com.mttnow.push.core.component.ApnsCertificateStorageHandler;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUploader.class)
public class ApplicationControllerTest {
    @Mock
    ApplicationService applicationService;
    @Mock
    ChannelService channelService;
    ApplicationValidator applicationValidator;
    @Mock
    ApnsCertificateStorageHandler apnsCertificateStorageHandler;
    ApplicationController controller;
    Application app;
    @Mock
    HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        controller = new ApplicationController();
        controller.applicationService = applicationService;
        controller.channelService = channelService;
        applicationValidator = new ApplicationValidator();
        controller.applicationValidator = applicationValidator;
        controller.apnsCertificateStorageHandler = apnsCertificateStorageHandler;
        mockStatic(FileUploader.class);
        when(request.getHeader("Authorization")).thenReturn("Basic YWRtaW46YWRtaW4=");

        app = new Application();
        app.setMode(ApplicationMode.DEVELOPMENT);
        app.setName("Push Sample App");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldGetTheCorrectApplication() throws Exception {
        Application expected = app;

        when(applicationService.findById(anyString())).thenReturn(expected);

        Application actual = controller.getApplication("1l");

        assertTrue(expected.equals(actual));
    }

    @Test
    public void shouldGetApplicationsThruEncodedUsercredentials() throws Exception {
        List<Application> expected = new ArrayList<Application>();
        expected.add(app);

        when(request.getHeader("Authorization")).thenReturn("Basic YWRtaW46YWRtaW4=");
        when(applicationService.findAllByUser(anyString())).thenReturn(expected);

        Iterable<Application> actual = controller.getApplications(request);
        assertEquals(expected, actual);
    }


    @Test
    public void shouldSaveApplicationWithCompleteDetails() throws Exception, PushApplicationException {
        final String content = "{\"id\":null,\"name\":\"File Upload App 3\",\"mode\":\"DEVELOPMENT\",\"dateCreated\":null,\"dateDeleted\":null}";
        final String iosDetails = "{\"id\":null, \"name\":\"ios\",\"type\":\"IOS\",\"password\":\"art!ckm0nk5y\"}";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("PushSample.p12");
        final MultipartFile file = new MockMultipartFile("PushSample.p12", resourceAsStream);
        final Application expected = app;
        expected.setId("1l");

        when(apnsCertificateStorageHandler.getApplicationCertificateStorage(expected.getId())).thenReturn("/fake/path/to/cert/storage");
        when(applicationService.save(any(Application.class), anyString())).thenReturn(expected);
        when(channelService.saveApnsChannel(any(APNSChannel.class))).thenReturn(new APNSChannel());

        Application actual = controller.saveApplicationMultipart(content, iosDetails, file, request);
        assertTrue(expected.equals(actual));
    }

    @Test(expected = PushApplicationException.class)
    public void saveApplicationMultiPartShouldThrowErrorWhenContentIsMissingDetail() throws Exception, PushApplicationException {
        final String content = "{\"id\":null,\"mode\":\"DEVELOPMENT\",\"dateCreated\":null,\"dateDeleted\":null}";
        final String iosDetails = "{\"id\":null, \"name\":\"ios\",\"type\":\"IOS\",\"password\":\"art!ckm0nk5y\"}";
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("PushSample.p12");
        final MultipartFile file = new MockMultipartFile("PushSample.p12", resourceAsStream);
        final Application expected = app;
        expected.setId("1l");

        when(apnsCertificateStorageHandler.getApplicationCertificateStorage(expected.getId())).thenReturn("/fake/path/to/cert/storage");
        when(applicationService.save(any(Application.class), anyString())).thenReturn(expected);
        when(channelService.saveApnsChannel(any(APNSChannel.class))).thenReturn(new APNSChannel());

        controller.saveApplicationMultipart(content, iosDetails, file, request);
    }

    @Test(expected = PushApplicationException.class)
    public void saveApplicationMultipartShouldThrowErrorWhenThereIsNoFile() throws Exception, PushApplicationException {
        final String content = "{\"id\":null,\"name\":\"File Upload App 3\",\"mode\":\"DEVELOPMENT\",\"dateCreated\":null,\"dateDeleted\":null}";
        final String iosDetails = "{\"id\":null, \"name\":\"ios\",\"type\":\"IOS\",\"password\":\"art!ckm0nk5y\"}";
        final Application expected = app;
        expected.setId("1l");

        when(apnsCertificateStorageHandler.getApplicationCertificateStorage(expected.getId())).thenReturn("/fake/path/to/cert/storage");
        when(applicationService.save(any(Application.class), anyString())).thenReturn(expected);
        when(channelService.saveApnsChannel(any(APNSChannel.class))).thenReturn(new APNSChannel());

        controller.saveApplicationMultipart(content, iosDetails, null, request);
    }

    @Test
    public void shouldDeleteApplication() throws Exception {
        doNothing().when(applicationService).delete(anyString());
        final String expected = "Successfully Deleted!";

        String actual = controller.deleteApplication("1l");
        assertEquals(expected, actual);
    }

    @Test
    public void shouldHandlePushApplicationException() throws Exception {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setError("Please fill in required fields!");
        PushApplicationException pushApplicationException = new PushApplicationException(errorMessage);
        ModelAndView result1 = controller.handlePushApplicationException(pushApplicationException);
        JsonError jsonError = new JsonError("Please fill in required fields!");
        assertEquals(jsonError.asModelAndView().getModel().toString(), result1.getModel().toString());

        ModelAndView result2 = controller.handlePushApplicationException(pushApplicationException);
        assertEquals(jsonError.asModelAndView().getModel().toString(), result2.getModel().toString());
    }

}
