package com.serverApp.serverApp.models;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;


@Entity
@Table(name = "accounts")
public class Accounts implements Serializable{
    private static final long serialVersionUID = 4L;

    @Id
    private String id;

    @Column(name = "label")
    private String label;

    @Column(name = "type")
    private String type;

    @Column(name = "isActive")
    private int isActive;

    @Column(name = "transactions")
    private Blob transactions;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Blob getTransactions() {
        return transactions;
    }

    public void setTransaction(Blob transactions) {
        this.transactions = transactions;
    }
}

