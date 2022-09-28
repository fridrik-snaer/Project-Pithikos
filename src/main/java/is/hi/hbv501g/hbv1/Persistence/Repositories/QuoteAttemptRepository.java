package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteAttemptRepository extends JpaRepository<QuoteAttempt, Long> {
}
