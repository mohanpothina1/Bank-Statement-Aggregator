package com.bankstatementaggregator.models;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;

    @Column(nullable = false)
    private String branchName;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BankStatement> bankStatements;

    public Branch() {
    }

    public Branch(Long branchId) {
        this.branchId = branchId;
    }

    // Getters and setters

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<BankStatement> getBankStatements() {
        return bankStatements;
    }

    public void setBankStatements(Set<BankStatement> bankStatements) {
        this.bankStatements = bankStatements;
    }
}
