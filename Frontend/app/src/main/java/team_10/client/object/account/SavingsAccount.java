package team_10.client.object.account;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Vector;

import team_10.client.R;
import team_10.client.utility.General;

/**
 * Savings Account account type.
 */
public class SavingsAccount extends Account {

    public SavingsAccount() {
    }

    /**
     * Overloaded addTransaction method for Loan.
     *
     * @param d                   Date of transaction.
     * @param value               Value of transaction, aka change of principal value. (negative or positive)
     * @param annualPercentReturn Interest rate at the time of the transaction.
     */
    public void addTransaction(LocalDate d, double value, double annualPercentReturn, int recurring) {
        addTransaction(d, value, annualPercentReturn, transactions.size(), recurring);
    }

    public void addTransaction(LocalDate d, double value, double annualPercentReturn, int transactionID, int recurring) {
        Transaction t = new Transaction(General.round(value, 2), General.round(annualPercentReturn, 3), transactionID, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    /**
     * Get the value of this Loan at a specified date.
     *
     * @param d LocalDate which to calculate value.
     * @return Value at LocalDate <code>d</code>
     */
    public double getValue(LocalDate d) {
        Vector<LocalDate> transaction_dates = new Vector<LocalDate>(transactions.keySet());

        double total = 0;

        if (transaction_dates.size() <= 0) {
            return 0.0;
        } else {
            for (int i = 0; i < transaction_dates.size(); i++) {

                LocalDate fromDate = transaction_dates.get(i);
                LocalDate toDate;

                if ((i + 1) >= transaction_dates.size() || transaction_dates.get(i + 1).isAfter(d)) {
                    toDate = d;
                    i = Integer.MAX_VALUE - 1; // Stop calculating if date d is before last transaction
                } else {
                    toDate = transaction_dates.get(i + 1);
                }

                //A = P(1 + r/n)^nt -> Daily Compound Interest
                double principle = total + ((Transaction) transactions.get(fromDate)).getValue();
                double rate = ((Transaction) transactions.get(fromDate)).getAnnualPercentReturn();
                long n = fromDate.until(toDate, ChronoUnit.DAYS); //number of compounding periods (DAYS) per unit t
                double t = n / (double) fromDate.lengthOfYear();

                total = principle * Math.pow(1 + (rate / n), (n * t));
            }
        }

        return General.round(total, 2); // round to nearest cent
    }

    @Override
    public View getView(Context context) {
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.item_string_input_view, null);

        // Fill in the details
        TextView textView = (TextView) v.findViewById(R.id.item_string_input_view_TITLE);
        textView.setText("Label:");

        EditText editText = (EditText) v.findViewById(R.id.item_string_input_view_INPUT);
        editText.setHint((label == null) ? "" : label);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                label = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return v;
    }


    /**
     * Loan specific Transaction object.
     */
    private class Transaction extends team_10.client.object.account.Transaction {

        double annualPercentReturn;

        Transaction(double value, double annualPercentReturn, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.annualPercentReturn = annualPercentReturn;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }

        public double getAnnualPercentReturn() {
            return this.annualPercentReturn;
        }

        public void setAnnualPercentReturn(double interestRate) {
            this.annualPercentReturn = interestRate;
        }
    }
}
