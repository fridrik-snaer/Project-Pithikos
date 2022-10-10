package is.hi.hbv501g.hbv1.Controllers;


import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.Implementations.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private User loggedIn;
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    public UserController(UserServiceImplementation userServiceImplementation) {
        this.loggedIn = null;
        this.userServiceImplementation = userServiceImplementation;
    }
    @CrossOrigin
    @PostMapping(value="/login")
    public void login(){
        //Tímabundin prófun
        User user = new User("Fridrik","lykilord","fsb@gmail.com");
        loggedIn = user;
    }
    @CrossOrigin
    @RequestMapping(value="/getLogged", method= RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public User getLogged(){
        return loggedIn;
    }

    //Hér er hægt að setja skipunina:
    //curl -X POST -H "Content-Type: application/json" -d '{"username":"Fridrik","password":"lykilord","email":"fsb@gmail.com"}' localhost:8080/createAccount
    //inn og það virkar
    @CrossOrigin
    @PostMapping(value="/createAccount",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createAccount(@RequestBody User user){
        User user_use = new User(user.getUsername(),user.getPassword(), user.getEmail());
        userServiceImplementation.create(user_use);
    }
}
