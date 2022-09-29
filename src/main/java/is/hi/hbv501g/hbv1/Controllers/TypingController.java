package is.hi.hbv501g.hbv1.Controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class TypingController {

    //@RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    //public String indexGET() {
    //    return "{\"words: [the, monkey, will, rule, the, world]\"}";
    //}


    // Er að nota þetta method til að test react framendann
    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String[] indexGETArray() {
        String[] string = {"the", "monkey", "will", "rule", "the", "world"};
        return string;
    }


    @RequestMapping(value="/", method = RequestMethod.POST)
    public String indexPOST() {
        System.out.println("Hello World");
        return "Poop";
    }
}
