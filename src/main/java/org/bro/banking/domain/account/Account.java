package org.bro.banking.domain.account;

import lombok.Getter;
import lombok.Setter;
import org.bro.banking.domain.account.exption.InsufficientFundsException;
import org.bro.banking.domain.account.exption.SameSourceAndDestinationAccountException;
import org.bro.banking.domain.exception.CustomExcepting;
import org.bro.banking.domain.exception.ErrorType;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;


public class Account {
    @Getter
    private final long id;
    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    private final LocalDate expiredTime;
    private final Clock clock;


    public Account(long id, BigDecimal amount, LocalDate expiredTime, Clock clock) {
        this.id = id;
        this.amount = amount;
        this.expiredTime = expiredTime;
        this.clock = clock;
    }

    public void updateBalancesForTransfer(Account destinationAccount, BigDecimal amount) {
        this.setAmount(this.getAmount().subtract(amount));
        destinationAccount.setAmount(destinationAccount.getAmount().add(amount));
    }


    public void checkAccountExpiry() {
        if (this.isExpired())
            throw new CustomExcepting(ErrorType.DESTINATION_OR_SOURCE_ACCOUNT_EXPIRED);

    }

    public void hasSufficientFunds(BigDecimal amountToTransfer) {
        if (this.amount.compareTo(amountToTransfer) <= 0)
            throw new InsufficientFundsException();
    }

    public void checkForSameAccount(long id) {
        if (this.id == id)
            throw new SameSourceAndDestinationAccountException();
    }

    private boolean isExpired() {
        return LocalDate.now(clock).isAfter(expiredTime);
    }


}
