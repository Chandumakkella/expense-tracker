package com.expensetracker.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.expensetracker.Entity.Transaction;
import com.expensetracker.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public Transaction addTransaction(Transaction transaction) {
        return repo.save(transaction);
    }

    public List<Transaction> getMonthlySummary(int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return repo.findByDateBetween(start, end);
    }

    public void importFromFile(MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Transaction t = new Transaction();
            t.setType(parts[0].trim());
            t.setCategory(parts[1].trim());
            t.setAmount(Double.parseDouble(parts[2].trim()));
            t.setDate(LocalDate.parse(parts[3].trim()));
            repo.save(t);
        }
    }
}

