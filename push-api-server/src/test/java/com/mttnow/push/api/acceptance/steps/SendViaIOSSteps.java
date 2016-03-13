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
import com.mttnow.push.api.models.ComposeMessageDTO;

public class SendViaIOSSteps extends BaseRestAcceptanceSteps {
  
  private ComposeMessageDTO message;
  private HttpResponse response;

  @Given("user is on push message composition page")
  public void userOnPushMessageComposition(){;
    message = new ComposeMessageDTO();
  }
  
  @Given("iOS channel configuration is set up")
  public void iOSChannelIsConfigured(){
    // Do nothing
  }
  
  @Given("user enters valid message")
  public void enterValidMessage(){
    message.setMessage("message from acceptance test");
  }
  
  @Given("adds tags")
  public void addTags(){
    List<Long> tagIds = new ArrayList<Long>();
    tagIds.add(1l);
    message.setTagIds(tagIds);
  }
  
  @Given("selects iOS channel")
  public void selectIOSChannel(){
    // Do Nothing
  }
  
  @Given("selects Right Now as delivery time")
  public void selectRightNowAsDeliveryTime(){
    // Do Nothing
  }
  
  @When("user clicks on Send button")
  public void sendMessage() throws IOException{
    String message = objectMapper.writeValueAsString(this.message);
    response = post("message/4471dc68-9ee9-47bb-93a7-c058f743f9dd" , message);
  }
  
  @Then("user is presented with a page notification \"Message created and sent successfuly\"")
  public void notifiedWithMessage() throws IOException{
    assertTrue("problem on sending message to iOS", EntityUtils.toString(response.getEntity()).contains("Message Successfully Sent"));
  }
  
}
