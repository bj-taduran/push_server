package com.mttnow.push.api.security;

import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.service.ApplicationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This filter makes sure that the REST user is accessing only apps that he owns by extracting
 * appId from the request. This class assumes that the appId conforms to standard UUID pattern.
 *
 * If appId given by user is not on the application list, error 401 is returned.
 *
 */
public class UsernameMismatchFilter extends GenericFilterBean {
    public static final String USER_NAME_FROM_HEADER = "userNameFromHeader";
    @Autowired
    ApplicationService applicationService;
    List<String> urlsNotToBeChecked;


    public UsernameMismatchFilter() {
        urlsNotToBeChecked = new ArrayList<String>();
        urlsNotToBeChecked.add("/applications");
        urlsNotToBeChecked.add("/application/multi");
        urlsNotToBeChecked.add("/user");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String userNameFromHeader = getUserNameFromHeader(request.getHeader("Authorization"));

        if(requestIsInListOfURLsNotToBeChecked(request.getPathInfo())) {
            request.setAttribute(USER_NAME_FROM_HEADER, userNameFromHeader);
        } else {
            List<Application> userApplications = applicationService.findAllByUser(userNameFromHeader);
            String applicationId = getApplicationIdFromRequest(request);
            if(!thisUserOwnsThisApplication(applicationId, userApplications)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * This method extracts UUID from the url.
     *
     * http://en.wikipedia.org/wiki/Universally_unique_identifier
     *
     *  @param request
     * @return
     */
    private String getApplicationIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo().toLowerCase();

        Pattern pattern = Pattern.compile("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}");
        Matcher matcher = pattern.matcher(pathInfo);

        if(matcher.find()) {
            return matcher.group();
        }

        return "invalid-app-id";
    }

    private boolean thisUserOwnsThisApplication(String appId, List<Application> applications) {
        for(Application application : applications) {
            if(appId.equalsIgnoreCase(application.getId())) {
                return true;
            }
        }

        return false;
    }

    private boolean requestIsInListOfURLsNotToBeChecked(String requestURL) {
        for(String url : urlsNotToBeChecked) {
            if(removeSlash(requestURL).equalsIgnoreCase(removeSlash(url))) {
                return true;
            }
        }

        return false;
    }

    private String removeSlash(String url) {
        return url.replace("/", "");
    }

    /**
     * Decodes the header into a username and password.
     */
    private String getUserNameFromHeader(String header) throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        decoded = Base64.decode(base64Token);

        String token = new String(decoded, "UTF-8");

        int delim = token.indexOf(":");

        return token.substring(0, delim);
    }


}
