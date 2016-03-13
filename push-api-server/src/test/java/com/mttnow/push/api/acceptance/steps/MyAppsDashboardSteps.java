package com.mttnow.push.api.acceptance.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Pending;
import org.jbehave.core.annotations.When;
import org.jbehave.core.annotations.Then;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.Channel;
import com.mttnow.push.api.models.ComposeMessageDTO;

public class MyAppsDashboardSteps extends BaseRestAcceptanceSteps {
  
  private HttpResponse response;
  private ArrayList<Application> applications;
  private Application currentApp;
  private List<Channel> channels;
  private String responseString;
  
  /* Other user */
  private Header header;
  private ArrayList<Application> otherUserApplications;

  @Given("user was able to login successfully")
  public void loginSuccessfully(){
    // header is already set on BaseRestAcceptanceSteps
  }
  
  @Given("user has admin rights")
  @Pending
  public void hasAdminRights(){
    // needs role in login service
  }

  @When("there is more than one application")
  public void hasMoreThanOneApplication() throws IOException{
    response = get("applications");
    applications = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Application>>() {});
    logInfo("############## Applications : " + applications);
    assertTrue("less than one", applications.size() > 1);
  }
  
  @Then("apps dashboard is displayed with the list of applications with quick links")
  @Given("user is presented with quick links on the my apps dashboard")
  @When("user is presented with quick links on the my apps dashboard")
  public void appsDashboardIsDisplayedWithQuickLinks(){
    assertNotNull("no apps in dashboard", applications);
    assertTrue("no apps in dashboard", applications.size() > 0);
  }
  
  @Then("create application link")
  @Pending
  public void hasCreateApplicationLink(){
    // need to call create app and create app needs to have a check in user roles
  }
  
  @Given("user application doesn’t have any channels configured")
  public void userApplicationDoesNotHaveChannels() throws IOException{
    if(applications==null){
      hasMoreThanOneApplication();
    }
    currentApp = applications.get(1);
    response = get("channel/" + currentApp.getId());
    channels = parseJSONResponse(response, ArrayList.class);
    assertNotNull(channels);
    assertEquals(0, channels.size());
  }
  
  @Given("user application have any channels configured")
  public void userApplicationHaveAnyChannels() throws IOException{
    if(applications==null){
      hasMoreThanOneApplication();
    }
    currentApp = applications.get(0);
    response = get("channel/" + currentApp.getId());
    channels = parseJSONResponse(response, ArrayList.class);
    assertNotNull(channels);
    assertTrue("no channels configured", channels.size() > 0);
  }
  
  @Then("compose quick link should be disabled")
  public void composeLinkIsDisabled() throws IOException{
    userTriesToClickComposeMessage();
    assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode());
  }
  
  @Then("a warning message is displayed for the application stating that no channels are configured yet")
  public void warningMessageIsDisplayedForAppsWithNoChannels(){
    assertEquals(0, channels.size());
    assertTrue("problem on sending message to iOS: " + responseString, responseString.contains("No channels configured for this application!"));
  }
  
  @When("user tries to click one of the enabled quick links")
  public void userTriesToClickComposeMessage() throws IOException{
    ComposeMessageDTO messageDTO = new ComposeMessageDTO();
    String message = objectMapper.writeValueAsString(messageDTO);
    response = post("message/" + currentApp.getId(), message);
    responseString = EntityUtils.toString(response.getEntity());
  }
  
  @Then("user should be able to access that feature")
  public void userAccessComposeMessage() throws IOException{
    assertEquals(HttpStatus.SC_OK,response.getStatusLine().getStatusCode());
    assertTrue("problem on sending message to iOS: " + responseString, responseString.contains("Message Successfully Sent"));
  }
  
  @Given("another user was able to login successfully")
  public void anotherUserTriesToLogin() throws IOException{
    if(applications==null){ // check first that previous user's applications were set..
      hasMoreThanOneApplication();
    }
    header = new BasicHeader("Authorization", "Basic bXR0LWFkbWluOm10dC1hZG1pbg=="); // mtt-admin:mtt-admin
  }
  
  @When("user tries to view his application")
  public void userTriesToViewApplications() throws IOException{
    response = get("applications", header);
    otherUserApplications = objectMapper.readValue(response.getEntity().getContent(), new TypeReference<List<Application>>() {});
    logInfo("############## Applications : " + otherUserApplications);
    assertNotNull("less than one", otherUserApplications);
  }
  
  @Then("user has a different set of applications from admin user")
  public void userHasADifferentSetOfApplication(){
    assertFalse(applications.equals(otherUserApplications));
    assertFalse(applications.size() == otherUserApplications.size());
    for(Application otherUserApplication : otherUserApplications){
      assertFalse(applications.contains(otherUserApplication));
    }
  }
  
}