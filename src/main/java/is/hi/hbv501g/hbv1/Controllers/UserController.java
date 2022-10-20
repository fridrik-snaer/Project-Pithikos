package is.hi.hbv501g.hbv1.Controllers;


import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.Implementations.UserServiceImplementation;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Objects.isNull;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userService = userServiceImplementation;
    }

    //Býr til nýjan account með username,password og email
    //Ef aðgangur er núþegar til er skilað https villu
    @CrossOrigin
    @RequestMapping (value="/createAccount",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createAccount(@RequestBody User user){
        User newUser = userService.saveUser(user);
        if(isNull(newUser)){
            //Spurning hvort við viljum skila einhverju
            return ResponseEntity.unprocessableEntity().body(null);
        }
        return ResponseEntity.ok().body(user);
    }
    //Til að breyta lykilorðum
    //Á enn eftir að útfæra örlítið vegna lykilorðabras
    @CrossOrigin
    @PostMapping(value="/changePassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody Map<String, String> json){
        int id = Integer.parseInt(json.get("user_id"));
        String password = json.get("password");
        userService.changePassword(id,password);
    }
}
