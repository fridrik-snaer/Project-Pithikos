package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteAttemptRepository extends JpaRepository<QuoteAttempt, Long> {
    List<QuoteAttempt> findByUser(User user);
    List<QuoteAttempt> findByQuote(Quote quote);
    QuoteAttempt findById(long id);
    int countAllByUser(User user);
    QuoteAttempt save(QuoteAttempt quoteAttempt);
}
