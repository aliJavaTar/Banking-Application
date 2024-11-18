package org.bro.banking.domin.usecase;

import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;
import org.junit.jupiter.api.Test;

class OpenAccountShould {

    @Test
    void customer_open_account_and_get_cart() {
        var openAccount = new OpenAccount();
        var request = new OpenAccountRequest();
        // min money must be  100 , get name of the bank , branch number


        CartResponse malie = openAccount.open(request);

        // cart with cvv2 , name of the bank , date of  expire , name of person ,number of card , shaba
    }
}