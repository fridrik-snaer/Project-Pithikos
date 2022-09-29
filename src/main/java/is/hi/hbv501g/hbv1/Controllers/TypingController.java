package is.hi.hbv501g.hbv1.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

@RestController
public class TypingController {

    private TypingService typingService;

    @Autowired
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    @RequestMapping(value="/", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String indexGET() {
        List<Word> words = typingService.getRandomWords();
        ObjectMapper obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = obj.writeValueAsString(words);
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
