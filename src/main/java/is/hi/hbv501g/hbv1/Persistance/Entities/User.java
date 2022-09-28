package is.hi.hbv501g.hbv1.Persistance.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String username;
    private String passqord;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();

    public User(String username, String passqord) {
        this.username = username;
        this.passqord = passqord;
    }

    public User() {
    }
}
