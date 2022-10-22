package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.RandomAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RandomAttemptRepository extends JpaRepository<RandomAttempt,Long> {
    List<RandomAttempt> findByUser(User user);
    int countAllByUser(User user);
    RandomAttempt save(RandomAttempt randomAttempt);
}
