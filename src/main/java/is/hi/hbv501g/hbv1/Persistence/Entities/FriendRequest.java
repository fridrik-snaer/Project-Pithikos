package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * An entity for storing information about the friendship relation between two users
 */
@Entity
@Table(name = "friendRequests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User requestSender;
    @ManyToOne(fetch = FetchType.EAGER)
    private User requestReciever;
    private Timestamp created_at;

    public FriendRequest(User sender, User requestReciever) {
        this.requestSender = sender;
        this.requestReciever = requestReciever;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public FriendRequest() {
    }
    //<editor-fold desc="Getters & Setters>


    public long getId() {
        return id;
    }

    public User getRequestSender() {
        return requestSender;
    }

    public void setRequestSender(User requestSender) {
        this.requestSender = requestSender;
    }

    public User getRequestReciever() {
        return requestReciever;
    }

    public void setRequestReciever(User requestReciever) {
        this.requestReciever = requestReciever;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    //</editor-fold>
}
