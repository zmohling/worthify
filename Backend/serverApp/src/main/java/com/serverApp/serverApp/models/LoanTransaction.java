package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "accounts")
public class LoanTransaction implements Serializable{
    private static final long serialVersionUID = 4L;

    @Id
    private long id;

    @Column(name = "date")
    private java.sql.Date date;

    @Column(name = "interestRate")
    private double interestRate;

    @Column(name = "amount")
    private double amount;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
