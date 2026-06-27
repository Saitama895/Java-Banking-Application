package domain;

import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private Double amount;
    private String id;
    private String note;
    private LocalDateTime timeStamp;
    private TransactionType type;

    public Transaction(String accountNumber, Double amount, String id, String note, LocalDateTime timeStamp, TransactionType type) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.id = id;
        this.note = note;
        this.timeStamp = timeStamp;
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
