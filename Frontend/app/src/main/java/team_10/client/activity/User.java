package team_10.client.activity;

import java.util.ArrayList;
import java.util.List;
import team_10.client.account.*;

public class User {
    private int id, type;
    private String lastName, firstName, email; //Password doesn't need to be in user

    List<Account> accounts;

    public User(int id, String lastName, String firstName, String email, int type) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.type = type;

        accounts = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getLastName() {return lastName;}

    public String getFirstName() {return firstName;}

    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account a) {
        accounts.add(a);
    }


    //public String getUsername() {
     //   return username;
    //}

    public String getEmail() {
        return email;
    }

    public int getType() {return type;}
}
// id, last name, first name, email, password, type (int)