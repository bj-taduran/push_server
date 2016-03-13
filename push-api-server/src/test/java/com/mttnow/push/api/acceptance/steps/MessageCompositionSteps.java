package com.mttnow.push.api.acceptance.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.ComposeMessageDTO;

public class MessageCompositionSteps extends BaseRestAcceptanceSteps {
  
  private Application application;
  private HttpResponse response;
  private String responseString;
  private List<Channel> channels;
  
  @Given("user has at least one channel configured for the app")
  public void applicationHasAtLeastOneChannel() throws IOException{
    response = get("application/4471dc68-9ee9-47bb-93a7-c058f743f9dd");
    application = parseJSONResponse(response, Application.class);
    assertNotNull(application);
    assertEquals("application id", "4471dc68-9ee9-47bb-93a7-c058f743f9dd", application.getId());
    assertNotNull(application.getName());
  }
  
  @When("user tries to compose a message")
  public void composeMessage() throws IOException {
    ComposeMessageDTO messageDTO = new ComposeMessageDTO();
    messageDTO.setAppId(application.getId());
    messageDTO.setMessage("message");
    String message = objectMapper.writeValueAsString(messageDTO);
    response = post("message/" + application.getId(), message);
    responseString = EntityUtils.toString(response.getEntity());
  }
  
  @Then("user is presented with the compose message screen with the configured channels available for selection")
  public void channelsAreAvailableForSelection() throws IOException {
    assertNotNull(responseString);
    assertTrue(responseString.contains("Message Successfully Sent"));
    response = get("channel/" + application.getId());
    channels = parseJSONResponse(response, ArrayList.class);
    assertNotNull(channels);
    assertTrue("no channels configured", channels.size() > 0);
  }
  
  @Given("user has no channels configured for an app")
  public void channelsAreNotAvailableForSelection() throws IOException {
    response = get("application/47a63d95-9582-41a3-9aa0-34fe2d97c428");
    application = parseJSONResponse(response, Application.class);
    assertNotNull(application);
    assertEquals("application id not equal", "47a63d95-9582-41a3-9aa0-34fe2d97c428", application.getId());
    assertNotNull(application.getName());
    
    response = get("channel/" + application.getId());
    channels = parseJSONResponse(response, ArrayList.class);
    assertNotNull(channels);
    assertTrue("no channels configured", channels.isEmpty());
  }
  
  @Then("compose message link/button is disabled")
  public void composeMessageIsDisabled(){
    assertTrue(responseString.contains("No channels configured for this application!"));
  }
 
}
