package com.example.demo.service.impl;


import com.example.demo.model.Transaction;
import com.example.demo.repasitory.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    @Override
    public Charge charge(Transaction transaction) throws StripeException {

        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("source", transaction.getStripeToken());
        chargeParams.put("amount", transaction.getAmount());
        chargeParams.put("currency", "USD");
        Charge charge = Charge.create(chargeParams);
        transactionRepository.save(transaction);
        return charge;
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        return transactions;
    }
}
