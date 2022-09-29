package is.hi.hbv501g.hbv1.Persistence.Entities;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    //TODO: fill in attributes of Lessons
}
