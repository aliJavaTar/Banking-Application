package org.bro.banking.infrastructure.persistence.repo;

import org.bro.banking.infrastructure.persistence.model.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<BankEntity, Long> {
}
