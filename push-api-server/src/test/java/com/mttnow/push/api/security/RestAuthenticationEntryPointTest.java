package com.mttnow.push.api.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import static org.junit.Assert.assertEquals;


public class RestAuthenticationEntryPointTest {
    RestAuthenticationEntryPoint entryPoint;

    @Before
    public void setup() {
        entryPoint = new RestAuthenticationEntryPoint();
    }

    @Test
    public void test401Response() throws Exception {
        //Given
        int expected = MockHttpServletResponse.SC_UNAUTHORIZED;
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authenticationException = new AuthenticationCredentialsNotFoundException("exception");

        //When
        entryPoint.commence(httpServletRequest, response, authenticationException);

        //Then
        assertEquals(expected, response.getStatus());
    }
}
