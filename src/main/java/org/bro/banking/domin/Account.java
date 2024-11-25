package org.bro.banking.domin;

import java.math.BigDecimal;

public class Account {
    private final BigDecimal amount;

    public Account(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
