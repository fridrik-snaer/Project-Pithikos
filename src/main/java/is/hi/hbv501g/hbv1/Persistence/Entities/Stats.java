package is.hi.hbv501g.hbv1.Persistence.Entities;

import java.util.List;

public class Stats {
    private float avg_wpm;
    private float avg_acc;
    private float tests_taken;
    private float tests_completed;

//    private List<Attempt> allAttempts; Kannski sniðugt
    public Stats() {
        this.avg_wpm = 0;
        this.avg_acc = 0;
        this.tests_taken = 0;
        this.tests_completed = 0;
    }

    public Stats(float avg_wpm, float avg_acc, float tests_taken, float tests_completed) {
        this.avg_wpm = avg_wpm;
        this.avg_acc = avg_acc;
        this.tests_taken = tests_taken;
        this.tests_completed = tests_completed;
    }

//    <editor-fold desc="Functions">
    public void Update_Stats(List<Attempt> attempts) {
        //update stat of user bla bla
    }
    //TODO: decide which functions stats should have and implement them
//    </editor-fold>

//<editor-fold desc="Getters & Setters">
    public float getAvg_wpm() {
        return avg_wpm;
    }

    public void setAvg_wpm(float avg_wpm) {
        this.avg_wpm = avg_wpm;
    }

    public float getAvg_acc() {
        return avg_acc;
    }

    public void setAvg_acc(float avg_acc) {
        this.avg_acc = avg_acc;
    }

    public float getTests_taken() {
        return tests_taken;
    }

    public void setTests_taken(float tests_taken) {
        this.tests_taken = tests_taken;
    }

    public float getTests_completed() {
        return tests_completed;
    }

    public void setTests_completed(float tests_completed) {
        this.tests_completed = tests_completed;
    }
    //</editor-fold>
}