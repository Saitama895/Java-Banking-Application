package service;

import domain.Account;
import domain.Transaction;

import java.util.List;

public interface BankService {
    String openAccount(String name, String email, String accountType);

    List <Account> listAccounts();

    void deposit(String accountNumber, double amount, String Note);

    void withdraw(String accountNumber, double amount, String withdraw);

    void transfer(String sender, String receiver, Double amount, String transfer);

    List<Transaction> getStatement(String accountNumber);

    List<Account> searchAccountByName(String name);
}


//Modular design