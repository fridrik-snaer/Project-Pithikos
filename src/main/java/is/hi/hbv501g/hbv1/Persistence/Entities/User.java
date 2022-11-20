package is.hi.hbv501g.hbv1.Persistence.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An entity used for storing data about the user
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private Timestamp createdAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stats stats = new Stats();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteAttempt> quoteAttempts = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RandomAttempt> randomAttempts = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonAttempt> lessonAttempts;
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> senderFriendships = new ArrayList<>();
    @OneToMany(mappedBy = "reciever", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friendship> recieverFriendships = new ArrayList<>();
    @OneToMany(mappedBy = "requestSender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> senderFriendRequests = new ArrayList<>();
    @OneToMany(mappedBy = "requestReciever", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> recieverFriendRequests = new ArrayList<>();

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    @Override
    public String toString() {
        return "User: "+ this.username;
    }

    public User(long id) {
        this.id = id;
    }


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = new ArrayList<>();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public boolean equal(User user) {
        return this.id == user.id;
    }
    public void clear(){
        setPassword(null);
        setStats(null);
        setQuoteAttempts(null);
        setRandomAttempts(null);
        setRecieverFriendships(null);
        setSenderFriendships(null);
        setRecieverFriendRequests(null);
        setSenderFriendRequests(null);
        setLessonAttempts(null);
    }

    //<editor-fold desc="Functions">
    //TODO: decide which functions User class should have and implement them

    //Add and delete from randomAttempts,quoteAttempt, senderRelationships,recieverRelationships

    //</editor-fold>
    //<editor-fold desc="Getters & Setters">


    public List<LessonAttempt> getLessonAttempts() {
        return lessonAttempts;
    }

    public void setLessonAttempts(List<LessonAttempt> lessonAttempts) {
        this.lessonAttempts = lessonAttempts;
    }

    public long getId() {
        return id;
    }

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

    public List<Friendship> getSenderFriendships() {
        return senderFriendships;
    }

    public void setSenderFriendships(List<Friendship> senderFriendships) {
        this.senderFriendships = senderFriendships;
    }

    public List<Friendship> getRecieverFriendships() {
        return recieverFriendships;
    }

    public void setRecieverFriendships(List<Friendship> recieverFriendships) {
        this.recieverFriendships = recieverFriendships;
    }

    public List<FriendRequest> getSenderFriendRequests() {
        return senderFriendRequests;
    }

    public void setSenderFriendRequests(List<FriendRequest> senderFriendRequests) {
        this.senderFriendRequests = senderFriendRequests;
    }

    public List<FriendRequest> getRecieverFriendRequests() {
        return recieverFriendRequests;
    }

    public void setRecieverFriendRequests(List<FriendRequest> recieverFriendRequests) {
        this.recieverFriendRequests = recieverFriendRequests;
    }
    //</editor-fold>
}
