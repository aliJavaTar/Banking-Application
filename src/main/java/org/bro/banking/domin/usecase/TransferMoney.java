package org.bro.banking.domin.usecase;

import org.bro.banking.domin.Accounts;

public class TransferMoney {
    private final Accounts accounts;

    public TransferMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void transferToAccount(long accountId, String nationalNumber) {
        // Implement transfer logic here
        // Check if source account exists, destination account exists, amount is not zero,
        // source account has sufficient balance, source and destination accounts are not the same,
        // source account is active, destination account is active,
        // update source account balance, update destination account balance,
        // and update transfer history
        throw new IllegalArgumentException("Source account does not exist");
    }
}
