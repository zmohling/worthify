package team_10.client.data.models;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Vector;

import team_10.client.R;
import team_10.client.object.account.UserInputField;
import team_10.client.utility.General;

public class CertificateOfDeposit extends Account {

    @UserInputField(
            priority = 1,
            name = "Maturity Date",
            inputType = LocalDate.class
    )
    protected LocalDate maturityDate;

    public CertificateOfDeposit() {
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
        if (d.isAfter(maturityDate)) {
            d = maturityDate;
        }
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

    public void setMaturityDate(LocalDate d) {
        this.maturityDate = d;
    }

    public LocalDate getMaturityDate() {
        return this.maturityDate;
    }

    @Override
    public View getView(final Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Label input
        View v1 = vi.inflate(R.layout.item_string_input_view, null);
        TextView v1_textView = (TextView) v1.findViewById(R.id.item_string_input_view_TITLE);
        v1_textView.setText("Label:");
        EditText v1_editText = (EditText) v1.findViewById(R.id.item_string_input_view_INPUT);
        v1_editText.setHint((label == null) ? "" : label);
        linearLayout.addView(v1);

        v1_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                label = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        // Maturity Date input
        View v2 = vi.inflate(R.layout.item_date_input_view, null);
        TextView v2_textView = (TextView) v2.findViewById(R.id.item_date_input_view_TITLE);
        v2_textView.setText("Maturity Date:");
        final EditText v2_editText = (EditText) v2.findViewById(R.id.item_date_input_view_INPUT);
        v2_editText.setHint((maturityDate == null) ? "Click to Add" : maturityDate.toString());
        linearLayout.addView(v2);

        // Date Picker
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                maturityDate = LocalDate.of(year, monthOfYear, dayOfMonth);
                v2_editText.setHint(maturityDate.toString());
            }

        };

        v2_editText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        return linearLayout;
    }


    /**
     * Loan specific Transaction object.
     */
    private class Transaction extends team_10.client.data.models.Transaction {

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
