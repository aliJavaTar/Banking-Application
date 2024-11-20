package org.bro.banking.domin.bank;

import java.util.Optional;

public interface Banks {
    boolean existByNameAndBranchCodeOrCode(String name, int branchCode,String code);

    Optional<Bank> getById(long bankId);

    void addBank(String name, int breachCode, String spacialNumber);
//    Optional<Bank> getById(long id);
}
