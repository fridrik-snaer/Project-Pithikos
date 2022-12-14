package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    void delete(User user);
    User findById(long user_id);
    User findByUsername(String username);

    User findByEmail(String email);
}
