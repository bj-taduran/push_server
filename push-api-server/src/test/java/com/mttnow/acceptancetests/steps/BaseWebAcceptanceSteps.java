package com.mttnow.acceptancetests.steps;

import static com.mttnow.acceptancetests.StoryContext.getFromContext;
import static com.mttnow.acceptancetests.StoryContext.putInContext;

import org.jbehave.core.annotations.BeforeStories;

public class BaseWebAcceptanceSteps extends AcceptanceSteps {

  private static final String BASE_URI = "baseUri";

  @BeforeStories
  public void before() throws Exception {
    putInContext(BASE_URI, initBaseUri());
  }
  
  public String getBaseURI(){
    return getFromContext(BASE_URI);
  }
    
  private static String initBaseUri(){
    String testUrl = System.getProperty("api.client.service.url");
    if (testUrl == null) {
      testUrl = "http://localhost:18080/" + System.getProperty("acceptance.tests.context") + "/";
    }
    return testUrl;
  }
  
}
