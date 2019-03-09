package team_10.client.account;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.TreeMap;

/**
 * Abstract Account class for all account types. All transactions for all types that extend
 * this class are put in the transactions TreeMap.
 */
public abstract class Account implements Serializable
{
    protected String accountID, label;
    protected TreeMap<LocalDate, team_10.client.account.Transaction> transactions;

    public Account() {
        transactions = new TreeMap<>();
    }

    public Account(String accountID, String label) {
        this.accountID = accountID;
        this.label = label;

        transactions = new TreeMap<>();
    }

    public void addTransaction(LocalDate d, double value) {
        Transaction t = new Transaction(value);
        transactions.put(d, t);
    }

    public Transaction removeTransaction(LocalDate d) {
        return (Transaction) transactions.remove(d);
    }

    public String getID() { return this.accountID; }
    public void setID(String accountID) { this.accountID = accountID; }
    public String getLabel() { return this.label; }
    public void setLabel(String label) { this.label = label; }

    public abstract double getValue(LocalDate d);




    private class Transaction extends team_10.client.account.Transaction {
        private Transaction(double value) { super(value); }
    }
}

