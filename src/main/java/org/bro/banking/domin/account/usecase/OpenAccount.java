package org.bro.banking.domin.account.usecase;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domin.Banks;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.presentation.openaccountdto.CartResponse;
import org.bro.banking.presentation.openaccountdto.OpenAccountRequest;

@RequiredArgsConstructor
public class OpenAccount {
    private final Accounts accounts;
    private final Banks banks;

    public CartResponse open(OpenAccountRequest request) {
        boolean exist = accounts.isExist(request.getNationalCode(), request.getNameOfBank());
        if (exist)
            throw new IllegalArgumentException("you have an account already ");




        return null;
    }
}
