package org.bro.banking.domin.bank.usecase;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domin.bank.Banks;

@RequiredArgsConstructor
public class SigningBank {
    private final Banks banks;

    public void register(String name, int branchCode, String specificNumber) {
        String fourthUniqueCode = specificNumber.substring(0, 4);
        if (banks.existByNameAndBranchCodeOrCode(name, branchCode, fourthUniqueCode))
            throw new IllegalArgumentException("this bank already exists");
    }
}
