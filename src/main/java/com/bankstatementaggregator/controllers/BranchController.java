package com.bankstatementaggregator.controllers;

import com.bankstatementaggregator.models.Branch;
import com.bankstatementaggregator.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping("/create")
    public Branch createBranch(@RequestBody Branch branch) {
        return branchService.createBranch(branch);
    }

    @GetMapping("/{id}")
    public Branch getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }
    
    @GetMapping("/all")
    public List<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/company/{companyId}")
    public List<Branch> getBranchesByCompanyId(@PathVariable Long companyId) {
        return branchService.getBranchesByCompanyId(companyId);
    }
}
