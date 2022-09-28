package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private float ID;
    private String text;
    private String language;
    private int rank;

    public Word() {}

    public Word(String text, String language, int rank) {
        this.text = text;
        this.language = language;
        this.rank = rank;
    }

    //<editor-fold desc="Getters & Setters>
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    //</editor-fold>
}
