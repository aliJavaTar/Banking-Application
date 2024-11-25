package org.bro.banking.domin.usecase;

import org.assertj.core.api.Assertions;
import org.bro.banking.domin.Account;
import org.bro.banking.domin.Accounts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransferMoneyShould {

    @Mock
    private Accounts accounts;

    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_and_destination_accounts_are_the_same() {

        long accountId = 0;
        long destinationAccountId = 0;
        BigDecimal amount = new BigDecimal("0");

        var transfer = new TransferMoney(accounts);


        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(accountId, destinationAccountId, amount))
                .hasMessage("Source and destination accounts are the same");
    }



    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_does_not_exist() {

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.empty());

        long sourceAccountId = 1;

        long destinationAccountId = 0;
        BigDecimal amount = new BigDecimal("0");

        var transfer = new TransferMoney(accounts);



        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(sourceAccountId, destinationAccountId, amount))
                .hasMessage("Source account does not exist");

    }

    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_destination_account_does_not_exist() {
        var account = new Account(BigDecimal.ONE);
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.empty());


        long sourceAccountId = 1;
        long destinationAccountId = 0;
        BigDecimal amount = new BigDecimal("0");


        var transfer = new TransferMoney(accounts);


        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(sourceAccountId, destinationAccountId, amount))
                .hasMessage("destination  account does not exist");

    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_amount_is_zero() {
        var account = new Account(BigDecimal.ONE);
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));

        // Given
        long sourceAccountId = 1;

        long destinationAccountId = 0;
        BigDecimal amount = new BigDecimal("0");
        // When
        var transfer = new TransferMoney(accounts);


        // Then
        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(sourceAccountId, destinationAccountId, amount))
                .hasMessage("Amount must be greater than zero");
    }

    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_has_insufficient_balance() {
        var account = new Account(BigDecimal.TEN);
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));

        // Given
        long sourceAccountId = 1;

        long destinationAccountId = 0;
        final BigDecimal greatThanAmount = new BigDecimal("12");
        // When
        var transfer = new TransferMoney(accounts);


        // Then
        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(sourceAccountId, destinationAccountId, greatThanAmount))
                .hasMessage("Insufficient funds: Your account balance is too low to complete this transaction");


        Assertions.assertThatThrownBy(() -> transfer.transferToAccount(sourceAccountId, destinationAccountId, BigDecimal.TEN))
                .hasMessage("Insufficient funds: Your account balance is too low to complete this transaction");
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