package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Entity for storing information about a single attempt at a quote
 */
@Entity
@Table(name = "quoteAttempts")
public class QuoteAttempt /*extends Attempt*/ implements Comparable<QuoteAttempt>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private Quote quote;
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

    public QuoteAttempt(User user, Quote quote, Timestamp time_start, Timestamp time_finish, int keystrokes, int correct, boolean completed, boolean daily, boolean canpost) {
        this.user = user;
        this.quote = quote;
        this.time_start = time_start;
        this.time_finish = time_finish;
        this.keystrokes = keystrokes;
        this.correct = correct;
        this.completed = completed;
        this.daily = daily;
        this.canpost = canpost;
    }

    public QuoteAttempt(Timestamp time_start, Timestamp time_finish, int keystrokes, int correct, boolean completed, boolean daily, boolean canpost) {
        this.time_start = time_start;
        this.time_finish = time_finish;
        this.keystrokes = keystrokes;
        this.correct = correct;
        this.completed = completed;
        this.daily = daily;
        this.canpost = canpost;
    }

    @Override
    public String toString() {
        return "This quote attempt started at " + time_start.toString() + " ended at " + time_finish.toString() + " had " + keystrokes + " keystrokes and " + correct + " were correct";
    }

    @Override
    public int compareTo(QuoteAttempt o) {
        long this_time = this.time_finish.getTime()-this.time_start.getTime();
        long u_time = o.time_finish.getTime()-o.time_start.getTime();
        if (this_time<u_time) {return -1;}
        else if (this_time>u_time) {return 1;}
        else {return 0;}
    }

    //<editor-fold desc="Getters & Setters">

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
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
    //</editor-fold>
}
