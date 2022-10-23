package is.hi.hbv501g.hbv1.Services.Implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final RelationshipRepository relationshipRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("Saved new user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
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

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + (10 * 60 * 1000))) //access Token expires in 10 minutes
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

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
