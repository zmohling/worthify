package com.serverApp.serverApp.models;


import org.hibernate.type.BlobType;

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

    @Column(name = "accountID")
    private String accountID;

    @Column(name = "label")
    private String label;

    @Column(name = "type")
    private String type;

    @Column(name = "isActive")
    private int isActive;

    @Column(name = "transactions")
    private String transactions;

    @Column(name = "recurring")
    private int recurring;

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
        return accountID;
    }

    public void setAccountId(String accountId) {
        this.accountID = accountId;
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

    public int getRecurring() {
        return recurring;
    }

    public void setRecurring(int recurring) {
        this.recurring = recurring;
    }
}
