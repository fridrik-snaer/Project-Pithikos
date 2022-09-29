package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;

@Entity
@Table(name = "words")
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String text;
    private Lang language;
    private int rank;

    public Word() {}

    public Word(String text, Lang language, int rank) {
        this.text = text;
        this.language = language;
        this.rank = rank;
    }
    public  String toString() {
        return this.text;
    }

    //<editor-fold desc="Getters & Setters>
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Lang getLanguage() {
        return language;
    }

    public void setLanguage(Lang language) {
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
