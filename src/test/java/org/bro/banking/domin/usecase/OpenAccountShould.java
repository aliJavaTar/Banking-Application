package org.bro.banking.domin.usecase;

import org.assertj.core.api.Assertions;
import org.bro.banking.domin.Banks;
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
import static org.mockito.ArgumentMatchers.anyLong;
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
        when(mockAccounts.isExist(anyString(), anyLong())).thenReturn(true);
        var openAccount = new OpenAccount(mockAccounts, banks);
        assertThrows(IllegalArgumentException.class, () -> openAccount.open(getRequest()));
    }

    @Test
    void can_not_open_account_with_wrong_phone_number() {
        when(mockAccounts.isExist(anyString(), anyLong())).thenReturn(false);
        var openAccount = new OpenAccount(mockAccounts, banks);
        OpenAccountRequest request = OpenAccountRequest.builder().amount(new BigDecimal(10)).bankId(1).firstname("")
                .phoneNumber("4353452354").lastname("").nameOfFather("test").nationalCode("")
                .build();

        assertThrows(IllegalArgumentException.class, () -> openAccount.open(request));
    }

    @Test
    void generateCardNumber_16_digit_fourth_first_number_be_uneque_for_each_name_of_bank() {

    }

    @Test
    void customer_open_account_and_get_cart() {
        when(mockAccounts.isExist(anyString(), anyLong())).thenReturn(false);
        var openAccount = new OpenAccount(mockAccounts, banks);


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
                .bankId(1)
                .firstname("")
                .phoneNumber("09212221423")
                .lastname("")
                .nameOfFather("test")
                .nationalCode("")
                .build();


    }
}