package com.bankstatementaggregator.services;

import com.bankstatementaggregator.models.Branch;
import com.bankstatementaggregator.models.Company;
import com.bankstatementaggregator.models.Transaction;
import com.bankstatementaggregator.models.User;
import com.bankstatementaggregator.utils.DummyDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankstatementaggregator.models.BankStatement;
import com.bankstatementaggregator.repositories.BankStatementRepository;
import com.bankstatementaggregator.repositories.CompanyRepository;
import com.bankstatementaggregator.repositories.TransactionRepository;
import com.bankstatementaggregator.repositories.BranchRepository;
import com.bankstatementaggregator.repositories.UserRepository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BankStatementService {

    private static final String STATEMENTS_DIR = "C:\\Users\\pothi\\OneDrive\\Documents\\BankStatements";

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AWSService awsService;

    public String generateBankStatement(Long userId, Long companyId, Long branchId, int transactionCount, boolean deleteAfterUpload) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new IllegalArgumentException("Invalid company ID"));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new IllegalArgumentException("Invalid branch ID"));

        List<Transaction> transactions = DummyDataGenerator.generateDummyTransactions(transactionCount, company.getCompanyName(), branch.getBranchName());

        String fileName = "company_" + companyId + "_user_" + userId + "_" + System.currentTimeMillis() + ".csv";
        String filePath = STATEMENTS_DIR + "/" + fileName;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("transaction_id,date,amount,description,company_name");
            for (Transaction transaction : transactions) {
                writer.printf("%s,%s,%.2f,%s,%s%n",
                        transaction.getTransactionId(),
                        transaction.getDate(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getCompanyName());
            }
        }

        String fileUrl = awsService.uploadFile(filePath, fileName);

        BankStatement bankStatement = new BankStatement();
        bankStatement.setUser(user);
        bankStatement.setCompany(company);
        bankStatement.setBranch(branch);
        bankStatement.setStatementDate(new Date());
        bankStatement.setStatementData(fileUrl);

        bankStatementRepository.save(bankStatement);

        // Delete the temporary file after upload if flag is set
        if (deleteAfterUpload) {
            new File(filePath).delete();
        }

        return fileUrl;
    }

    public void parseAndSaveTransactions(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records.subList(1, records.size())) { // Skipping header
                Transaction transaction = new Transaction();
                transaction.setTransactionId(record[0]);
                transaction.setDate(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(record[1]));
                transaction.setAmount(Double.parseDouble(record[2]));
                transaction.setDescription(record[3]);
                transaction.setCompanyName(record[4]);
                transactionRepository.save(transaction);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
