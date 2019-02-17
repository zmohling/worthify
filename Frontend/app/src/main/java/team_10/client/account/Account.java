package team_10.client.account;

import java.time.LocalDate;

public abstract class Account
{
    protected int id;

    public Account(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public abstract double getValue(LocalDate d);


    abstract class Transaction {
        double amount;

        Transaction(double amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return this.amount;
        }
    }
}

