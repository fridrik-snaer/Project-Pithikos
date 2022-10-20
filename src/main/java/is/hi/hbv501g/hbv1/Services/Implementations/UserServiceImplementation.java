package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Relationship;
import is.hi.hbv501g.hbv1.Persistence.Entities.Role;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Persistence.Repositories.RelationshipRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.RoleRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.UserRepository;
import is.hi.hbv501g.hbv1.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;

@Service @Slf4j @RequiredArgsConstructor @Transactional
//Extenda userDetails service fyrir security reasons
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RelationshipRepository relationshipRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("Saved new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }
    //TODO græja equals
    @Override
    public User changePassword(long user_id, String newPassword) {
//        Sorry braut þetta alveg frikki, kv. Valdi

//        System.out.println(user_id);
//        User user = this.findById(user_id);
//        System.out.println(loggedIn.getUsername());
//        System.out.println(user.getUsername());
//        if (loggedIn.equal(user)) {
//            userRepository.delete(user);
//            user.setPassword(newPassword);
//            User newUser = userRepository.save(user);
//            loggedIn = newUser;
//            return newUser;
//        }
//        System.out.println("Ekki sami loggaður inn og reynt að breyta lykilorði");
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

    private boolean exists(User user){
        User dbUser = userRepository.findByUsername(user.getUsername());
        return !isNull(dbUser);
    }

    @Override
    //Implement-a þetta frá UserDetailService svo að þetta class sé usable í SecurityConfig
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(isNull(user)) {
            log.error("User {} not found in the database", username);
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.error("User {} found in the database!", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role ->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }

}
