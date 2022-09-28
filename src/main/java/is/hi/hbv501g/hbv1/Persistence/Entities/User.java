package is.hi.hbv501g.hbv1.Persistence.Entities;

import java.sql.Timestamp;

public class User {
    private String Id;
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
    private Stats stats;

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.stats = new Stats();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    //<editor-fold desc="Functions">
    //TODO: decide which functions User class should have and implement them
    //</editor-fold>
    //<editor-fold desc="Getters & Setters">
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
    //</editor-fold>
}
