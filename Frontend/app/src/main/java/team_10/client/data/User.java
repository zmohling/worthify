package team_10.client.data;

/**
 * Class that keeps track of the user's information.
 */
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

    /**
     * Gets the user's ID.
     * @return user's ID
     */
    public static int getID() {
        return userID;
    }

    /**
     * Gets the user's last name.
     * @return user's last name.
     */
    public static String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's first name.
     * @return user's first name.
     */
    public static String getFirstName() {
        return firstName;
    }

    /**
     * Gets a user's email.
     * @return user's email
     */
    public static String getEmail() {
        return email;
    }

    /**
     * Gets a user's type.
     * @return user's type.
     */
    public static int getType() {
        return type;
    }

    /**
     * Gets a user's token.
     * @return user's token.
     */
    public static String getToken() {
        return token;
    }

    /**
     * Sets a user's token.
     * @param t user's token
     */
    public static void setToken(String t) {
        token = t;
    }
}