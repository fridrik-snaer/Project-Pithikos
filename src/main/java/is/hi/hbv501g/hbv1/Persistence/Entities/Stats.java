package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.util.List;

/**
 * An entity used for storing statistical data of a user
 */
@Entity
public class Stats {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    private float avgWpm;
    private float avgAcc;
    private int testsTaken;
    private int testsCompleted;

//    private List<Attempt> allAttempts; Kannski sniðugt
    public Stats() {
        this.avgWpm = 0;
        this.avgAcc = 0;
        this.testsTaken = 0;
        this.testsCompleted = 0;
    }

    public Stats(User user, float avgWpm, float avgAcc, int testsTaken, int testsCompleted) {
        this.user = user;
        this.avgWpm = avgWpm;
        this.avgAcc = avgAcc;
        this.testsTaken = testsTaken;
        this.testsCompleted = testsCompleted;
    }

    //    <editor-fold desc="Functions">
    public void Update_Stats(List<QuoteAttempt> attempts) {
        //update stat of user bla bla
    }
    //TODO: decide which functions stats should have and implement them
//    </editor-fold>

//<editor-fold desc="Getters & Setters">
    public float getAvgWpm() {
        return avgWpm;
    }

    public void setAvgWpm(float avgWpm) {
        this.avgWpm = avgWpm;
    }

    public float getAvgAcc() {
        return avgAcc;
    }

    public void setAvgAcc(float avgAcc) {
        this.avgAcc = avgAcc;
    }

    public int getTestsTaken() {
        return testsTaken;
    }

    public void setTestsTaken(int testsTaken) {
        this.testsTaken = testsTaken;
    }

    public int getTestsCompleted() {
        return testsCompleted;
    }

    public void setTestsCompleted(int testsCompleted) {
        this.testsCompleted = testsCompleted;
    }
    //</editor-fold>
}
