package is.hi.hbv501g.hbv1.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

@RestController
public class TypingController {

    private TypingService typingService;
    //TODO útfæra post request fyrir tungumál

    @Autowired
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }
    @CrossOrigin
    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllWords() {
        List<Word> words = typingService.getAllWords();
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = obj.writeValueAsString(words);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
    @CrossOrigin
    @RequestMapping(value="/words/{lang}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllWordsOfLanguage(@PathVariable String lang) {

        Lang language = Lang.valueOf(lang);
        List<Word> words = typingService.getRandomWordsByLanguage(language);
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = obj.writeValueAsString(words);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }
    @CrossOrigin
    @RequestMapping(value="/words/{lang}/{rank}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRandomWords(@PathVariable String lang,@PathVariable String rank) {
        int ranking  = Integer.parseInt(rank);
        Lang language = Lang.valueOf(lang);
        List<Word> words = typingService.getRandomWords(language,ranking);
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = obj.writeValueAsString(words);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    @CrossOrigin
    @RequestMapping(value="/quotes/{lang}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getQuotesByLanguage(@PathVariable String lang) {
        Lang language = Lang.valueOf(lang);
        List<Quote> quotes = typingService.getQuotes(language);
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = obj.writeValueAsString(quotes);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String indexPOST() {
        System.out.println("Hello World");
        return "";
    }
}
