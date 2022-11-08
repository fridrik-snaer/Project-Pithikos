package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Specialised container for text used in lessons
 */
@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    private String description;
    private String text;
    private Lang lang;
    private int lvl;
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonAttempt> lessonAttempts = new ArrayList<>();

    public Lesson() {
    }

    public Lesson(String description,String text, Lang lang, int lvl) {
        this.text = text;
        this.lang = lang;
        this.lvl = lvl;
        this.description = description;
    }
    //<editor-fold desc="Getters & Setters">

    public long getId() {
        return Id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }
    //</editor-fold>
}
