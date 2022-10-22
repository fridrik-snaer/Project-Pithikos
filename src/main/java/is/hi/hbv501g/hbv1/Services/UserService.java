package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.Role;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.stereotype.Service;

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
    Relationship makeRelationship(User sender, User reciever);
    User findById(long id);
}
