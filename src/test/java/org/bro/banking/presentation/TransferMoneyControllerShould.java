package org.bro.banking.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.bro.banking.domain.account.usecase.TransferMoney;
import org.bro.banking.presentation.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class TransferMoneyControllerShould {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Accounts accounts;

    private TransferMoney transferMoney;

    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-11-25T00:00:00Z"), ZoneId.of("UTC"));
//        transferMoney = new TransferMoney(accounts);
    }

    @Test
    void shouldReturnOkWhenTransferIsSuccessful() throws Exception {
        BigDecimal sourceAmount = new BigDecimal("100");
        BigDecimal destinationAmount = new BigDecimal("50");
        BigDecimal amountToTransfer = new BigDecimal("50");

        Account sourceAccount = new Account(1L, sourceAmount, getExpiredDatePlusDays(), clock);
        Account destinationAccount = new Account(2L, destinationAmount, getExpiredDatePlusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(sourceAccount));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(destinationAccount));

        TransferRequest request = new TransferRequest(1L, 2L, amountToTransfer);

        mockMvc.perform(put("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transfer successful"));
    }

    @Test
    void shouldReturnOkWhenTransferIsSuccessful_() throws Exception {
        BigDecimal sourceAmount = new BigDecimal("1");
        BigDecimal destinationAmount = new BigDecimal("5");
        BigDecimal amountToTransfer = new BigDecimal("5088");

        Account sourceAccount = new Account(1L, sourceAmount, getExpiredDatePlusDays(), clock);
        Account destinationAccount = new Account(2L, destinationAmount, getExpiredDatePlusDays(), clock);

        when(accounts.getByIdAndUsername(anyLong(), anyString())).thenReturn(Optional.of(sourceAccount));
        when(accounts.getById(anyLong())).thenReturn(Optional.of(destinationAccount));

        TransferRequest request = new TransferRequest(1L, 2L, amountToTransfer);

        mockMvc.perform(put("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Insufficient funds: Your account balance is too low to complete this transaction"));

    }

    @Test
    void shouldReturnBadRequestWhenAmountIsZero() throws Exception {

        TransferRequest request = new TransferRequest(1L, 2L, BigDecimal.ZERO);

        mockMvc.perform(put("/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    private LocalDate getExpiredDateMinusDays() {
        return LocalDate.now(clock).minusDays(1);
    }

    private LocalDate getExpiredDatePlusDays() {
        return LocalDate.now(clock).plusDays(1);
    }
}
