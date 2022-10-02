package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "relationships")
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    @ManyToOne(fetch = FetchType.LAZY)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reciever;
    private Timestamp created_at;

    public Relationship(User sender, User reciever) {
        this.sender = sender;
        this.reciever = reciever;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }

    public Relationship() {
    }
    //<editor-fold desc="Getters & Setters>

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReciever() {
        return reciever;
    }

    public void setReciever(User reciever) {
        this.reciever = reciever;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    //</editor-fold>
}
