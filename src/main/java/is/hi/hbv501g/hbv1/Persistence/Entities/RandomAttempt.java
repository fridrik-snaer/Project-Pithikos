package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
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
