package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Persistence.Repositories.RelationshipRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.UserRepository;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImplementation implements UserService {
    private User loggedIn;
    private UserRepository userRepository;
    private RelationshipRepository relationshipRepository;

    @Autowired
    public UserServiceImplementation(UserRepository userRepository, RelationshipRepository relationshipRepository) {
        this.loggedIn = null;
        this.userRepository = userRepository;
        this.relationshipRepository = relationshipRepository;
    }

    @Override
    public User create(User user) {
        if (exists(user)){
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
    //TODO græja equals
    @Override
    public User changePassword(int user_id, String newPassword) {
        System.out.println(user_id);
        User user = this.findByID(user_id);
        System.out.println(loggedIn.getUsername());
        System.out.println(user.getUsername());
        if (loggedIn.equal(user)) {
            userRepository.delete(user);
            user.setPassword(newPassword);
            User newUser = userRepository.save(user);
            loggedIn = newUser;
            return newUser;
        }
        System.out.println("Ekki sami loggaður inn og reynt að breyta lykilorði");
        return null;
    }

    @Override
    public List<User> getFriends(User user) {
        return null;
    }

    @Override
    public Relationship makeRelationship(User sender, User reciever) {
        return null;
    }

    @Override
    public User findByID(int id) {
        return userRepository.findById(id);
    }
    public User login(User user){
        loggedIn = user;
        return user;
    }
    public User logout(){
        User previous = loggedIn;
        loggedIn = null;
        return previous;
    }
    public User getLogged(){
        return loggedIn;
    }

    private boolean exists(User user){
        List<User> users = userRepository.findByUsername(user.getUsername());
        return !users.isEmpty();
    }
}
