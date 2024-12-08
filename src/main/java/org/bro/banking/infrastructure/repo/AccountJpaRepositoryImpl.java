package org.bro.banking.infrastructure.repo;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domain.account.Account;
import org.bro.banking.domain.account.Accounts;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountJpaRepositoryImpl implements Accounts {



    @Override
    public Optional<Account> getByIdAndUsername(long accountId, String username) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> getById(long id) {
        return Optional.empty();
    }

    @Override
    public void updateAccount(Account account) {

    }
}
