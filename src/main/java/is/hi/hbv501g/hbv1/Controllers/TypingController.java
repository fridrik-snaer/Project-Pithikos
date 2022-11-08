package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController @Slf4j
public class TypingController {
    private final TypingService typingService;
    @Autowired
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    /**
     * API homescreen, returns a list of all posssible endpoints and info about them
     * Should not be available in production
     */
    @CrossOrigin
    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Greeting getIndex() {
        //TODO: Bæta við öllum endpoints
        log.info("User just submitted");
        Endpoint endpoint = new Endpoint("/api/login", "http", List.of("POST"));
        Endpoint wordendpoint = new Endpoint("/api/words/{lang}/{rank}", "http", List.of("POST"));
        return new Greeting("Welcome to the api!", List.of(wordendpoint,endpoint));
    }

    /**
     * A method to fetch words for the typing game based on language and frequency rank
     * @param lang specifies the language of the requested words
     * @param rank specifies the rank of the least frequent word to be fetched.
     * @return returns all words in specified language with frequency higher or equal to
     * the specified rank
     */
    @CrossOrigin
    @RequestMapping(value="/words/{lang}/{rank}", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<Word> getRandomWords(@PathVariable String lang,@PathVariable String rank) {
        return typingService.getRandomWords(Lang.valueOf(lang),Integer.parseInt(rank));
    }

    /**
     * A method to fetch quotes for the typing game based on language
     * @param lang specifies the language of the requested quotes
     * @return returns all words in specified language
     */
    @CrossOrigin
    @RequestMapping(value="/quotes/{lang}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Quote> getQuotesByLanguage(@PathVariable String lang) {
        return typingService.getQuotes(Lang.valueOf(lang));
    }

    //Temporary classes used for enpoint displaying
    //TODO: replace these with a HashMap
    @Data
    class Endpoint {
        String href;
        String type;
        List<String> methods;

        public Endpoint(String href, String type, List<String> methods) {
            this.href = href;
            this.type = type;
            this.methods = methods;
        }
    }

    @Data
    class Greeting{
        String message;
        List<Endpoint> endpoints;

        public Greeting(String message, List<Endpoint> endpoints) {
            this.message = message;
            this.endpoints = endpoints;
        }
    }

}
