package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String text;
    private Lang language;
    private String origin; // Author & Reference
    private boolean accepted;
    private boolean daily;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Quote() {
    }

    public Quote(String text, Lang language, String origin, boolean accepted, boolean daily) {
        this.text = text;
        this.language = language;
        this.origin = origin;
        this.accepted = accepted;
        this.daily = daily;
    }

    public Quote(String text, Lang language, String origin) {
        this.text = text;
        this.language = language;
        this.origin = origin;
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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    //</editor-fold>
}
