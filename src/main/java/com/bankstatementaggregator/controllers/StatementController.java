package com.bankstatementaggregator.controllers;

import com.bankstatementaggregator.services.BankStatementService;
import com.bankstatementaggregator.services.AWSService;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/statements")
public class StatementController {

    @Autowired
    private BankStatementService bankStatementService;

    @Autowired
    private AWSService awsService;

    @PostMapping("/generate")
    public String generateStatement(@RequestParam Long userId, @RequestParam Long companyId, @RequestParam Long branchId, 
                                    @RequestParam int transactionCount, @RequestParam boolean deleteAfterUpload) {
        try {
            return bankStatementService.generateBankStatement(userId, companyId, branchId, transactionCount, deleteAfterUpload);
        } catch (IOException e) {
            return "Error generating bank statement: " + e.getMessage();
        }
    }

    @GetMapping("/download")
    public String downloadStatement(@RequestParam String key) {
        try {
            awsService.downloadFileToLocal(key);
            return "File downloaded successfully to your Downloads folder.";
        } catch (IOException e) {
            return "Error downloading bank statement: " + e.getMessage();
        }
    }

    @PostMapping("/parse")
    public String parseStatement(@RequestParam String filePath) {
        try {
            bankStatementService.parseAndSaveTransactions(filePath);
            return "Transactions parsed and saved successfully!";
        } catch (IOException | CsvException e) {
            return "Error parsing transactions: " + e.getMessage();
        }
    }
}
