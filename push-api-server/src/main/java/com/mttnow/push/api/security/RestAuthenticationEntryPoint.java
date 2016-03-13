package com.mttnow.push.api.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is basically BasicAuthenticationEntryPoint without the header 'WWW-Authenticate', browsers use this
 * header to display the native login popup. We want to display our own styled login popup.
 */
@Component( "restAuthenticationEntryPoint" )
public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence( HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException ) throws IOException {
        response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
    }
}
