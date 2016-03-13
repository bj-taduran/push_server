package com.mttnow.push.api.controllers;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class BaseController {
    protected  String getUserName(HttpServletRequest request) throws IOException {
        String header = request.getHeader("Authorization");
        String[] tokens = extractAndDecodeHeader(header);
        assert tokens.length == 2;
        String username = tokens[0];
        return username;
    }
    private String[] extractAndDecodeHeader(String header) throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] {token.substring(0, delim), token.substring(delim + 1)};
    }
}
