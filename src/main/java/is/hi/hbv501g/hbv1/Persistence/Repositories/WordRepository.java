package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word,Long> {
    List<Word> findAll();
    List<Word> findAllByLanguage(Lang lang);
    List<Word> findAllByLanguageAndAndRankLessThanEqual(Lang lang,int rank);
    Word findByRankAndLanguage(int rank, Lang lang);
    Word findById(long id);
    Word save(Word word);
}
