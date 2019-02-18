package team_10.client.account;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public abstract class Account
{
    protected int id;
    public TreeMap<LocalDate, team_10.client.account.Transaction> transactions;

    public Account() {
        transactions = new TreeMap<>();
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void addTransaction(LocalDate d, double amount) {
        Transaction t = new Transaction(amount);
        transactions.put(d, t);
    }

    public abstract double getValue(LocalDate d);

    protected class Transaction extends team_10.client.account.Transaction {
        double amount;

        protected Transaction(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }
}

