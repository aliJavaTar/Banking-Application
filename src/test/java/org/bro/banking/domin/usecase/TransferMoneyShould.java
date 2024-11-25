package org.bro.banking.domin.usecase;

import org.assertj.core.api.Assertions;
import org.bro.banking.domin.Account;
import org.bro.banking.domin.Accounts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

class TransferMoneyShould {

    @Mock
    private Accounts accounts;

    @Test
    void not_transfer_money_if_source_account_does_not_exist() {

        when(accounts.getAccountByIdAndNationalNumber(anyLong(),anyString()))
                .thenReturn(Optional.ofNullable(Account.class));

        // Given
        long account_id = 0;
        String national_number = "";

        // When
        var transfer = new TransferMoney(accounts);


        // Then
        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(account_id, national_number))
                .hasMessage("Source account does not exist");

    }

    @Test
    void not_transfer_money_if_destination_account_does_not_exist() {

    }


    @Test
    void not_transfer_money_if_amount_is_zero() {

    }

    @Test
    void not_transfer_money_if_source_account_has_insufficient_balance() {

    }

    @Test
    void not_transfer_money_if_source_and_destination_accounts_are_the_same() {

    }


    @Test
    void not_transfer_money_if_source_account_is_not_active() {

    }

    @Test
    void not_transfer_money_if_destination_account_is_not_active() {

    }


    @Test
    void update_source_account_balance_and_update_destination_account_balance_update_transfer_history() {
        // Given
        // When
        // Then
    }
}