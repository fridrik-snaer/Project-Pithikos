package is.hi.hbv501g.hbv1.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String HomeController(){
        //Business logic
        //Call a method in a service class
        //Add some data to the model
        return "Home";
    }
}
