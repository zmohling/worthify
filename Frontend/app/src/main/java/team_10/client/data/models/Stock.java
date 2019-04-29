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

        Transaction t = new Transaction(value, amount, 1, transactionId, recurring, d);
        t.setAccount(this);
        transactions.put(d, t);
    }

    public double getValue(LocalDate d) {

        if (!transactions.isEmpty()) {

            Set<LocalDate> dateSet = transactions.keySet();

            LocalDate startDate = transactions.firstKey();

            LocalDate endDate = null;

            Double val;

            Iterator<LocalDate> localDateIterator = dateSet.iterator();
            while (localDateIterator.hasNext()) {
                LocalDate temp = localDateIterator.next();

                if (temp.isBefore(d)) {
                    startDate = temp;
                } else if (temp.isAfter(d)) {
                    endDate = temp;
                } else if (temp.isEqual(d)) {
                    if (transactions.get(temp).getValue() == null) {
                        return 0.0;
                    } else return transactions.get(temp).getValue();
                }
            }
            if (endDate == null)
                if (transactions.get(startDate).getValue() == null) {
                    return 0.0;
                } else return transactions.get(startDate).getValue();
            else
                val = (startDate.until(d, ChronoUnit.DAYS) <= d.until(endDate, ChronoUnit.DAYS))
                        ?transactions.get(startDate).getValue()
                        : transactions.get(endDate).getValue();
                if(val == null) {
                    return 0;
                } else return val;

        }

        return 0; // if all else fails
    }


    public team_10.client.data.models.Transaction getTransaction(LocalDate date) {
        if (date != null && transactions.containsKey(date)) {
            return transactions.get(date);
        } else if (date != null){
            Transaction t = new Transaction(0, 0, 0, transactions.size(), 0, date);
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

        public int visibility; // for populated transactions (i.e. a daily high stock value)

        Transaction() { }

        Transaction(double value, int amount, int visibility, int transactionID, int recurring, LocalDate date) {
            this.value = value;
            this.amount = amount;
            this.visibility = visibility;
            this.transactionID = transactionID;
            this.recurring = recurring;
            this.date = date;
        }
    }
}
