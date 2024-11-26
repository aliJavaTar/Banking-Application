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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

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

    private static Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
        transfer = new TransferMoney(accounts);
    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_and_destination_accounts_are_the_same() {


        mockAccounts(getAccount(1L, BigDecimal.TWO), getAccount(1L, BigDecimal.ZERO));

        Assertions.assertThatThrownBy(() -> {
                    var request = new TransferRequest(1L, 1L, BigDecimal.TEN);
                    transfer.processTransfer(request);
                })
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

        Account account = getAccount(0, BigDecimal.ONE);
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(account));
        when(accounts.getById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> transferRequest(41, 12, BigDecimal.ZERO))
                .isInstanceOf(AccountDoesNotExist.class)
                .hasMessage("Destination account does not exist");

    }


    @ParameterizedTest
    @MethodSource("provideInsufficientFundsTestData")
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_has_insufficient_balance(BigDecimal transferAmount) {

        Account resiverAccount = getAccount(2, BigDecimal.TEN);
        Account senderAccount = getAccount(1, BigDecimal.TEN);
        mockAccounts(senderAccount, resiverAccount);

        Assertions.assertThatThrownBy(() -> transferRequest(senderAccount.getId(), resiverAccount.getId(), transferAmount))
                .hasMessage("Insufficient funds: Your account balance is too low to complete this transaction");
    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void not_transfer_money_if_source_account_is_not_active() {
        var senderAccount = new Account(1L, BigDecimal.TEN, getExpiredDateMinusDays(), clock);
        var resiverAccount = new Account(4L, BigDecimal.TEN, getExpiredDateMinusDays(), clock);

        mockAccounts(senderAccount, resiverAccount);

        Assertions.assertThatThrownBy(() -> transferRequest(2, 0, BigDecimal.TEN))
                .isInstanceOf(CustomExcepting.class);
    }

//    @Test
//    @WithMockUser(username = "alien", password = "1234")
//    void not_transfer_money_if_destination_account_is_not_active() {
//        var senderAccount = new Account(0, BigDecimal.TEN, getExpiredDateMinusDays(), clock);
//        var resiverAccount = new Account(0, BigDecimal.TEN, getExpiredDateMinusDays(), clock);
//
//        mockAccounts(senderAccount, resiverAccount);
//
//        Assertions.assertThatThrownBy(() -> transferRequest(2, 0, BigDecimal.TEN))
//                .isInstanceOf(CustomExcepting.class);
//    }


    @Test
    @WithMockUser(username = "alien", password = "1234")
    void update_source_account_balance_and_update_destination_account_balance_update_transfer_history() {
        BigDecimal sourceAmount = new BigDecimal(100);
        BigDecimal destinationAmount = new BigDecimal(50);

        var senderAccount = getAccount(1L, sourceAmount);
        var resiverAccount = getAccount(2L, destinationAmount);

        mockAccounts(senderAccount, resiverAccount);

        transferRequest(1, 2, new BigDecimal(50));


        Assertions.assertThat(senderAccount.getAmount()).isEqualTo(new BigDecimal(50));
        Assertions.assertThat(resiverAccount.getAmount()).isEqualTo(new BigDecimal(100));

        verify(accounts, times(1)).updateAccount(resiverAccount);
        verify(accounts, times(1)).updateAccount(senderAccount);
    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    @WithMockUser(username = "alien", password = "1234")
    void notTransferMoney_whenTimeExpired(Account sourceAccount, Account destinationAccount) {
        var transferRequest = new TransferRequest(2L, 5L, new BigDecimal("6.0"));
        mockAccounts(sourceAccount, destinationAccount);
        Assertions.assertThatThrownBy(() -> transfer.processTransfer(transferRequest))
                .isInstanceOf(CustomExcepting.class);
    }

    static Stream<Arguments> provideInsufficientFundsTestData() {
        return Stream.of(
                Arguments.of(BigDecimal.TEN),
                Arguments.of(BigDecimal.TEN.add(BigDecimal.TEN)),
                Arguments.of((Object) null)
        );
    }

    static Stream<Arguments> provideTestData() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
        return Stream.of(
                Arguments.of(new Account(1L, BigDecimal.TEN, getExpiredDateMinusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDatePlusDays(), clock)),

                Arguments.of(new Account(1L, BigDecimal.TEN, getExpiredDatePlusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDateMinusDays(), clock)),

                Arguments.of(new Account(1L, BigDecimal.TEN, getExpiredDateMinusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDateMinusDays(), clock)));


    }


    private Account getAccount(long id, BigDecimal amount) {
        return new Account(id, amount, getExpiredDatePlusDays(), clock);
    }

    private void mockAccounts(Account senderAccount, Account reciverAccount) {
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(senderAccount));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(reciverAccount));
    }


    private static LocalDate getExpiredDateMinusDays() {
        return LocalDate.now(clock).minusDays(1);
    }

    private static LocalDate getExpiredDatePlusDays() {
        return LocalDate.now(clock).plusDays(1);
    }

    private void transferRequest(long sourceAccountId, long destinationAccountId, BigDecimal amount) {
        var request = new TransferRequest(sourceAccountId, destinationAccountId, amount);
        transfer.processTransfer(request);
    }

}