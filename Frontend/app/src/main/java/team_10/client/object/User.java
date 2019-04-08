package team_10.client.object;

import java.util.List;

import team_10.client.object.account.Account;

public class User {
    private static int userID, type, numAccounts;
    private static String lastName, firstName, email, token; //Password doesn't need to be in user
    private static List<Account> accounts;

    public User(int userID, String lastName, String firstName, String email, int type, String token) {
        this.userID = userID;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.type = type;
        this.token = token;
    }

    public static int getID() {
        return userID;
    }

    public static String getLastName() {
        return lastName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static List<Account> getAccounts() {
        return accounts;
    }

    public static void setAccounts(List<Account> aList) {
        accounts = aList;
    }

    public static void addAccount(Account a) {
        if (a.getID() == null) {
            a.setID(String.format("%08d", userID) + String.format("%04d", (numAccounts++)));
        }

        accounts.add(a);
    }

    // Return's ID of remove accounts
    public static String removeAccount(Account a) {
        if (a != null) {
            accounts.remove(a);
            return a.getID();
        } else {
            return null;
        }
    }

    public static String getEmail() {
        return email;
    }

    public static int getType() {
        return type;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }

}