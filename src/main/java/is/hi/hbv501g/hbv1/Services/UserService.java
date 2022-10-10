package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User create(User user);
    void delete(User user);
    User changePassword(User user,String newPassword);
    List<User> getFriends(User user);
    Relationship makeRelationship(User sender, User reciever);
}
