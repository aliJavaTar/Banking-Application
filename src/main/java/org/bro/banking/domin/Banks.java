package org.bro.banking.domin;

import java.util.Optional;

public interface Banks {
    boolean existByName(String bankName);

    Optional<Bank> getById(long bankId);
//    Optional<Bank> getById(long id);
}
