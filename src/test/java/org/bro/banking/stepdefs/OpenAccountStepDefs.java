package org.bro.banking.stepdefs;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bro.banking.domin.account.usecase.OpenAccount;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;

import static org.junit.jupiter.api.Assertions.*;

public class OpenAccountStepDefs {
    private OpenAccountRequest request;
    private CartResponse response;

    @Given("I provide a valid phone number")
    public void i_provide_a_valid_phone_number() {
        request = new OpenAccountRequest();
        request.setPhoneNumber("09123456789");
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setNationalCode("123456789");
        request.setBankId(1);
    }

    @When("I submit the account opening request")
    public void i_submit_the_account_opening_request() {
        var openAccount = new OpenAccount(null, null);
        response = openAccount.open(request);
    }

    @Then("the account should be opened successfully")
    public void the_account_should_be_opened_successfully() {
        assertNotNull(response);
        assertFalse(response.getNumberOfCart().isEmpty());
    }
}
