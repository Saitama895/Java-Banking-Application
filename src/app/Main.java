package app;

import exceptions.ValidationException;
import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();
        boolean running = true;

        System.out.println("Welcome to Console Bank");
        while (running){
            System.out.println("""
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) Account Statement
                6) List Account
                7) Search Acount by Customer Name
                8) Exit
                """);

            System.out.print("CHOOSE: ");
            String choice = scanner.nextLine().trim();
            System.out.println("CHOICE: " + choice);

            switch (choice) {
                case "1" -> openAccount(scanner, bankService);
                case "2" -> deposit(scanner, bankService);
                case "3" -> withdraw(scanner, bankService);
                case "4" -> transfer(scanner, bankService);
                case "5" -> statement(scanner, bankService);
                case "6" -> listAccounts(bankService);
                case "7" -> searchAccounts(scanner, bankService);
                case "8" -> running = false;
            }
        }

    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.println("Customer name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Customer email: ");
        String email = scanner.nextLine().trim();
        System.out.println("Account Type (SAVINGS / CURRENT): ");
        String type = scanner.nextLine().trim();
        System.out.println("Initial deposit (optional, blank for 0): ");
        String amountStr = scanner.nextLine().trim();

        if (amountStr.isBlank()) amountStr = "0";
        double initial = Double.parseDouble(amountStr);

        String accountNumber = bankService.openAccount(name, email, type);
        if (initial > 0) {
            bankService.deposit(accountNumber, initial, "Initial Deposit");
            System.out.println("Account Opened: " + accountNumber);
        }
        else {
            throw new ValidationException("Please enter a valid amount");
        }
    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("Enter the amount to be deposited: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        bankService.deposit(accountNumber, amount, "Deposit");

        System.out.println("Successfully deposited");
    }

    private static void withdraw(Scanner scanner, BankService bankService) {
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.print("Enter the amount to be deposited: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        bankService.withdraw(accountNumber, amount, "Withdraw");

        System.out.println("Successfully Withdrawn");
    }

    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.print("Sender's Account: ");
        String sender = scanner.nextLine().trim();
        System.out.print("Sender's Account: ");
        String receiver = scanner.nextLine().trim();
        System.out.print("Amount: ");
        Double amount = Double.valueOf(scanner.nextLine().trim());

        bankService.transfer(sender, receiver, amount, "Transfer");

        System.out.println("Successfully Transferred: " + amount);
    }

    private static void statement(Scanner scanner, BankService bankService) {
        System.out.print("Account Number: ");
        String accountNumber = scanner.nextLine().trim();
        bankService.getStatement(accountNumber).forEach(t -> {
            System.out.println(t.getTimeStamp() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote());
                }
        );
    }

    private static void listAccounts(BankService bankService) {
        bankService.listAccounts().forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getAccountType() + " | " + a.getBalance());
        });
    }

    private static void searchAccounts(Scanner scanner, BankService bankService) {
        System.out.print("Customer name contains: ");
        String name = scanner.nextLine().trim();

        bankService.searchAccountByName(name).forEach(account ->
                System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance())
        );
    }

}