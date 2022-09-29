package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuoteRepository extends JpaRepository<Quote,Long> {
    Quote findByID(long Id);
    Quote findFirstByOrderByID();
    List<Quote> findAllByDailyTrue();
    List<Quote> findAllByAcceptedTrue();
    Quote save(Quote quote);

}
