package org.bro.banking.domin;

import java.util.Optional;

public interface Accounts {
    Optional<Account> getByIdAndUsername(long accountId, String username);

    Optional<Account> getById(long id);
}
