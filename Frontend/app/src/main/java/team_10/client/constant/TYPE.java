package team_10.client.constant;

import java.util.ArrayList;
import java.util.List;

import team_10.client.data.models.Account;
import team_10.client.data.models.CertificateOfDeposit;
import team_10.client.data.models.Loan;
import team_10.client.data.models.RealEstate;
import team_10.client.data.models.SavingsAccount;
import team_10.client.data.models.Stock;

/**
 * Enum for types of accounts.
 */
public enum TYPE {
    CERTIFICATEOFDEPOSIT("Certificate of Deposit", CertificateOfDeposit.class),
    LOAN("Loan", Loan.class),
    SAVINGSACCOUNT("Savings Account", SavingsAccount.class),
    REALESTATE("Real Estate", RealEstate.class),
    STOCK("Stock", Stock.class);

    private String simpleName;
    private Class<? extends Account> clazz;

    TYPE(String simpleName, Class<? extends Account> clazz) {
        this.simpleName = simpleName;
        this.clazz = clazz;
    }

    /**
     * Gets the type of the account.
     * @return the type's account class
     */
    public Class<Account> getTypeClass() {
        String p = "team_10.client.data.models.";
        Class<Account> c = null;

        try {
            c = (Class<Account>) Class.forName(p + clazz.getSimpleName());
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
    public static TYPE getType(String s) {
        for (TYPE t : TYPE.values()) {
            if (t.simpleName.equals(s)) {
                return t;
            }
        }


        return null;
    }

    /**
     * Returns TYPE enum member for clazz
     * @param clazz Class which will be used to find it's coordinating TYPE
     * @return TYPE
     */
    public static TYPE getType(Class<? extends Account> clazz) {
        for (TYPE t : TYPE.values()) {
            if (t.clazz.equals(clazz)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return simpleName;
    }

    public Class<? extends Account> getClazz() {
        return clazz;
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
