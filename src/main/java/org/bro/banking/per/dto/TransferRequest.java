package org.bro.banking.per.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequest {
    private long sourceAccountId;
    private long destinationAccountId;
    private BigDecimal amount;

    public TransferRequest() {
    }

    public TransferRequest(long sourceAccountId, long destinationAccountId, BigDecimal amount) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
    }


}
