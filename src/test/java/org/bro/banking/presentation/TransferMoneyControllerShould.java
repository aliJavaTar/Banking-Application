package org.bro.banking.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.bro.banking.presentation.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class TransferMoneyControllerShould {
    private static final String PATH = "/api/transfer";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Accounts accounts;

    private static Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
    }


    @Test
    void whenTransferaseSuccessFullShowOk() throws Exception {


        Account sourceAccount = new Account(1L, BigDecimal.TEN.add(BigDecimal.TEN), getExpiredDatePlusDays(), clock);
        Account destinationAccount = new Account(2L, BigDecimal.TWO, getExpiredDatePlusDays(), clock);

        mockAccountRetrieval(sourceAccount, destinationAccount);

        var request = new TransferRequest(1L, 2L, BigDecimal.TEN);

        performTransferRequest(request)
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    void when_dont_have_SufficientFunds_show_badRequest() throws Exception {


        Account sourceAccount = new Account(1L, BigDecimal.ONE, getExpiredDatePlusDays(), clock);
        Account destinationAccount = new Account(2L, BigDecimal.TWO, getExpiredDatePlusDays(), clock);
        mockAccountRetrieval(sourceAccount, destinationAccount);

        var request = new TransferRequest(1L, 2L, BigDecimal.TEN);


        performTransferRequest(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Insufficient funds: Your account balance is too low to complete this transaction"));

    }


    @ParameterizedTest
    @MethodSource("provideTestData")
    void notTransferMoney_whenTimeExpired(Account sourceAccount, Account destinationAccount, String exceptionMessage) throws Exception {

        var transferRequest = new TransferRequest(2L, 5L, new BigDecimal("6.0"));

        mockAccountRetrieval(sourceAccount, destinationAccount);

        performTransferRequest(transferRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorType").value(exceptionMessage));
    }


    @ParameterizedTest
    @MethodSource("provideInvalidDateRequests")
    void shouldReturnBadRequestForInvalidTransferRequests(
            TransferRequest request, String expectedErrorMessage) throws Exception {


        Account sourceAccount = new Account(1L, BigDecimal.TEN, getExpiredDatePlusDays(), clock);
        Account destinationAccount = new Account(2L, BigDecimal.ZERO, getExpiredDatePlusDays(), clock);

        mockAccountRetrieval(sourceAccount, destinationAccount);


        performTransferRequest(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value(expectedErrorMessage));

    }

    static Stream<Arguments> provideTestData() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
        return Stream.of(
                Arguments.of(
                        new Account(1L, BigDecimal.TEN, getExpiredDateMinusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDatePlusDays(), clock),
                        "DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED"
                ),
                Arguments.of(
                        new Account(1L, BigDecimal.TEN, getExpiredDatePlusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDateMinusDays(), clock),
                        "DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED"
                ),
                Arguments.of(
                        new Account(1L, BigDecimal.TEN, getExpiredDateMinusDays(), clock),
                        new Account(2L, BigDecimal.TWO, getExpiredDateMinusDays(), clock),
                        "DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED"
                )
        );
    }

    static Stream<Arguments> provideInvalidDateRequests() {
        return Stream.of(
                Arguments.of(
                        new TransferRequest(1L, 2L, new BigDecimal("-1")),
                        "amountToTransfer: Amount must be greater than five"
                ),
                Arguments.of(
                        new TransferRequest(1L, 2L, null),
                        "amountToTransfer: Amount must not be null"
                ),
                Arguments.of(
                        new TransferRequest(0L, 2L, BigDecimal.TEN),
                        "sourceAccountId: Sender account ID is not a valid"
                ),
                Arguments.of(
                        new TransferRequest(2L, 0L, BigDecimal.TEN),
                        "destinationAccountId: Receiver account ID is not a valid"
                )
        );
    }

    private void mockAccountRetrieval(Account sourceAccount, Account destinationAccount) {
        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(sourceAccount));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(destinationAccount));
    }

    private ResultActions performTransferRequest(TransferRequest request) throws Exception {
        return mockMvc.perform(put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }


    private static LocalDate getExpiredDateMinusDays() {
        return LocalDate.now(clock).minusDays(1);
    }

    private static LocalDate getExpiredDatePlusDays() {
        return LocalDate.now(clock).plusDays(1);
    }
}



