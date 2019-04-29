package team_10.client.data.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Set;

import team_10.client.data.UserInputField;

public class Stock extends Account {

    @UserInputField(
            priority = 1,
            name = "Ticker",
            inputType = String.class
    )
    public String ticker;

    public Stock() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String symbol) {
        this.ticker = symbol;
    }

    public void addTransaction(LocalDate d, double value, int amount) {
        addTransaction(d, amount, 0, value, transactions.size());
    }

    public void addTransaction(LocalDate d, int amount, int recurring, double value, int transactionId) {

        Transaction t = new Transaction(value, amount, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    public double getValue(LocalDate d) {

        if (!transactions.isEmpty()) {

            Set<LocalDate> dateSet = transactions.keySet();

            LocalDate startDate = transactions.firstKey();

            LocalDate endDate = null;

            /* THIS IS CAUSING NULLPOINTER EXCEPTION BECAUSE VALUES AREN"T BEING *
             * STORED PROPERLY WHEN VALUES ARE FETCHED                           */
            try {
                Iterator<LocalDate> localDateIterator = dateSet.iterator();
                while (localDateIterator.hasNext()) {
                    LocalDate temp = localDateIterator.next();

                    if (temp.isBefore(d)) {
                        startDate = temp;
                    } else if (temp.isAfter(d)) {
                        endDate = temp;
                    } else if (temp.isEqual(d)) {
                        return transactions.get(temp).getValue();
                    }
                }

                if (endDate == null)
                    return transactions.get(startDate).getValue();
                else
                    return (startDate.until(d, ChronoUnit.DAYS) <= d.until(endDate, ChronoUnit.DAYS))
                            ? transactions.get(startDate).getValue()
                            : transactions.get(endDate).getValue();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return 0;
            }
        }

        return 0; // if all else fails
    }


    public team_10.client.data.models.Transaction getTransaction(LocalDate date) {
        if (date != null && transactions.containsKey(date)) {
            return transactions.get(date);
        } else if (date != null){
            Transaction t = new Transaction(0, 0, transactions.size(), 0, date);
            t.setVisibility(1);

            transactions.put(date, t);

            return transactions.get(date);
        } else {
            return new Transaction();
        }
    }




    private class Transaction extends team_10.client.data.models.Transaction {

        @UserInputField(
                priority = 0,
                name = "Amount",
                inputType = Integer.class
        )
        public int amount;

        Transaction() { }

        Transaction(double value, int amount, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.amount = amount;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }
    }
}
