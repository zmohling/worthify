package team_10.client.object.account;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Transaction implements Serializable {
    protected double value;
    protected int transactionID;
    protected transient Account account;
    protected int recurring;
    protected LocalDate date;

    public Transaction() {
    }

    public Transaction(double value, int transactionID, int recurring, LocalDate date) {
        this.value = value;
        this.transactionID = transactionID;
        this.recurring = recurring;
        this.date = date;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getID() {
        return transactionID;
    }

    public void setValue(int transactionID) { this.transactionID = transactionID; }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getRecurring() { return recurring; }

    public void setRecurring(int recurring) { this.recurring = recurring; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }
}