package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote,Long> {
    Quote findByID(long Id);
    Quote findFirstByOrderByID();
    List<Quote> findAllByDailyTrue();
    List<Quote> findAllByAcceptedTrue();
    Quote save(Quote quote);

}
