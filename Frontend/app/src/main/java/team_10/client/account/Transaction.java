package team_10.client.account;

public abstract class Transaction {
    double value;
    String transactionID;

    public Transaction() {}
    public Transaction(double value) { this.value = value; }

    public double getValue() {
        return this.value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public String getID() { return transactionID; }
    public void setValue(String transactionID) { this.transactionID = transactionID; }
}