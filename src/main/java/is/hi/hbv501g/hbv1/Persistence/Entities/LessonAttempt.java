package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "lessonAttempts")
public class LessonAttempt implements Comparable<LessonAttempt>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    private Lesson lesson;
    private Timestamp time_start;
    private Timestamp time_finish;
    private int keystrokes;
    private int correct;
    private boolean completed;

    public LessonAttempt() {
    }

    public LessonAttempt(User user, Lesson lesson, Timestamp time_start, Timestamp time_finish, int keystrokes, int correct, boolean completed) {
        this.user = user;
        this.lesson = lesson;
        this.time_start = time_start;
        this.time_finish = time_finish;
        this.keystrokes = keystrokes;
        this.correct = correct;
        this.completed = completed;
    }

    @Override
    public int compareTo(LessonAttempt o) {
        long this_time = this.time_finish.getTime()-this.time_start.getTime();
        long u_time = o.time_finish.getTime()-o.time_start.getTime();
        if (this_time<u_time) {return -1;}
        else if (this_time>u_time) {return 1;}
        else {return 0;}
    }

    //<editor-fold desc="Getters & Setters">

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
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


    //</editor-fold>

}
