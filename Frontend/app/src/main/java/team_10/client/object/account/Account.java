package team_10.client.object.account;

import android.content.Context;
import android.view.View;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeMap;

/**
 * Abstract Account class for all account types. All transactions for all types that extend
 * this class are put in the transactions TreeMap.
 */
public abstract class Account implements Serializable {
    protected String accountID, label;
    protected TreeMap<LocalDate, team_10.client.object.account.Transaction> transactions;
    protected transient Context context;

    /**
     * Default constructor
     */
    public Account() {
        this.transactions = new TreeMap<>();
    }

    /**
     * Constructor with context for api accounts.
     * @param context context account is in
     */
    public Account(Context context) {
        transactions = new TreeMap<>();
        this.context = context;
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
        t.setAccount(this);
        transactions.put(d, t);
    }

    /**
     * Remove a transaction from the list.
     * @param d date
     * @return the removed transaction
     */
    public Transaction removeTransaction(LocalDate d) {
        return (Transaction) transactions.remove(d);
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

    /**
     * Gets the value of the account.
     * @param d date
     * @return double value
     */
    public abstract double getValue(LocalDate d);

    /**
     * Returns the view the account was made in.
     * @param context current context
     * @return View the account was made in
     */
    public abstract View getView(Context context);


    private class Transaction extends team_10.client.object.account.Transaction {
        private Transaction(double value, int transactionID, int recurring, LocalDate date) {
            super(value, transactionID, recurring, date);
        }
    }
}

