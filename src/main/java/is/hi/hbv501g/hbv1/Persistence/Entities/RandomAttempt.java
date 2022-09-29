package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "randomAttempts")
public class RandomAttempt /*extends Attempt*/{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long user_id;
    private Timestamp time_start;
    private Timestamp time_finish;
    private int keystrokes;
    private int correct;
    private boolean completed;

    //RandomAttempt specific
    private String mode;
}
