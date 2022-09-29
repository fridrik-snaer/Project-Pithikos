package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "quoteAttempts")
public class QuoteAttempt /*extends Attempt*/ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private long user_id;
    private Timestamp time_start;
    private Timestamp time_finish;
    private int keystrokes;
    private int correct;
    private boolean completed;

    //QuoteAttempt specific
    private boolean daily;
    private boolean canpost;

    public QuoteAttempt() {
    }

    public QuoteAttempt(long user_id, Timestamp time_start, Timestamp time_finish, int keystrokes, int correct, boolean completed, boolean daily, boolean canpost) {
        this.user_id = user_id;
        this.time_start = time_start;
        this.time_finish = time_finish;
        this.keystrokes = keystrokes;
        this.correct = correct;
        this.completed = completed;
        this.daily = daily;
        this.canpost = canpost;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Timestamp getTime_start() {
        return time_start;
    }

    public void setTime_start(Timestamp time_start) {
        this.time_start = time_start;
    }

    public Timestamp getTime_finish() {
        return time_finish;
    }

    public void setTime_finish(Timestamp time_finish) {
        this.time_finish = time_finish;
    }

    public int getKeystrokes() {
        return keystrokes;
    }

    public void setKeystrokes(int keystrokes) {
        this.keystrokes = keystrokes;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    public boolean isCanpost() {
        return canpost;
    }

    public void setCanpost(boolean canpost) {
        this.canpost = canpost;
    }
}
