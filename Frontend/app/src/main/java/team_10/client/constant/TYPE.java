package team_10.client.constant;

import java.util.ArrayList;
import java.util.List;

import team_10.client.object.account.Account;

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
