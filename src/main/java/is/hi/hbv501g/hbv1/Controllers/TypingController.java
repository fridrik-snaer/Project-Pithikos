package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
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
        Endpoint endpoint = new Endpoint("/login/api", "http", List.of("POST"));
        Greeting greeting = new Greeting("Welcome to the api!", List.of(endpoint));
        return greeting;
    }

    /**
     * A method to fetch words for the typing game based on language and frequency rank
     * @param lang specifies the language of the requested words
     * @param rank specifies the rank of the least frequent word to be fetched.
     * @return returns all words in specified language with frequency higher or equal to
     * the specified rank
     */
    @CrossOrigin
    @RequestMapping(value="/words/{lang}/{rank}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Word> getRandomWords(@PathVariable String lang,@PathVariable String rank) {
        int ranking  = Integer.parseInt(rank);
        Lang language = Lang.valueOf(lang);
        List<Word> words = typingService.getRandomWords(language,ranking);
        return words;
    }

    /**
     * A method to fetch quotes for the typing game based on language
     * @param lang specifies the language of the requested quotes
     * @return returns all words in specified language
     */
    @CrossOrigin
    @RequestMapping(value="/quotes/{lang}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Quote> getQuotesByLanguage(@PathVariable String lang) {
        Lang language = Lang.valueOf(lang);
        List<Quote> quotes = typingService.getQuotes(language);
        return quotes;
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
