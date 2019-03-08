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
    protected String id;
    protected String label;
    protected TreeMap<LocalDate, team_10.client.account.Transaction> transactions;

    public Account() {
        transactions = new TreeMap<>();
    }

    public Account(String id, String label) {
        this.id = id;
        this.label = label;

        transactions = new TreeMap<>();
    }

    public void addTransaction(LocalDate d, double amount) {
        Transaction t = new Transaction(amount);
        transactions.put(d, t);
    }

    public Transaction removeTransaction(LocalDate d) {
        return (Transaction) transactions.remove(d);
    }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    public String getLabel() { return this.label; }
    public void setLabel(String label) { this.label = label; }

    public abstract double getValue(LocalDate d);




    private class Transaction extends team_10.client.account.Transaction {
        double amount;

        protected Transaction() { }
        protected Transaction(double amount) {
            this.amount = amount;
        }


        public double getAmount() {
            return this.amount;
        }
        public void setAmount(double amount) { this.amount = amount; }
    }
}

