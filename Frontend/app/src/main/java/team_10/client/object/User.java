package team_10.client.object;

public class User {
    private static int userID, type;
    private static String lastName, firstName, email, token; //Password doesn't need to be in user

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