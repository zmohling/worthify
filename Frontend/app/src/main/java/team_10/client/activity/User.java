package team_10.client.activity;

public class User {
    private int id;
    private String username, email;

    public User(int id, String username, String email, String gender) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public User(int id, String username, String email) {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
