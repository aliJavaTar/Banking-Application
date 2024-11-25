package org.bro.banking.presentation.dto;

import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
public class TransferRequest {
    private long sourceAccountId;
    private long destinationAccountId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "5", message = "Amount must be greater than fiver")
    private BigDecimal amountToTransfer;

    public TransferRequest() {
    }

    public TransferRequest(long sourceAccountId, long destinationAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amountToTransfer = amount;
    }


}
