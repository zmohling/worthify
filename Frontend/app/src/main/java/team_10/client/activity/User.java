package team_10.client.activity;

import java.util.ArrayList;
import java.util.List;
import team_10.client.account.*;

public class User {
    private static int userID, type;
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

    public static AccountsWrapper getAccountsWrapper() {
        AccountsWrapper a = new AccountsWrapper();
        a.setAccounts(accounts);

        return a;
    }

    public static void addAccount(Account a) {
        accounts.add(a);
    }


    //public String getUsername() {
     //   return username;
    //}

    public static String getEmail() {
        return email;
    }

    public static int getType() {return type;}
}
// id, last name, first name, email, password, type (int)