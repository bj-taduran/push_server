package com.mttnow.push.api.acceptance.steps;

import static org.junit.Assert.*;

import com.mttnow.acceptancetests.steps.BaseRestAcceptanceSteps;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

public class LoginSteps extends BaseRestAcceptanceSteps {
    private BasicHeader badCredential;
    private HttpResponse response;

    @Given("User has no valid account")
    public void givenUserHasAValidAccount() throws Exception {
        badCredential =  new BasicHeader("Authorization", "Basic wrongunameandpw=");
    }

    @When("The user tries to log in w/ username and password")
    public void whenUserTriesToLogInWrongCredentials() throws Exception {
        response = post("app", badCredential);
    }

    @Then("The user is presented with HTTP response 401")
    public void httpResponse401() {
        Header header = response.getFirstHeader("Authorization");
        int actual = response.getStatusLine().getStatusCode();
        int expected = 401;
        assertEquals(expected, actual);
    }



}
