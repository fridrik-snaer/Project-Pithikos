package is.hi.hbv501g.hbv1.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.hbv501g.hbv1.Persistence.Entities.Role;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.Implementations.UserServiceImplementation;
import is.hi.hbv501g.hbv1.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController @RequestMapping("/api") @RequiredArgsConstructor @Slf4j
public class UserController {
    private final UserService userService;

    @CrossOrigin
    @RequestMapping(value="/createAccount",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createAccount(@RequestBody User user){
        User newUser = userService.saveUser(user);
        if(isNull(newUser)){
            //Spurning hvort við viljum skila einhverju
            return ResponseEntity.unprocessableEntity().body(null);
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(user);
    }

    @CrossOrigin
    @RequestMapping(value="/role/save")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @CrossOrigin
    @PostMapping(value="/changePassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody Map<String, String> json){
        int id = Integer.parseInt(json.get("user_id"));
        String password = json.get("password");
        userService.changePassword(id,password);
    }

    //TODO: Refactor this out of the Controller into the UserService
    @GetMapping("/token/refresh")
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
                User user = userService.getUser(username);

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
}
