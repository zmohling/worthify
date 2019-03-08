package com.serverApp.serverApp.models;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;


@Entity
@Table(name = "accounts")
public class Accounts implements Serializable{
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "accountId")
    private String accountId;

    @Column(name = "label")
    private String label;

    @Column(name = "type")
    private String type;

    @Column(name = "isActive")
    private int isActive;

    @Column(name = "transactions")
    private String transactions;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTransactions(String transactions) {
        this.transactions = transactions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getTransactions() {
        return transactions;
    }

    public void setTransaction(String transactions) {
        this.transactions = transactions;
    }
}

