package team_10.client.account;

public abstract class Transaction {
    double amount;

    public Transaction() {}

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}