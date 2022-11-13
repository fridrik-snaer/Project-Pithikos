package is.hi.hbv501g.hbv1.Controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
            return ResponseEntity.unprocessableEntity().body(null);
        }
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        user.clear();
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

    @CrossOrigin
    @RequestMapping(value="/friends/sendRequest",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FriendRequest> sendFriendRequest(@RequestBody FriendRequest friendRequest){
        System.out.println("Tried to send request");
        FriendRequest friendRequest1 = userService.sendFriendRequest(friendRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/friends/sendRequest").toUriString());
        if (isNull(friendRequest1)){
            return ResponseEntity.unprocessableEntity().body(null);
        }
        friendRequest1.getRequestReciever().clear();
        friendRequest1.getRequestSender().clear();
        return ResponseEntity.created(uri).body(friendRequest1);
    }

    @CrossOrigin
    @RequestMapping(value="/friends/sendRequest2",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendFriendRequest2(@RequestBody FriendRequest friendRequest){
        System.out.println("Tried to send request2");
        ResponseEntity response = userService.sendFriendRequestVol2(friendRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/friends/sendRequest").toUriString());
        if (response.getBody().getClass().equals("".getClass())){
            return response;
        }
        FriendRequest friendRequest1 = (FriendRequest)response.getBody();
        friendRequest1.getRequestReciever().clear();
        friendRequest1.getRequestSender().clear();
        return ResponseEntity.created(uri).header(response.getHeaders().toString()).body(friendRequest1);
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/acceptRequest",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity acceptRequest(@RequestBody FriendRequest friendRequest){
        System.out.println("Tried to accept request");
        Friendship friendship = userService.acceptRequest(friendRequest);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/friends/acceptRequest").toUriString());
        if (isNull(friendship)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        friendship.getReciever().clear();
        friendship.getSender().clear();
        return ResponseEntity.created(uri).body(friendship);
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/declineRequest",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity declineRequest(@RequestBody FriendRequest friendRequest){
        System.out.println("Tried to decline request");
        friendRequest = userService.declineRequest(friendRequest);
        if (isNull(friendRequest)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        friendRequest.getRequestReciever().clear();
        friendRequest.getRequestSender().clear();
        return ResponseEntity.accepted().build();
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/getIncomingRequests",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getIncomingRequests(@RequestBody User user){
        System.out.println("Tried to get incoming friend requests");
        List<FriendRequest> friendRequests = userService.getIncomingRequests(user);
        if (isNull(friendRequests)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        for (FriendRequest f:friendRequests) {
            f.getRequestSender().clear();
            f.getRequestReciever().clear();
        }
        return ResponseEntity.ok().body(friendRequests);
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/getOutgoingRequests",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getOutgoingRequests(@RequestBody User user){
        System.out.println("Tried to get outgoing friend requests");
        List<FriendRequest> friendRequests = userService.getOutgoingRequests(user);
        if (isNull(friendRequests)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        for (FriendRequest f:friendRequests) {
            f.getRequestSender().clear();
            f.getRequestReciever().clear();
        }
        return ResponseEntity.ok().body(friendRequests);
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/getFriends",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFriends(@RequestBody User user){
        System.out.println("Tried to get friends of user");
        List<User> friends = userService.getFriends(user);
        if (isNull(friends)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        for (User f:friends) {
            f.clear();
        }
        return ResponseEntity.ok().body(friends);
    }

    @CrossOrigin
    @RequestMapping(value = "/friends/getFriendsStats",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getFriendsStats(@RequestBody User user){
        System.out.println("Tried to get stats of friends of user");
        List<Stats> friendsStats = userService.getFriendsStats(user);
        if (isNull(friendsStats)){
            return ResponseEntity.unprocessableEntity().body("{\"error\":\"User not found\"}");
        }
        for (Stats f:friendsStats) {
            f.getUser().clear();
        }
        return ResponseEntity.ok().body(friendsStats);
    }


}
