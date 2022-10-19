package is.hi.hbv501g.hbv1.Controllers;


import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.Implementations.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.util.Objects.isNull;

@RestController
public class UserController {
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    public UserController(UserServiceImplementation userServiceImplementation) {
        this.userServiceImplementation = userServiceImplementation;
    }
    @CrossOrigin
    @GetMapping(value="/login")
    public void login(){
        //Tímabundin prófun
        User user = userServiceImplementation.findById(1);
        System.out.println("UserController user: " + user);
        userServiceImplementation.login(user);
    }

    @CrossOrigin
    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody User user){
        String token = userServiceImplementation.login(user);
        return token;
    }

    @CrossOrigin
    @RequestMapping(value="/getLogged", method= RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public User getLogged(){
        return userServiceImplementation.getLogged();
    }

    //Hér er hægt að setja skipunina:
    //curl -X POST -H "Content-Type: application/json" -d '{"username":"Fridrik","password":"lykilord","email":"fsb@gmail.com"}' localhost:8080/createAccount
    //inn og það virkar
    @CrossOrigin
    @RequestMapping (value="/createAccount",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createAccount(@RequestBody User user){
        User user_new = new User(user.getUsername(),user.getPassword(), user.getEmail());
        User user1 = userServiceImplementation.create(user_new);
        if(isNull(user1)){
            //Spurning hvort við viljum skila einhverju
            System.out.println("Reynt að gera user sem er núþegar til");
            return ResponseEntity.unprocessableEntity().body(null);
        }
        return ResponseEntity.ok().body(user);
    }

    @CrossOrigin
    @PostMapping(value="/changePassword",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody Map<String, String> json){
        int id = Integer.parseInt(json.get("user_id").toString());
        String password = json.get("password").toString();
        userServiceImplementation.changePassword(id,password);
    }
}
