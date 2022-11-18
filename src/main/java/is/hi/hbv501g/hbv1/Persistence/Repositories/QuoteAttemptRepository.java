package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuoteAttemptRepository extends JpaRepository<QuoteAttempt, Long> {
    List<QuoteAttempt> findByUser(User user);
    List<QuoteAttempt> findByQuote(Quote quote);
    QuoteAttempt findById(long id);
    int countAllByUser(User user);
    int countAllByQuote(Quote quote);
    QuoteAttempt save(QuoteAttempt quoteAttempt);

    @Query(nativeQuery = true, value =
            "select * from " +
            "quote_attempts qa " +
            "left join users us on us.id = qa.user_id " +
            "left join quotes qu on qu.id = qa.quote_id " +
            "where qa.quote_id = :q and qa.canpost = true " +
            "order by " +
            "((60 * qa.correct) / (5.156 * extract(epoch from qa.time_finish - qa.time_start))) desc " +
            "LIMIT :n"
    )
    List<QuoteAttempt> findBestQuoteAttempts(@Param("q") long q, @Param("n") int n); // TODO velja líka acc > 0.95
}
