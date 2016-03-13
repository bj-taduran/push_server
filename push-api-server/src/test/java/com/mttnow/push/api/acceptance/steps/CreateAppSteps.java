package com.mttnow.push.api.acceptance.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CreateAppSteps extends BaseRestAcceptanceSteps {

    private HttpResponse response;
    private Application application;
    private Application responseApp;
    private Application appExisting;

    @Given("The user is on app creation page")
    public void createAppInstance() {
        application = new Application();
    }

    @Given("The user inputs app name and app mode")
    public void addNameAndAppMode() {
        application.setName("Sample Create App Name");
        application.setMode(ApplicationMode.DEVELOPMENT);
    }

    @Given("The user did not select any channel type")
    public void skipChannelSelection() {
        //DO NOTHING
    }

    @When("The user clicks on save button")
    public void saveApp() throws IOException {
        String s = objectMapper.writeValueAsString(application);
        logInfo("############## JSON : " + s);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("content",s);
        response = postMultipart("application/multi", map);
    }

    @Then("The app creation is successful")
    public void validateSuccessResponse() throws IOException {
        responseApp = parseJSONResponse(response, Application.class);
        assertNotNull(responseApp);
        assertNotNull(responseApp.getId());
        
        response = get("applications");
        List<Application> userApplications = objectMapper.readValue(response.getEntity().getContent(),new TypeReference<List<Application>>() {});
        assertTrue(userApplications.contains(responseApp));
    }

    @Given("The user was not able to supply the required fields.")
    public void createApplicationInstance2() {
        application = new Application();

    }

    @Then("The user is presented with an error message \"Please fill in required fields\"")
    public void getErrorResponse() throws Exception {

        logInfo("################# Status Code: "+response.getStatusLine().getStatusCode());
        assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode());
        Map<String, String> result = objectMapper.readValue(response.getEntity().getContent(),new TypeReference<HashMap<String,Object>>() {});
        assertNotNull(result);
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("error","Please fill in required fields!");
        expected.put("user","User's name is empty!");
        expected.put("mode","Application mode not set!");
        expected.put("name","Application name is empty!");
        for (String s : result.keySet()) {
            assertTrue(expected.containsKey(s));
            assertEquals(expected.get(s), result.get(s));
        }

    }


    @Given("The user has an existing app")
    public void setupExistingApp() throws IOException {
        if (appExisting == null) {
            appExisting = new Application();
            appExisting.setMode(ApplicationMode.DEVELOPMENT);
            appExisting.setName("Existing App");
            appExisting.setId("4471dc68-9ee9-47bb-93a7-c058f743f9dd");

            String s = objectMapper.writeValueAsString(appExisting);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("content",s);
            HttpResponse existingResponse = postMultipart("application/multi", map);
            Application existingAppResponse = parseJSONResponse(existingResponse, Application.class);
            assertNotNull(existingAppResponse);
            appExisting = existingAppResponse;
        }
    }

    @Given("The app is still active")
    public void isAppActive() {
        //DO NOTHING
    }

    @When("The user creates a similar app")
    public void createAppWithSameName() throws IOException {
            application = new Application();
            application.setMode(ApplicationMode.DEVELOPMENT);
            application.setName("Existing App");

            String s = objectMapper.writeValueAsString(application);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("content",s);
            response = postMultipart("application/multi", map);
    }

    @Then("The user is presented an error message \"Application already exists. Please check your application details.\"")
    public void errorMessageAppExists() throws IOException {

        assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode());
        Map<String, String> result = objectMapper.readValue(response.getEntity().getContent(),new TypeReference<HashMap<String,Object>>() {});
        assertNotNull(result);
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("error","Application already exists. Please check your application details.");
        for (String s : result.keySet()) {
            assertTrue(expected.containsKey(s));
            assertEquals(expected.get(s), result.get(s));
        }
    }


    @Given("The app is no longer active")
    public void setAppInactive() throws IOException {
        //TODO
        appExisting.setDeleted();
        String s = objectMapper.writeValueAsString(appExisting);
        HttpResponse put = put("application/4471dc68-9ee9-47bb-93a7-c058f743f9dd", s);
        Application deletedApp = parseJSONResponse(put, Application.class);
        logInfo(deletedApp.toString());
    }
}
