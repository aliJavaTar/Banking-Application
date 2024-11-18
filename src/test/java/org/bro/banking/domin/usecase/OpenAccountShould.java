package org.bro.banking.domin.usecase;

import org.assertj.core.api.Assertions;
import org.bro.banking.domin.Banks;
import org.bro.banking.domin.account.Account;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.domin.account.usecase.OpenAccount;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAccountShould {
    @Mock
    private Accounts mockAccounts;
    @Mock
    private Banks banks;

    @Test
    void customer_canNot_open_account_with_existed_acc_in_same_bank() {
        when(mockAccounts.isExist(anyString(), anyString())).thenReturn(true);
        var openAccount = new OpenAccount(mockAccounts, banks);
        assertThrows(IllegalArgumentException.class, () -> openAccount.open(getRequest()));
    }

    @Test
    void generateCardNumber_16_digit_fourth_first_number_be_uneque_for_each_name_of_bank() {
        Account account = new Account();
        account.generatedCard("mali");
    }

    @Test
    void customer_open_account_and_get_cart() {
        when(mockAccounts.isExist(anyString(), anyString())).thenReturn(false);
        var openAccount = new OpenAccount(mockAccounts,banks);


        OpenAccountRequest request = getRequest();


        CartResponse malie = openAccount.open(request);

        int length = String.valueOf(malie.getCvv2()).length();


        Assertions.assertThat(length).isEqualTo(3);
//        todo test date
//        Assertions.assertThat(malie.)

    }

    private OpenAccountRequest getRequest() {
        return OpenAccountRequest.builder()
                .amount(new BigDecimal(10))
                .firstname("")
                .lastname("")
                .codeOfBank(123)
                .nameOfFather("test")
                .nationalCode("")
                .nameOfBank("")
                .build();


    }
}