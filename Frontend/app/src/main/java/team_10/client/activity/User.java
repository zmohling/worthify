package team_10.client.activity;

public class User {
    private int id, type;
    private String lastName, firstName, email; //Password doesn't need to be in user

    public User(int id, String lastName, String firstName, String email, int type) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {return lastName;}

    public String getFirstName() {return firstName;}


    //public String getUsername() {
     //   return username;
    //}

    public String getEmail() {
        return email;
    }

    public int getType() {return type;}
}
// id, last name, first name, email, password, type (int)