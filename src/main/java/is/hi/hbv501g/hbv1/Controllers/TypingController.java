package is.hi.hbv501g.hbv1.Controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TypingController {
    @Autowired
    private TypingService typingService;

    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String indexGET() {
        return "{\"words: [the, monkey, will, rule, the, world]\"}";
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String indexPOST() {
        Quote q = new Quote("To be or not to be", Lang.ISK,"Shakespeare");
        typingService.submitQuote(q);
        return "";
    }
}
