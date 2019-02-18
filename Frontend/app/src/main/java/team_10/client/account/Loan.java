package team_10.client.account;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

public class Loan extends Account implements Serializable {

    public Loan() {

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
                double principle = total + ((Transaction) transactions.get(fromDate)).getAmount();
                double rate = ((Transaction) transactions.get(fromDate)).getInterestRate();
                long n = fromDate.until(toDate, ChronoUnit.DAYS); //number of compounding periods (DAYS) per unit t
                double t = n / (double) fromDate.lengthOfYear();

                total = principle * Math.pow(1 + (rate / n), (n * t));
            }
        }

        return total;
    }

    /* Overloaded addTransaction method */
    public void addTransaction(LocalDate d, double amount, double interestRate)
    {
        Transaction t = new Transaction(amount, interestRate);
        transactions.put(d, t);
    }

    protected class Transaction extends team_10.client.account.Transaction {

        double interestRate;

        Transaction(double amount, double interestRate)
        {
            this.amount = amount;
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