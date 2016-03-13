package com.mttnow.push.api.acceptance.steps;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;

public class ViewAppDetailsSteps extends BaseRestAcceptanceSteps {

    private HttpResponse response;
    private Application actualApplication;
    private Application application_fromResponse;
    private List<Application> adminApplications;

    @Given("an app with complete details was already created for the user")
    public void createAppWithCompleteDetails() throws ClientProtocolException, IOException {
        HttpResponse applicationsResponse = get("applications");
        adminApplications = objectMapper.readValue(applicationsResponse.getEntity().getContent(),new TypeReference<List<Application>>() {});
        actualApplication = adminApplications.get(0);
    }

    @When("user access the app details page")
    public void accessAppDetails() throws IOException {
        HttpResponse httpResponse = get("application/" + actualApplication.getId());
        application_fromResponse = parseJSONResponse(httpResponse, Application.class);
    }

    @Then("user should see the complete details shown on the page")
    public void showCompleteDetails() {
        assertThat(actualApplication.getId(), equalTo(application_fromResponse.getId()));
        assertThat(actualApplication.getName(), equalTo(application_fromResponse.getName()));
        assertThat(actualApplication.getMode(), equalTo(application_fromResponse.getMode()));
    }

    @Given("an app with details for specific channels only was already created for the user")
    public void createAppForSpecificChannel() throws ClientProtocolException, IOException {
        actualApplication = adminApplications.get(0);
    }

    @Then("user should see only the details that was filled up on the page")
    public void showFilledOutDetails() {
        assertThat(actualApplication.getId(), equalTo(application_fromResponse.getId()));
        assertThat(actualApplication.getName(), equalTo(application_fromResponse.getName()));
        assertThat(actualApplication.getMode(), equalTo(application_fromResponse.getMode()));
    }

    @Given("an app with no configured channels was already created for the user")
    public void createAppWithoutChannels() throws ClientProtocolException, IOException {
        //the second item of the admin app has no associated channel
        actualApplication = adminApplications.get(1);
    }

    @Then("warning notification \"No channels are currently configured for this application. Please contact MTT support.\" should be presented to the user")
    public void noConfiguredChannelsWarning() throws IOException {
      response = get("channel/" + application_fromResponse.getId());
      List<Channel> channels = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Channel>>() {});
      assertNotNull(channels);
      assertTrue("message channels", channels.isEmpty());
    }

}
