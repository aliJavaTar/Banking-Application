package org.bro.banking.domin;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;


public class Account {
    @Getter
    private final BigDecimal amount;
    @Getter
    private final LocalDate expiredTime;
    private final Clock clock;


    public Account(BigDecimal amount, LocalDate expiredTime, Clock clock) {
        this.amount = amount;
        this.expiredTime = expiredTime;
        this.clock = clock;
    }


    public boolean isExpired() {
        return LocalDate.now(clock).isAfter(expiredTime);
    }
}
