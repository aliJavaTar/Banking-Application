package org.bro.banking.domain.account;

import java.util.Optional;

public interface Accounts {
    Optional<Account> getByIdAndUsername(long accountId, String username);

    Optional<Account> getById(long id);

    void updateAccount(Account account);
}
