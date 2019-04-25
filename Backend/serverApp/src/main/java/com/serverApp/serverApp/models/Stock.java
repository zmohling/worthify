package com.serverApp.serverApp.models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing the stocks table
 *
 * @author Michael Davis
 */
@Entity
@Table(name = "stocks")
public class Stock implements Serializable {
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "accountID")
    private String accountID;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "dailyVal")
    private ArrayList<Double> dailyVal;

    @Column(name = "dailyDate")
    private ArrayList<Double> dailyDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
