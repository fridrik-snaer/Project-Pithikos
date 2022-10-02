package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stats stats;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteAttempt> quoteAttempts = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RandomAttempt> randomAttempts = new ArrayList<>();
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Relationship> senderRelationships = new ArrayList<>();
    @OneToMany(mappedBy = "reciever", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Relationship> recieverRelationships = new ArrayList<>();

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

    //Add and delete from randomAttempts,quoteAttempt, senderRelationships,recieverRelationships

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

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public List<QuoteAttempt> getQuoteAttempts() {
        return quoteAttempts;
    }

    public void setQuoteAttempts(List<QuoteAttempt> quoteAttempts) {
        this.quoteAttempts = quoteAttempts;
    }

    public List<RandomAttempt> getRandomAttempts() {
        return randomAttempts;
    }

    public void setRandomAttempts(List<RandomAttempt> randomAttempts) {
        this.randomAttempts = randomAttempts;
    }

    public List<Relationship> getSenderRelationships() {
        return senderRelationships;
    }

    public void setSenderRelationships(List<Relationship> senderRelationships) {
        this.senderRelationships = senderRelationships;
    }

    public List<Relationship> getRecieverRelationships() {
        return recieverRelationships;
    }

    public void setRecieverRelationships(List<Relationship> recieverRelationships) {
        this.recieverRelationships = recieverRelationships;
    }
    //</editor-fold>
}
