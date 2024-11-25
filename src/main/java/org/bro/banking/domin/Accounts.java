package org.bro.banking.domin;

import java.util.Optional;

public interface Accounts {
    Optional<Account> getAccountByIdAndNationalNumber(long accountId, String nationalNumber);
}
