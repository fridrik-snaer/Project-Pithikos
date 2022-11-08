package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Role;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

import static java.util.Objects.isNull;

/**
 * Handles user related endpoints and token refreshing
 */
@RestController @RequestMapping("/api") @RequiredArgsConstructor @Slf4j
public class UserController {
    private final UserService userService;

    /**
     * Creates account for user and saves it in the database if they do not exist there already
     * @param user the user to be created
     * @return the newly created user
     */
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

    /**
     * Creates a new role which can be added to users and adds it to the database
     * @param role the new role
     * @return the newly created role
     */
    @CrossOrigin
    @RequestMapping(value="/role/save")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    /**
     * Get new accessToken from a refresh token. This function behaves similarly to middleware
     * @param request the incoming request which must have Content-Type: application/json containing the accessToken.
     * @param response the response to be sent back.
     * @throws IOException
     */
    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Refresh controller");
        userService.refreshToken(request,response);
    }
}
