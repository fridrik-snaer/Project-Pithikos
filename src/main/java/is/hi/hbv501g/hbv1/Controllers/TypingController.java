package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles all typing data related endpoints
 */
@RestController @Slf4j
@RequestMapping("/api")
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
    //TODO: refactor path variables to use ?key=value&key2=value2
    @CrossOrigin
    @RequestMapping(value="/words/{lang}/{rank}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Word> getRandomWords(@PathVariable String lang,@PathVariable String rank) {
        return typingService.getRandomWords(Lang.valueOf(lang),Integer.parseInt(rank));
    }

    /**
     * A method to fetch quotes for the typing game based on language
     * @param lang specifies the language of the requested quotes
     * @return returns all words in specified language
     */
    @CrossOrigin
    @RequestMapping(value="/quotes/{lang}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Quote> getQuotesByLanguageAccepted(@PathVariable String lang) {
        return typingService.getQuotes(Lang.valueOf(lang));
    }

    /**
     * A method to fetch quotes for the typing game based on language
     * @param lang specifies the language of the requested quotes
     * @return returns all words in specified language
     */
    @CrossOrigin
    @RequestMapping(value="/quotesWithNonAcc/{lang}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Quote> getQuotesByLanguage(@PathVariable String lang) {
        return typingService.getQuotesWithNonAcc(Lang.valueOf(lang));
    }

    /**
     * Deletes a quote if its unaccepted
     * @param quote
     */
    @CrossOrigin
    @RequestMapping(value="/deleteQuote", method= RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public void deleteQuote(@RequestBody Quote quote) {
        typingService.deleteQuote(quote);
    }

    /**
     * Returns the quote of the day, all requests in the span of 24 hours return the same quote
     * @param lang language of quote
     * @return The same quote in the span of 24 hours
     */
    @CrossOrigin
    @RequestMapping(value="/getDailyQuote/{lang}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Quote getDailyChallengeByLanguage(@PathVariable String lang) {
        return typingService.getDailyChallenge(Lang.valueOf(lang));
    }

    /**
     * Returns all lessons of language
     * @param lang langauge required
     * @return All lessons of language
     */
    @CrossOrigin
    @RequestMapping(value="/lessons/getByLanguage/{lang}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public List<Lesson> getLessonsByLanguage(@PathVariable String lang) {
        return typingService.getLessonsByLanguage(Lang.valueOf(lang));
    }

    /**
     * Get a lesson by lvl and language
     * @param lvl the level of the lesson
     * @param lang the language of the lesson
     * @return The lesson in question
     */
    @CrossOrigin
    @RequestMapping(value="/lessons/getByLanguage/{lang}/{lvl}", method= RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Lesson getLessonsByLanguageAndLvl(@PathVariable String lang,@PathVariable int lvl) {
        List<Lesson> lessons = typingService.getLessonsByLanguage(Lang.valueOf(lang));
        if (lessons.size()<lvl){
            return null;
        }
        return lessons.get(lvl-1);
    }

    /**
     * A way to submit quotes, quote object preferrably includes text, language and origin
     * @param quote The quote to be submitted
     */
    @CrossOrigin
    @RequestMapping(value="/submitQuote", method= RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public void submitQuote(@RequestBody Quote quote) {
        typingService.submitQuote(quote);
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
