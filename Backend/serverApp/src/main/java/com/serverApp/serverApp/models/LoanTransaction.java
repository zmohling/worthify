package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;

@Entity
@Table(name = "loanTransactions")
public class LoanTransaction implements Serializable{
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "accountsId")
    private String accountsId;

    @Column(name = "date")
    private java.sql.Date date;

    @Column(name = "interestRate")
    private double interestRate;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction")
    private Blob transaction;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(String accountsId) {
        this.accountsId = accountsId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Blob getTransaction() {
        return transaction;
    }

    public void setTransaction(Blob transaction) {
        this.transaction = transaction;
    }
}

