package team_10.client.account;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class Loan extends Account {

    private Map<LocalDate, Transaction> transactions;

    public Loan(int id)
    {
        super(id);
        this.transactions = new TreeMap<LocalDate, Transaction>();

    }

    public double getValue(LocalDate d) {
        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());

        double total = 0;

        if (transaction_dates.size() <= 0) {
            throw new IllegalStateException("No transactions for this account.");
        } else {

            for (int i = 0; i < transaction_dates.size(); i++) {

                LocalDate fromDate = transaction_dates.get(i);
                LocalDate toDate;

                if ((i+1) >= transaction_dates.size() || transaction_dates.get(i + 1).isAfter(d)) {
                    toDate = d;
                    i = Integer.MAX_VALUE - 1; // Stop calculating if date d is before last transaction
                } else {
                    toDate = transaction_dates.get(i + 1);
                }

                //A = P(1 + r/n)^nt -> Daily Compound Interest
                double principle = total + transactions.get(fromDate).getAmount();
                double rate = transactions.get(fromDate).getInterestRate();
                long n = fromDate.until(toDate, ChronoUnit.DAYS); //number of compounding periods (DAYS) per unit t
                double t = n / (double) fromDate.lengthOfYear();

                total = principle * Math.pow(1 + (rate / n), (n * t));
            }
        }

        return total;
    }

    public void addTransaction(LocalDate date, double value, double interestRate) {

        Transaction t = new Transaction(value, interestRate);

        transactions.put(date, t);
    }

    private class Transaction extends Account.Transaction {
        double interestRate;

        private Transaction(double amount, double interestRate) {
            super(amount);
            this.interestRate = interestRate;
        }

        public double getAmount() {
            return this.amount;
        }

        public double getInterestRate() {
            return this.interestRate;
        }
    }
}