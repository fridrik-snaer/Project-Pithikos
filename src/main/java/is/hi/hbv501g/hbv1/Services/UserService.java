package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public interface UserService {
    User saveUser(User user);

    User getUser(String username);
    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);
    void delete(User user);
    User changePassword(long user_id, String newPassword);
    List<User> getFriends(User user);
    Friendship makeRelationship(User sender, User reciever);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    User findByUsername(String username);

    FriendRequest sendFriendRequest(FriendRequest friendRequest);

    ResponseEntity<FriendRequest> sendFriendRequestVol2(FriendRequest friendRequest);

    Friendship acceptRequest(FriendRequest friendRequest);

    FriendRequest declineRequest(FriendRequest friendRequest);

    List<FriendRequest> getIncomingRequests(User user);

    List<FriendRequest> getOutgoingRequests(User user);

    List<Stats> getFriendsStats(User user);
}
