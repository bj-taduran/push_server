package com.mttnow.push.api.acceptance.steps;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import com.mttnow.push.api.models.Tag;

public class SelectTagsSteps extends BaseRestAcceptanceSteps {
  
  private HttpResponse response;
  private String path;
  private String tagName;

  @Given("user is in the Compose page")
  public void userOnComposePage(){
    path = "tags/4471dc68-9ee9-47bb-93a7-c058f743f9dd";
  }
  
  @Given("user typed some characters")
  public void userTypedSomeCharacters(){
    tagName = "/samp"; // Typed 'samp' in this case
  }
  
  @When("user clicked the Search button")
  public void clickedTheSearchButton() throws IOException{
    response = get(path + tagName);
    assertNotNull("response is null", response);
  }
  
  @SuppressWarnings("unchecked")
  @Then("user is directed to Tags list page limited with selection containing the user typed string")
  public void userIsDirectedToTagsList() throws IOException{
    List<Tag> tags = parseJSONResponse(response, ArrayList.class);
    assertTrue("no results found", tags.size() > 0);
  }
  
  @Given("user didn't type any character")
  public void userDidNotTypeAnyCharacter(){
    tagName = "";
  }
  
  @SuppressWarnings("unchecked")
  @Then("user is directed to Tags list page with the complete list of available tags")
  public void userIsDirectedToTagsListWithCompleteList() throws IOException{
    List<Tag> tags = parseJSONResponse(response, ArrayList.class);
    assertTrue("results less than expected", tags.size() > 1);
  }

}
