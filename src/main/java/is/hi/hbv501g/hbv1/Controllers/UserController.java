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
import java.util.Map;

import static java.util.Objects.isNull;

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

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request,response);
    }
}
