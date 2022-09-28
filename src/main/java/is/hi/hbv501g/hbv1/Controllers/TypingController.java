package is.hi.hbv501g.hbv1.Controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypingController {

    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String indexGET() {
        return "{\"words: [the, monkey, will, rule, the, world]\"}";
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String indexPOST() {
        System.out.println("Hello World");
        return "";
    }
}
