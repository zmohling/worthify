package team_10.client.constant;

import java.util.ArrayList;
import java.util.List;

import team_10.client.object.account.Account;

/**
 * Enum for types of accounts.
 */
public enum TYPE {
    CERTIFICATEOFDEPOSIT("Certificate of Deposit", "CertificateOfDeposit"),
    LOAN("Loan", "Loan"),
    SAVINGSACCOUNT("Savings Account", "SavingsAccount"),
    STOCK("Stock", "Stock");

    private String simpleName;
    private String className;

    TYPE(String simpleName, String className) {
        this.simpleName = simpleName;
        this.className = className;
    }

    /**
     * Gets the type of the account.
     * @return the type's account class
     */
    public Class<Account> getTypeClass() {
        String p = "team_10.client.object.account.";
        Class<Account> c = null;

        try {
            c = (Class<Account>) Class.forName(p + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return c;
    }

    /**
     * Finds type that matches.
     * @param s
     * @return the type that matches s in the types list
     */
    public static TYPE firstMatch(String s) {
        for (TYPE t : TYPE.values()) {
            if (t.simpleName.equals(s)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return simpleName;
    }

    /**
     * Gets all types and finds there strings.
     * @return all types of accounts as a list of strings
     */
    public static List<String> getAllAsStrings() {
        ArrayList<String> types = new ArrayList<>();
        TYPE[] t = TYPE.values();

        int i;
        for (i = 0; i < t.length; i++) {
            types.add(t[i].toString());
        }

        return types;
    }
}
