package org.bro.banking.infrastructure.persistence.repo.imp;

import lombok.RequiredArgsConstructor;
import org.bro.banking.domin.bank.Bank;
import org.bro.banking.domin.bank.Banks;
import org.bro.banking.infrastructure.persistence.repo.BankRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BankJpaRepositoryImpl implements Banks {

    private final BankRepository bankJpaRepository;

    @Override
    public boolean existByNameAndBranchCodeOrCode(String name, int branchCode, String code) {
        return false;
    }

    @Override
    public Optional<Bank> getById(long bankId) {
        return Optional.empty();
    }

    @Override
    public void addBank(String name, int breachCode, String spacialNumber) {

    }
}
