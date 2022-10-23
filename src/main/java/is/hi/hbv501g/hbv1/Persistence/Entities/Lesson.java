package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;

/**
 * Specialised container for text used in lessons
 */
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String text;
    private Lang lang;
    private int lvl;

    public Lesson() {
    }

    public Lesson(String text, Lang lang, int lvl) {
        this.text = text;
        this.lang = lang;
        this.lvl = lvl;
    }
    //<editor-fold desc="Getters & Setters">
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }
    //</editor-fold>
}
