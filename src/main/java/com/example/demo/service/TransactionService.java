package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import java.util.List;

public interface TransactionService {

    Charge charge(Transaction transaction) throws StripeException;

    List<Transaction> getTransactions();
}
