package com.bankstatementaggregator.services;

import com.bankstatementaggregator.models.Branch;
import com.bankstatementaggregator.models.Company;
import com.bankstatementaggregator.repositories.BranchRepository;
import com.bankstatementaggregator.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public Branch createBranch(Branch branch) {
        Company company = companyRepository.findById(branch.getCompany().getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found"));
        branch.setCompany(company);
        return branchRepository.save(branch);
    }

    public Branch getBranchById(Long branchId) {
        return branchRepository.findById(branchId).orElse(null);
    }

    public List<Branch> getBranchesByCompanyId(Long companyId) {
        return branchRepository.findByCompanyCompanyId(companyId);
    }
    
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
}
