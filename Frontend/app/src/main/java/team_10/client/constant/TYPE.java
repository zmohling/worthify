package team_10.client.constant;

import android.support.annotation.NonNull;

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
    CERTIFICATEOFDEPOSIT("Certificate of Deposit", CertificateOfDeposit.class, false, true),
    LOAN("Loan", Loan.class, false, false),
    SAVINGSACCOUNT("Savings Account", SavingsAccount.class, false, true),
    REALESTATE("Real Estate", RealEstate.class, true, true),
    STOCK("Stock", Stock.class, true, true);

    private String displayName;
    private Class<? extends Account> clazz;
    private boolean isValueOnNetwork;
    private boolean isAsset;

    TYPE(@NonNull String displayName, @NonNull Class<? extends Account> clazz, @NonNull boolean isValueOnNetwork, @NonNull boolean isAsset) {
        this.displayName = displayName;
        this.clazz = clazz;
        this.isValueOnNetwork = isValueOnNetwork;
        this.isAsset = isAsset;
    }

    /**
     * Gets the type of the account.
     * @return the type's account class
     */
    public Class<? extends Account> getTypeClass() {
        return this.clazz;
    }

    /**
     * Finds type that matches.
     * @param s
     * @return the type that matches s in the types list
     */
    public static TYPE getType(String s) {
        for (TYPE t : TYPE.values()) {
            if (t.displayName.equals(s)) {
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

    /**
     * Overrides Object's toString() method and
     * returns the TYPE's display name.
     * @return Name of type suited for displaying to the user.
     */
    @Override
    public @NonNull String toString() {
        return this.displayName;
    }

    public @NonNull boolean isValueOnNetwork() { return this.isValueOnNetwork; }

    public @NonNull boolean isAsset() { return this.isAsset; }

    public @NonNull Class<? extends Account> getClazz() {
        return this.clazz;
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
