package com.bankstatementaggregator.repositories;

import com.bankstatementaggregator.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByCompanyCompanyId(Long companyId);
}
