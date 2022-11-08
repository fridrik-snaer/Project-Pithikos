package is.hi.hbv501g.hbv1.Persistence.Repositories;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.LessonAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonAttemptRepository extends JpaRepository<LessonAttempt, Long> {
    List<LessonAttempt> findByUserAndLessonLang(User user, Lang lang);
    List<LessonAttempt> findByUser(User user);
    List<LessonAttempt> findByLesson(Lesson lesson);
    LessonAttempt findById(long id);
    int countAllByUser(User user);
    int countAllByLesson(Lesson lesson);
    LessonAttempt save(LessonAttempt lessonAttempt);
}
