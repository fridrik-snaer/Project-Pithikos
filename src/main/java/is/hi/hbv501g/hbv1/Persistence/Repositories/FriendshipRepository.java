package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.Friendship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship,Long> {
    List<Friendship> findBySender(User sender);
    List<Friendship> findByReciever(User reciever);
    Friendship save(Friendship friendship);
    List<Friendship> findAllBySender_UsernameAndReciever_Username(String sUsername, String rUsername);
    List<Friendship> findAllBySenderOrReciever(User sender, User reciever);
}
