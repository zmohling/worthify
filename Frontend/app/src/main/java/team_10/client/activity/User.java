package team_10.client.activity;

import java.util.ArrayList;
import java.util.List;
import team_10.client.account.*;

public class User {
    private static int id, type;
    private static String lastName, firstName, email; //Password doesn't need to be in user

    private static List<Account> accounts;

    public User(int id, String lastName, String firstName, String email, int type) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.type = type;

        accounts = new ArrayList<>();
    }

    public static int getId() {
        return id;
    }

    public static String getLastName() {return lastName;}

    public static String getFirstName() {return firstName;}

    public static List<Account> getAccounts() { return accounts; }

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