package org.bro.banking.domin;

import java.util.Optional;

public interface Banks {
    Optional<Bank> getById(long id);
}
