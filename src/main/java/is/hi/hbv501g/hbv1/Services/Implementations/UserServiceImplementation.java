package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Persistence.Repositories.RelationshipRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.UserRepository;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;

import static java.util.Objects.isNull;

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

        String unhashedPassword = user.getPassword();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, unhashedPassword.toCharArray());
        System.out.println(bcryptHashString);
        // $2a$12$US00g/uMhoSBm.HiuieBjeMtoN69SN.GE25fCpldebzkryUyopws6
        BCrypt.Result result = BCrypt.verifyer().verify(unhashedPassword.toCharArray(), bcryptHashString);
        System.out.println(result);

        User newUser = new User(user.getUsername(), bcryptHashString, user.getEmail());
        
        return userRepository.save(newUser);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
    //TODO græja equals
    @Override
    public User changePassword(long user_id, String newPassword) {
        System.out.println(user_id);
        User user = this.findById(user_id);
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
    public User findById(long id) {
        return userRepository.findById(id);
    }
    public String login(User user){
        User dbUser = userRepository.findByEmail(user.getEmail());
        System.out.print("User" + user);
        System.out.print("dbUser" + dbUser);
        if(dbUser == null) return "Notandi er ekki til";

        //Compare password of dbUser and user
        BCrypt.Result result = BCrypt.verifyer().verify(user.getPassword().toCharArray(), dbUser.getPassword());

        //TODO: Generate-a token til að senda á framenda sem confirm-ar að notandi er loggaður inn
        System.out.println(result);
        if(result.verified) return "Tókst að logga inn!";
        else return "Tókst ekki að logga inn ;(";
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
        User dbUser = userRepository.findByUsername(user.getUsername());
        return !isNull(dbUser);
    }
}
