package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.RandomAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomAttemptRepository extends JpaRepository<RandomAttempt,Long> {
}