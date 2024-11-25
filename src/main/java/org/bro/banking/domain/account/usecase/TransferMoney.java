package org.bro.banking.domain.account.usecase;

import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.bro.banking.domain.account.exption.AccountDoesNotExist;
import org.bro.banking.domain.account.exption.AmountMustBeGreaterThanZeroException;
import org.bro.banking.domain.account.exption.InsufficientFundsException;
import org.bro.banking.domain.account.exption.SameSourceAndDestinationAccountException;
import org.bro.banking.domain.exception.CustomExcepting;
import org.bro.banking.domain.exception.ErrorType;
import org.bro.banking.per.dto.TransferRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

public class TransferMoney {
    private final Accounts accounts;


    public TransferMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void transferToAccount(TransferRequest request) {

        long sourceAccountId = request.getSourceAccountId();
        long destinationAccountId = request.getDestinationAccountId();
        BigDecimal amount = request.getAmount();

        Object username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (destinationAccountId == sourceAccountId) {
            throw new SameSourceAndDestinationAccountException();
        }

        Account sourceAccount = accounts.getByIdAndUsername(sourceAccountId, username.toString())
                .orElseThrow(() -> new AccountDoesNotExist("Source"));

        Account destinationAccount = accounts.getById(destinationAccountId)
                .orElseThrow(() -> new AccountDoesNotExist("Destination"));

        if (sourceAccount.isExpired()) {
            throw new CustomExcepting(ErrorType.SOURCE_ACCOUNT_EXPIRED);
        }

        if (destinationAccount.isExpired()) {
            throw new CustomExcepting(ErrorType.DESTINATION_ACCOUNT_EXPIRED);
        }

        if (amount.intValue() <= 0) {
            throw new AmountMustBeGreaterThanZeroException();
        }

        if (sourceAccount.getAmount().compareTo(amount) <= 0) {
            throw new InsufficientFundsException();
        }

    }
}
