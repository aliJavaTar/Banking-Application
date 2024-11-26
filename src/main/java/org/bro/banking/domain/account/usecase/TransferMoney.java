package org.bro.banking.domain.account.usecase;

import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.bro.banking.domain.account.exption.AccountDoesNotExist;
import org.bro.banking.presentation.dto.TransferRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TransferMoney {
    private final Accounts accounts;


    public TransferMoney(Accounts accounts) {
        this.accounts = accounts;
    }

    public void processTransfer(TransferRequest request) {

        Object username = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var sourceAccount = getAccountByIdAndUsername(request.getSourceAccountId(), username.toString());

        var destinationAccount = getReceiverAccountById(request.getDestinationAccountId());

        validation(request, sourceAccount, destinationAccount);

        sourceAccount.updateBalancesForTransfer(destinationAccount, request.getAmountToTransfer());

        accounts.updateAccount(sourceAccount);
        accounts.updateAccount(destinationAccount);
    }

    private Account getAccountByIdAndUsername(long accountId, String username) {
        return accounts.getByIdAndUsername(accountId, username).orElseThrow(() -> new AccountDoesNotExist("Source"));
    }

    private Account getReceiverAccountById(long accountId) {
        return accounts.getById(accountId).orElseThrow(() -> new AccountDoesNotExist("Destination"));
    }

    private void validation(TransferRequest request, Account sourceAccount, Account destinationAccount) {
        sourceAccount.checkForSameAccount(request.getDestinationAccountId());
        sourceAccount.hasSufficientFunds(request.getAmountToTransfer());
        sourceAccount.checkAccountExpiry();
        destinationAccount.checkAccountExpiry();
    }
}
