package is.hi.hbv501g.hbv1.Services.Implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Persistence.Repositories.*;
import is.hi.hbv501g.hbv1.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service @Slf4j @RequiredArgsConstructor @Transactional
//Extenda userDetails service fyrir security reasons
public class UserServiceImplementation implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StatsRepository statsRepository;
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * saves a user to the database
     * @param user the user to save
     * @return the user with its newly generated id
     */
    @Override
    public User saveUser(User user) {
        log.info("Saved new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Find a user by username
     * @param username the username to search by
     * @return the user
     */
    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a new role to database
     * @param role the role to be saved
     * @return the role
     */
    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    /**
     * Gives a user a role
     * @param username the user to be given a role
     * @param roleName the role to be given to the user
     */
    @Override
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        user.getRoles().add(role);
        userRepository.save(user);
    }

    /**
     * deletes a user
     * @param user the user to be deleted
     */
    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public User changePassword(long user_id, String newPassword) {
        return null;
    }

    @Override
    public List<User> getFriends(User user) {
        System.out.println(user.getUsername());
        user = findByUsername(user.getUsername());
        if (isNull(user)){
            System.out.println("No user with this username");
            return null;
        }
        List<Friendship> friendships = friendshipRepository.findAllBySenderOrReciever(user,user);
        List<User> friends = new ArrayList<>();
        for (Friendship f:friendships) {
            if (f.getSender().equals(user)){
                friends.add(f.getReciever());
            }
            else friends.add(f.getSender());
        }
        return friends;
    }

    @Override
    public List<Stats> getFriendsStats(User user) {
        user = findByUsername(user.getUsername());
        if (isNull(user)){
            System.out.println("No user with this username");
            return null;
        }
        List<User> friends = getFriends(user);
        List<Stats> friendsStats = new ArrayList<>();
        for (User friend:friends) {
            friendsStats.add(statsRepository.findByUser(friend));
        }
        return friendsStats;
    }

    @Override
    public Friendship makeRelationship(User sender, User reciever) {
        return null;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("I am in the refreshToken method");
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken =  authorizationHeader.substring("Bearer ".length());
                log.info("Refresh token {}", refreshToken);
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes()); //TODO: refactor secret
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject(); //This http request only has the token
                User user = getUser(username);
                System.out.println("Creating new accessToken");
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) //access Token expires in 10 minutes
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                System.out.println("Creating response");
                Map<String,String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e){
                log.error("Error logging in: {}", e.getMessage());
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                //This sets the response content type to application json
                //and returns a json with the error message
                Map<String,String> error = new HashMap<>();
                error.put("errorMessage",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    /**
     * Checks if user exists in database
     * @param user the user in question
     * @return true if he exists, false otherwise
     */
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

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public FriendRequest sendFriendRequest(FriendRequest friendRequest) {
        User sender = findByUsername(friendRequest.getRequestSender().getUsername());
        User reciever = findByUsername(friendRequest.getRequestReciever().getUsername());
        if (isNull(reciever)){
            System.out.println("Reciever not found");
            return null;
        }
        if (isNull(sender)){
            System.out.println("Sender not found");
            return null;
        }
        if (sender.getUsername().equals(reciever.getUsername())){
            System.out.println("Not possible to send yourself friendrequest");
            return null;
        }
        if (friendshipExists(new Friendship(friendRequest.getRequestSender(),friendRequest.getRequestReciever()))){
            System.out.println("Friendship already exists");
            return null;
        }
        if (friendRequestExists(friendRequest)){
            System.out.println("Friend request already exists");
            return null;
        }
        FriendRequest toSave = new FriendRequest(sender,reciever);
        friendRequestRepository.save(toSave);
        return toSave;
    }

    private boolean friendshipExists(Friendship friendship){
        List<Friendship> s = friendshipRepository.findAllBySender_UsernameAndReciever_Username(friendship.getSender().getUsername(),friendship.getReciever().getUsername());
        if (s.isEmpty()){
            return false;
        }
        return true;
    }
    private boolean friendRequestExists(FriendRequest friendRequest){
        List<FriendRequest> r = friendRequestRepository.findAllByRequestSenderUsernameAndRequestRecieverUsername(friendRequest.getRequestSender().getUsername(),friendRequest.getRequestReciever().getUsername());
        if (r.isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public Friendship acceptRequest(FriendRequest friendRequest) {
        System.out.println(friendRequest.getId());
        friendRequest = friendRequestRepository.findById(friendRequest.getId());
        if (isNull(friendRequest)){
            System.out.println("No request with this id");
            return null;
        }
        friendRequestRepository.delete(friendRequest);
        Friendship friendship = new Friendship(friendRequest.getRequestSender(),friendRequest.getRequestReciever());
        friendshipRepository.save(friendship);
        return friendship;
    }

    @Override
    public FriendRequest declineRequest(FriendRequest friendRequest) {
        friendRequest = friendRequestRepository.findById(friendRequest.getId());
        if (isNull(friendRequest)){
            return null;
        }
        friendRequestRepository.delete(friendRequest);
        return friendRequest;
    }

    @Override
    public List<FriendRequest> getIncomingRequests(User user) {
        user = findByUsername(user.getUsername());
        if (isNull(user)){
            return null;
        }
        return friendRequestRepository.findAllByRequestReciever(user);
    }

    @Override
    public List<FriendRequest> getOutgoingRequests(User user) {
        user = findByUsername(user.getUsername());
        if (isNull(user)){
            return null;
        }
        return friendRequestRepository.findAllByRequestSender(user);
    }
}
