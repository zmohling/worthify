package team_10.client.object.account;

public abstract class Transaction {
    double value;
    int transactionID;

    public Transaction() {}
    public Transaction(double value, int transactionID) {
        this.value = value;
        this.transactionID = transactionID;
    }

    public double getValue() {
        return this.value;
    }
    public void setValue(double value) { this.value = value; }
    public int getID() { return transactionID; }
    public void setValue(int transactionID) { this.transactionID = transactionID; }
}