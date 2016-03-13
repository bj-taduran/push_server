package com.mttnow.push.api.acceptance.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.ApplicationChannelDTO;
import com.mttnow.push.core.persistence.enums.ApplicationMode;
import org.apache.commons.lang.StringUtils;
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

public class UpdateAppSteps extends BaseRestAcceptanceSteps {

    private HttpResponse response;
    private Application requestApp;
    private APNSChannel apnsChannel;
    private ApplicationChannelDTO responseDTO;
    private Application appExisting;

    @Given("User has only one app")
    public void createAppInstance() {
        requestApp = new Application();
        requestApp.setId("SampleApplicationID");
        requestApp.setName("Sample Application 1");
        requestApp.setMode(ApplicationMode.DEVELOPMENT);

        apnsChannel = new APNSChannel();
        apnsChannel.setId(Long.valueOf(100));
        apnsChannel.setName("iOS");
    }

//    @Given("The user is on the apps details page or the apps dashboard page")
//    public void userOnAppsDetailsPage() {
//
//    }
//
//    @When("The user clicks on edit button")
//    public void editApplication() throws IOException {
//
//        String app = objectMapper.writeValueAsString(requestApp);
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("content",app);
//
//        String apns = objectMapper.writeValueAsString(apnsChannel);
//        map.put("ios-details",apns);
//        response = putMultipart("application/details", map);
//
//    }
//
//    @Then("The user is presented with the app details page in edit mode")
//    public void getAppDetails() throws IOException {
//        responseDTO = parseJSONResponse(response, ApplicationChannelDTO.class);
//        assertNotNull(responseDTO);
//        assertTrue(StringUtils.equals(responseDTO.getApplication().getName(), requestApp.getName()));
//    }

//    @Given("User has only one app and the user is on the apps details page or the apps dashboard page")
//    public void createApplicationInstance2() {
//
//    }
//
//    @When("The user clicks on save button")
//    public void saveApp() throws IOException {
//
//    }
//
//    @Then("The user is presented with an error message \"Please fill in required fields\"")
//    public void getErrorResponse() throws Exception {
//
//    }
}
