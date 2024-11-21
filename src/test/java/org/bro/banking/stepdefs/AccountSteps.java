package org.bro.banking.stepdefs;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
public class AccountSteps {

    @Given("I provide a valid phone number")
    public void iProvideAValidPhoneNumber() {
        System.out.println("adfsasdfa");
    }

    @When("I submit the account opening request")
    public void iSubmitTheAccountOpeningRequest() {
        // Add logic to submit the account opening request
    }

    @Then("the account should be opened successfully")
    public void theAccountShouldBeOpenedSuccessfully() {
        // Add logic to verify the account is opened
    }
}
