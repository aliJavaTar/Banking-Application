package org.bro.banking.stepdefs;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bro.banking.domin.account.usecase.OpenAccount;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSteps {

    @Given("I provide a valid phone number")
    public void iProvideAValidPhoneNumber() {
        // Add logic to provide a valid phone number
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
