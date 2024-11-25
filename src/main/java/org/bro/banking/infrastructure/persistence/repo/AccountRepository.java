package org.bro.banking.infrastructure.persistence.repo;

import org.bro.banking.infrastructure.persistence.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
