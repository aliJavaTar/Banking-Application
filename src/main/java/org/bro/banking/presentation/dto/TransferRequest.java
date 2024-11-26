package org.bro.banking.presentation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.math.BigDecimal;

@Getter
@Valid
public class TransferRequest {
    @NotNull(message = "Sender account ID cannot be null")
    @Min(value = 1, message = "Sender account ID is not a valid")
    private long sourceAccountId;
    @NotNull(message = "Receiver account ID cannot be null")
    @Min(value = 1, message = "Receiver account ID is not a valid")
    private long destinationAccountId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "5.0", message = "Amount must be greater than five")
    private BigDecimal amountToTransfer;

    public TransferRequest() {
    }

    public TransferRequest(long sourceAccountId, long destinationAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amountToTransfer = amount;
    }
}
