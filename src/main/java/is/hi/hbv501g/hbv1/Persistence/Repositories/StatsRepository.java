package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.Stats;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatsRepository extends JpaRepository<Stats,Long> {
    Stats findByUser(User user);
    Stats save(Stats stats);
    void delete(Stats stats);
    List<Stats> findTop10ByOrderByAvgWpmDesc();

    @Query(nativeQuery = true,
            value = "SELECT * FROM " +
                    "stats s " +
                    "WHERE " +
                    "s.user_id is not null " +
                    "order by " +
                    "avg_wpm desc " +
                    "limit :n"
    )
    List<Stats> findTopN(@Param("n") int n);

    List<Stats> findTop10ByOrderByAvgWpm();
    List<Stats> findAll();
}
