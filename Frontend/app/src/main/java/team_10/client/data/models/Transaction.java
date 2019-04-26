package team_10.client.data.models;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Transaction class that each account type implements.
 */
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

    /**
     * Gets value of the transaction.
     * @return value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets value of the transaction.
     * @param value value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Gets id of the transaction.
     * @return transaction ID
     */
    public int getID() {
        return transactionID;
    }

    /**
     * Sets the value of the transaction.
     * @param transactionID transaction to set value for
     */
    public void setValue(int transactionID) { this.transactionID = transactionID; }

    /**
     * Gets the transactions account.
     * @return account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Sets the transactions account.
     * @param account account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Gets whether the transaction is recurring.
     * @return recurring
     */
    public int getRecurring() { return recurring; }

    /**
     * Sets whether the transaction is recurring.
     * @param recurring recurring
     */
    public void setRecurring(int recurring) { this.recurring = recurring; }

    /**
     * Gets the date of the transaction.
     * @return date
     */
    public LocalDate getDate() { return date; }

    /**
     * Sets the date of teh transaction.
     * @param date date
     */
    public void setDate(LocalDate date) { this.date = date; }

}