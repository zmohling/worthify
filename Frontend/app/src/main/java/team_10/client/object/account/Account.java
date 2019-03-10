package team_10.client.object.account;

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
    protected TreeMap<LocalDate, team_10.client.object.account.Transaction> transactions;

    public Account() {
        transactions = new TreeMap<>();
    }

    public void addTransaction(LocalDate d, double value) {
        Transaction t = new Transaction(value, transactions.size());
        transactions.put(d, t);
    }

    public void addTransaction(LocalDate d, double value, int transactionID) {
        Transaction t = new Transaction(value, transactionID);
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




    private class Transaction extends team_10.client.object.account.Transaction {
        private Transaction(double value, int transactionID) {
            super(value, transactionID);
        }
    }
}
