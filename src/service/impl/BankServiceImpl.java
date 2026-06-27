package service.impl;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.TransactionType;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.ValidationException;
import repository.AccountRepository;
import repository.CustomerRepository;
import repository.TransactionRepository;
import service.BankService;
import util.Validation;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    private final Validation<String> validateName = name -> {
        if (name == null || name.isBlank()) throw new ValidationException("Name is required");
    };

    private final Validation<String> validateEmail = email -> {
        if (email == null || !email.contains("@")) throw new ValidationException("Email is required");
    };

    private final Validation<String> validateType = type -> {
        if (type == null || !type.equalsIgnoreCase("SAVINGS") && !type.equalsIgnoreCase("CURRENT")) {
            throw new ValidationException("Type must be (SAVINGS or CURRENT)");
        }
    };

    private final Validation<Double> validateAmountPositive = amount -> {
        if (amount == null || amount < 0)
            throw new ValidationException("Please enter a valid amount");
    };

    @Override
    public String openAccount(String name, String email, String accountType) {
        validateName.validate(name);
        validateEmail.validate(email);
        validateType.validate(accountType);

        String customerId = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber(); //Method get String is called to get the number through size +1

        Customer c = new Customer( customerId, name, email);
        customerRepository.save(c);

        Account account = new Account(accountNumber, accountType, (double) 0, customerId);
        accountRepository.save(account);
        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findALl().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, double amount, String note) {
        validateAmountPositive.validate(amount);

        // to deposit we need the account object for particular accNumber so below
        // and also checking if null
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account Not Found: " + accountNumber));
        account.setBalance(account.getBalance() + amount);

        // After it would be deposited so we need to maintain this transaction
        // i.e. store it for the particular accountNum
        Transaction transaction = new Transaction(account.getAccountNumber(),
                amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), TransactionType.DEPOSIT);
        transactionRepository.add(transaction);

    }

    @Override
    public void withdraw(String accountNumber, double amount, String note) {

        // to withdraw we need the account object for particular accNumber so below
        // and also checking if null
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account Not Found: " + accountNumber));

        // We will first check if the amount to withdraw is present first present in the account or not.
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient Balance");
        }
        account.setBalance(account.getBalance() - amount);

        // After it would be deposited so we need to maintain this transaction
        // i.e. store it for the particular accountNum
        Transaction transaction = new Transaction(account.getAccountNumber(),
                amount, UUID.randomUUID().toString(), note, LocalDateTime.now(), TransactionType.WITHDRAW);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String sender, String receiver, Double amount, String note) {

        //first check if the sender and receiver are same if same then throw error.
        if (sender.equals(receiver))
            throw new RuntimeException("Cannot transfer to your own account");
        //2nd check if sender and receiver acc exist if it doesn't throw error.
        Account senderAccount = accountRepository.findByNumber(sender)
                .orElseThrow(() -> new AccountNotFoundException("Account not Found: " + sender));
        Account receiverAccount = accountRepository.findByNumber(receiver)
                .orElseThrow(() -> new AccountNotFoundException("Account not Found: " + receiver));
        //3rd if sender has less balance then the account to be sent then throw the error
        //as Insufficient balance
        if(senderAccount.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient Balance");

        senderAccount.setBalance(senderAccount.getBalance() - amount);
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        //Now we will add this transaction history to its specified acc Num.
        transactionRepository.add(new Transaction(sender, amount, UUID.randomUUID().toString(),
                        note, LocalDateTime.now(), TransactionType.TRANSFER_OUT));
        transactionRepository.add(new Transaction(receiver, amount, UUID.randomUUID().toString(),
                note, LocalDateTime.now(), TransactionType.TRANSFER_IN));

    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        return transactionRepository.findByAccount(accountNumber).stream()
                .sorted(Comparator.comparing(Transaction::getTimeStamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountByName(String name) {
        String customerName = (name == null) ? "" : name.toLowerCase();
//        List<Account> result = new ArrayList<>();
//        System.out.println(customerName);
//        System.out.println("Printing the list in searchAccount");
//
//        for (Customer c : customerRepository.findAll()) {
//            if (c.getName().toLowerCase().contains(customerName)){
//                result.addAll(accountRepository.findByCustomerId(c.getId()));
//            }
//        }
//        System.out.println(result);
//        result.sort(Comparator.comparing(Account::getAccountNumber));
//        return result;

        return customerRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(customerName))
                .flatMap(c -> accountRepository.findByCustomerId(c.getId()).stream())
                .collect(Collectors.toList());
    }

    private String getAccountNumber() {
        int size = accountRepository.findALl().size() + 1;
        return String.format("AC%06d", size);
    }
}