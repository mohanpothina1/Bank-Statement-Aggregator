package com.bankstatementaggregator.utils;

import com.bankstatementaggregator.models.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class DummyDataGenerator {

    private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TRANSACTION_ID_LENGTH = 8;
    private static final Random RANDOM = new Random();

    public static List<Transaction> generateDummyTransactions(int count, String companyName, String branchName) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            transactions.add(new Transaction(
                generateTransactionId(), // Generate a simple transaction ID
                new Date(), // Current date
                Math.random() * 1000, // Random amount
                "Transaction from " + branchName + " branch", // Description
                companyName // Company name
            ));
        }
        return transactions;
    }

    private static String generateTransactionId() {
        StringBuilder transactionId = new StringBuilder(TRANSACTION_ID_LENGTH);
        for (int i = 0; i < TRANSACTION_ID_LENGTH; i++) {
            transactionId.append(ALPHANUMERIC_CHARACTERS.charAt(RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length())));
        }
        return transactionId.toString();
    }
}
