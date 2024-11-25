package org.bro.banking.domain.account.usecase;

import org.assertj.core.api.Assertions;
import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.bro.banking.domain.account.exption.AccountDoesNotExist;
import org.bro.banking.domain.account.exption.SameSourceAndDestinationAccountException;
import org.bro.banking.domain.exception.CustomExcepting;
import org.bro.banking.presentation.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransferMoneyShould {

    @Mock
    private Accounts accounts;

    private TransferMoney transfer;

    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
        transfer = new TransferMoney(accounts);
    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_and_destination_accounts_are_the_same() {
        Account account = new Account(0, BigDecimal.ZERO, LocalDate.now(), clock);
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() -> transfer.processTransfer(new TransferRequest()))
                .isInstanceOf(SameSourceAndDestinationAccountException.class)
                .hasMessage("Source and destination accounts are the same");
    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_does_not_exist() {

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> transferRequest(1, 0, BigDecimal.ZERO))
                .isInstanceOf(AccountDoesNotExist.class)
                .hasMessage("Source account does not exist");

    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_destination_account_does_not_exist() {

        var account = new Account(0, BigDecimal.ONE, getExpiredDatePlusDays(), clock);
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> transferRequest(41, 12, BigDecimal.ZERO))
                .isInstanceOf(AccountDoesNotExist.class)
                .hasMessage("Destination account does not exist");

    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_has_insufficient_balance() {

        var source = new Account(1, BigDecimal.TEN, getExpiredDatePlusDays(), clock);

        var account = new Account(2, BigDecimal.TEN, getExpiredDatePlusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(source));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() -> transferRequest(source.getId(), account.getId(), BigDecimal.TEN))
                .hasMessage("Insufficient funds: Your account balance is too low to complete this transaction");

    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_is_not_active() {
        var account = new Account(0, BigDecimal.TEN, getExpiredDateMinusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() -> transferRequest(2, 0, BigDecimal.TEN))
                .isInstanceOf(CustomExcepting.class);
    }

    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_destination_account_is_not_active() {
        var account = new Account(0, BigDecimal.TEN, getExpiredDateMinusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(account));

        Assertions.assertThatThrownBy(() -> transferRequest(2, 0, BigDecimal.TEN))
                .isInstanceOf(CustomExcepting.class);
    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void update_source_account_balance_and_update_destination_account_balance_update_transfer_history() {
        BigDecimal sourceAmount = new BigDecimal(100);
        BigDecimal destinationAmount = new BigDecimal(50);

        var sourceAccount = new Account(1, sourceAmount, getExpiredDatePlusDays(), clock);
        var destinationAccount = new Account(2, destinationAmount, getExpiredDatePlusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(sourceAccount));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(destinationAccount));

        transferRequest(1, 2, new BigDecimal(50));


        Assertions.assertThat(sourceAccount.getAmount()).isEqualTo(new BigDecimal(50));
        Assertions.assertThat(destinationAccount.getAmount()).isEqualTo(new BigDecimal(100));

        verify(accounts, times(1)).updateAccount(destinationAccount);
        verify(accounts, times(1)).updateAccount(sourceAccount);
    }

    private LocalDate getExpiredDateMinusDays() {
        return LocalDate.now(clock).minusDays(1);
    }

    private LocalDate getExpiredDatePlusDays() {
        return LocalDate.now(clock).plusDays(1);
    }

    private void transferRequest(long sourceAccountId, long destinationAccountId, BigDecimal amount) {
        var request = new TransferRequest(sourceAccountId, destinationAccountId, amount);
        transfer.processTransfer(request);
    }

}