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

        Account sourceAccount = accounts.getByIdAndUsername(request.getSourceAccountId(), username.toString())
                .orElseThrow(() -> new AccountDoesNotExist("Source"));

        Account destinationAccount = accounts.getById(request.getDestinationAccountId())
                .orElseThrow(() -> new AccountDoesNotExist("Destination"));

        validation(request, sourceAccount, destinationAccount);

        sourceAccount.updateBalancesForTransfer(destinationAccount, request.getAmountToTransfer());

        accounts.updateAccount(sourceAccount);
        accounts.updateAccount(destinationAccount);
    }


    private void validation(TransferRequest request, Account sourceAccount, Account destinationAccount) {
        sourceAccount.checkForSameAccount(request.getDestinationAccountId());
        sourceAccount.hasSufficientFunds(request.getAmountToTransfer());
        sourceAccount.checkAccountExpiry();
        destinationAccount.checkAccountExpiry();
    }
}
