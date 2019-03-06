package com.serverApp.serverApp.models;

import javax.persistence.*;
import java.io.Serializable;

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
}
