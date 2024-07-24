// src/main/java/com/example/aggregator/repositories/BankStatementRepository.java
package com.bankstatementaggregator.repositories;

import com.bankstatementaggregator.models.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
}
