package is.hi.hbv501g.hbv1.Persistence.Repositories;

import is.hi.hbv501g.hbv1.Persistence.Entities.FriendRequest;
import is.hi.hbv501g.hbv1.Persistence.Entities.Friendship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest,Long> {
    FriendRequest save(FriendRequest friendRequest);
    List<FriendRequest> findAllByRequestSenderUsernameAndRequestRecieverUsername(String rsUsername, String rrUsername);
    FriendRequest findById(long id);
    List<FriendRequest> findAllByRequestSender(User user);

    List<FriendRequest> findAllByRequestReciever(User user);
    void delete(FriendRequest friendRequest);
}
