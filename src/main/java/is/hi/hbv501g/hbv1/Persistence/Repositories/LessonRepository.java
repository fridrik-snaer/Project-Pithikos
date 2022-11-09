package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {
    List<Lesson> findAll();
    Lesson findById(long id);
    List<Lesson> findAllByLang(Lang lang);
    Lesson findByLvlAndAndLang(int lvl,Lang lang);
    Lesson save(Lesson lesson);
}
