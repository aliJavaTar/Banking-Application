package org.bro.banking.infrastructure.persistence.repo.imp;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domin.account.Accounts;
import org.bro.banking.infrastructure.persistence.repo.AccountRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AccountJpaRepositoryImpl implements Accounts {
    private final AccountRepository accountRepository;

    @Override
    public boolean isExist(String nationalCode, long bankId) {
        return accountRepository.existsByNationalCodeAndBankId(nationalCode, bankId);
    }
}
