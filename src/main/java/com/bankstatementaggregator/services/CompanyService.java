package com.bankstatementaggregator.services;

import com.bankstatementaggregator.models.Company;
import com.bankstatementaggregator.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public String createCompany(Company company) {
        Optional<Company> existingCompany = companyRepository.findByCompanyName(company.getCompanyName());
        if (existingCompany.isPresent()) {
            return "Company with name '" + company.getCompanyName() + "' already exists.";
        }
        companyRepository.save(company);
        return "Company created successfully!";
    }

    public Company getCompanyById(Long companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}
