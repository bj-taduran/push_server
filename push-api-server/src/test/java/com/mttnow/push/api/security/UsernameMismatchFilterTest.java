package com.mttnow.push.api.security;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.service.ApplicationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.*;

public class UsernameMismatchFilterTest {
    @InjectMocks @Spy private UsernameMismatchFilter filter;
    private MockHttpServletResponse response;
    @Mock private MockHttpServletRequest request;
    @Mock private FilterChain chain;
    @Mock ApplicationService applicationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        response = new MockHttpServletResponse();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testUserIsGettingATagNotFromHisApplications() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_APPLICATIONS = "http://localhost:8080/push/tag/1/2";
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_APPLICATIONS));
        when(request.getPathInfo()).thenReturn("/tag/CA761232-ED42-11CE-BACD-00AA0057B223/2");

        List<Application> adminsApps = new ArrayList<Application>();
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B225"));
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B226"));
        when(applicationService.findAllByUser("admin")).thenReturn(adminsApps);

        //When
        filter.doFilter(request, response, chain);

        //Then
        assertThat(response.getStatus(), equalTo(401));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    public void testUserIsPostingAMessageNotFromHisApplications() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_APPLICATIONS = "http://localhost:8080/push/message";
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_APPLICATIONS));
        when(request.getPathInfo()).thenReturn("/message/CA761232-ED42-11CE-BACD-00AA0057B223");

        List<Application> adminsApps = new ArrayList<Application>();
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B227"));
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B228"));
        when(applicationService.findAllByUser("admin")).thenReturn(adminsApps);

        //When
        filter.doFilter(request, response, chain);

        //Then
        assertThat(response.getStatus(), equalTo(401));
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    public void testGetApplicationsMustNotBeChecked() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_APPLICATIONS = "/applications";
        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn("/applications");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_APPLICATIONS));

        //When
        filter.doFilter(request, response, chain);

        //Then
        verify(chain, times(1)).doFilter(request, response);
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void testGetUserMustNotBeChecked() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_USER = "/user";
        when(request.getMethod()).thenReturn("GET");
        when(request.getPathInfo()).thenReturn("/user");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_USER));

        //When
        filter.doFilter(request, response, chain);

        //Then
        verify(chain, times(1)).doFilter(request, response);
        assertThat(response.getStatus(), equalTo(200));
        verify(request, times(1)).setAttribute(anyString(), anyString());
    }

    //add test for /application/details/<appId>
    @Test
    public void testConsiderFirstNumberAsAppId() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_USER = "/user";
        when(request.getPathInfo()).thenReturn("/application/whatever/the/request/is/CA761232-ED42-11CE-BACD-00AA0057B223");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_USER));

        List<Application> adminsApps = new ArrayList<Application>();
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B223"));
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B224"));
        when(applicationService.findAllByUser("admin")).thenReturn(adminsApps);

        //When
        filter.doFilter(request, response, chain);

        //Then
        verify(chain, times(1)).doFilter(request, response);
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void testMalformedApplicationId() throws Exception {
        //Given
        String admin_admin = "Basic YWRtaW46YWRtaW4=";  //username: admin, password: admin
        String GET_APPLICATIONS = "http://localhost:8080/push/message";
        when(request.getMethod()).thenReturn("POST");
        when(request.getHeader("Authorization")).thenReturn(admin_admin);
        when(request.getRequestURL()).thenReturn(new StringBuffer(GET_APPLICATIONS));
        when(request.getPathInfo()).thenReturn("/message/CA761232-malformed-11CE-malformed-00AA0057B223");

        List<Application> adminsApps = new ArrayList<Application>();
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B227"));
        adminsApps.add(new Application("CA761232-ED42-11CE-BACD-00AA0057B228"));
        when(applicationService.findAllByUser("admin")).thenReturn(adminsApps);

        //When
        filter.doFilter(request, response, chain);

        //Then
        assertThat(response.getStatus(), equalTo(401));
        verify(chain, never()).doFilter(request, response);
    }

}
