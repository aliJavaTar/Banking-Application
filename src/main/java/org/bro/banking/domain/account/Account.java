package org.bro.banking.domain.account;

import lombok.Getter;
import org.bro.banking.domain.account.exption.InsufficientFundsException;
import org.bro.banking.domain.account.exption.SameSourceAndDestinationAccountException;
import org.bro.banking.domain.exception.CustomExcepting;
import org.bro.banking.domain.exception.ErrorType;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;


public class Account {
    @Getter
    private long id;
    @Getter
    private final BigDecimal amount;
    @Getter
    private final LocalDate expiredTime;
    private final Clock clock;


    public Account(long id, BigDecimal amount, LocalDate expiredTime, Clock clock) {
        this.id = id;
        this.amount = amount;
        this.expiredTime = expiredTime;
        this.clock = clock;
    }

    public void timeValidation()
    {
        if( this.isExpired())
            throw new CustomExcepting(ErrorType.DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED);

    }

    public void validationEnoughMoney(BigDecimal amountToTransfer) {
        if (this.amount.compareTo(amountToTransfer) <= 0)
            throw new InsufficientFundsException();
    }

    public void validationTransfer(long id) {
        if (this.id == id)
            throw new SameSourceAndDestinationAccountException();
    }

    private boolean isExpired() {
        return LocalDate.now(clock).isAfter(expiredTime);
    }


}
