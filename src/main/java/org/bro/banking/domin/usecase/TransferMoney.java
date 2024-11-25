package org.bro.banking.domin.usecase;

import org.bro.banking.domin.Account;
import org.bro.banking.domin.Accounts;
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
            throw new IllegalArgumentException("Source and destination accounts are the same");
        }

        Account sourceAccount = accounts.getByIdAndUsername(sourceAccountId, username.toString())
                .orElseThrow(() -> new IllegalArgumentException("Source account does not exist"));

        accounts.getById(destinationAccountId)
                .orElseThrow(() -> new IllegalArgumentException("destination  account does not exist"));

        if (amount.intValue() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (sourceAccount.getAmount().compareTo(amount) <= 0) {
            throw new IllegalArgumentException("Insufficient funds: Your account balance is too low to complete this transaction");
        }


    }
}
