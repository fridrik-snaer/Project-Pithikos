package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote,Long> {
    Quote findById(long id);
    Quote findFirstByOrderById();
    List<Quote> findAllByDailyTrue();
    List<Quote> findAllByAcceptedTrue();
    List<Quote> findAllByAcceptedTrueAndDailyFalseAndLanguage(Lang lang);
    Quote save(Quote quote);

}
