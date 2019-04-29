package team_10.client.data.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeMap;

import team_10.client.data.UserInputField;

/**
 * Abstract Account class for all account types. All transactions for all types that extend
 * this class are put in the transactions TreeMap.
 */
public abstract class Account implements Serializable {
    @UserInputField(
            priority = 0,
            name = "Label",
            inputType = String.class
    )
    protected String label;

    protected String accountID;
    protected int isActive;
    protected TreeMap<LocalDate, team_10.client.data.models.Transaction> transactions;

    /**
     * Default constructor
     */
    public Account() {
        this.transactions = new TreeMap<>();
    }

    /**
     * Add a transaction to account
     * @param d date
     * @param value value
     * @param recurring whether it is recurring
     */
    public void addTransaction(LocalDate d, double value, int recurring) {
        addTransaction(d, value, transactions.size(), recurring);
    }

    /**
     * Add transaction to transactions list.
     * @param d date
     * @param value value
     * @param transactionID ID of transaction
     * @param recurring whether it is recurring
     */
    public void addTransaction(LocalDate d, double value, int transactionID, int recurring) {
        Transaction t = new Transaction(value, transactionID, recurring, d);
        addTransaction(t);
    }

    public void addTransaction(team_10.client.data.models.Transaction t) {
        t.setAccount(this);
        transactions.put(t.date, t);
    }

    /**
     * Remove a transaction from the list.
     * @param d date
     * @return the removed transaction
     */
    public team_10.client.data.models.Transaction removeTransaction(LocalDate d) {
        return transactions.remove(d);
    }

    /**
     * Set the transactions.
     * @param tempTransactions new transactions to be set to
     */
    public void setTransactions(TreeMap tempTransactions) { this.transactions = tempTransactions; }

    /**
     * Gets the transactions of the account.
     * @return TreeMap of transactions
     */
    public TreeMap getTransactions() { return this.transactions; }

    /**
     * Gets the ID of the account.
     * @return String accountID
     */
    public String getID() {
        return this.accountID;
    }

    /**
     * Sets the ID of an account.
     * @param accountID accounts ID
     */
    public void setID(String accountID) {
        this.accountID = accountID;
    }

    /**
     * Gets the label of the account.
     * @return String label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Sets the label of the account.
     * @param label label to set as
     */
    public void setLabel(String label) {
        this.label = label;
    }

    public int isActive() {
        return this.isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public abstract team_10.client.data.models.Transaction getTransaction(LocalDate date);

    /**
     * Gets the value of the account.
     * @param d date
     * @return double value
     */
    public abstract double getValue(LocalDate d);

    protected class Transaction extends team_10.client.data.models.Transaction {

        Transaction() { }

        Transaction(double value, int transactionID, int recurring, LocalDate date) {
            super(value, transactionID, recurring, date);
        }
    }
}

