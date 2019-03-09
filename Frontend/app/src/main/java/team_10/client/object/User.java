package team_10.client.object;

import java.util.ArrayList;
import java.util.List;

import team_10.client.object.account.Account;
import team_10.client.object.account.AccountsWrapper;

public class User {
    private static int userID, type, numAccounts = 0;
    private static String lastName, firstName, email; //Password doesn't need to be in user

    private static List<Account> accounts;

    public User(int userID, String lastName, String firstName, String email, int type) {
        this.userID = userID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.type = type;

        accounts = new ArrayList<>();
    }

    public static int getID() {
        return userID;
    }

    public static String getLastName() {return lastName;}

    public static String getFirstName() {return firstName;}

    public static List<Account> getAccounts() { return accounts; }

    public static void setAccounts(List<Account> aList) { accounts = aList; }

    public static AccountsWrapper getAccountsWrapper() {
        AccountsWrapper a = new AccountsWrapper();
        a.setAccounts(accounts);

        return a;
    }

    public static void addAccount(Account a) {
        a.setID(String.format("%08d", userID) + String.format("%04d", (numAccounts++)));
        accounts.add(a);
    }

    public static String getEmail() {
        return email;
    }

    public static int getType() {return type;}
}
// id, last name, first name, email, password, type (int)