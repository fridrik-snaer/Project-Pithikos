package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.RandomAttempt;
import is.hi.hbv501g.hbv1.Persistence.Entities.User;
import is.hi.hbv501g.hbv1.Services.StatisticsService;
import is.hi.hbv501g.hbv1.Services.TypingService;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Clock;
import java.util.List;

@RestController
public class StatisticsController {
    private StatisticsService statisticsService;
    private UserService userService;
    private TypingService typingService;
    @Autowired
    public StatisticsController(StatisticsService statisticsService, UserService userService, TypingService typingService) {
        this.statisticsService = statisticsService;
        this.userService = userService;
        this.typingService = typingService;
    }
    //Þetta virkar ekki eins og er
    @CrossOrigin
    @RequestMapping(value="/getLeaderboardForQuote/{quote_id}",method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QuoteAttempt> getLeaderboardForQuote(@PathVariable long quote_id){
        //Tilraunastarfsemi
        System.out.println(quote_id);
        List<QuoteAttempt> attempts  = statisticsService.getLeaderboardForQuote(quote_id);
        for (QuoteAttempt attempt: attempts) {
            System.out.println(attempt);
        }
        return attempts;
    }

    @CrossOrigin
    @RequestMapping (value="/addQuoteAttempt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addQuoteAttempt(@RequestBody QuoteAttempt quoteAttempt){
        //Fáum notenda og quote sem hafa bara id og notum þau til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user og quote að fara á milli bak- og framenda
        quoteAttempt.setQuote(typingService.getQuoteById(quoteAttempt.getQuote().getId()));
        quoteAttempt.setUser(userService.findById(quoteAttempt.getUser().getId()));
        //Bara beint kall síðan
        statisticsService.addQuoteAttempt(quoteAttempt);
    }
    @CrossOrigin
    @RequestMapping (value="/addRandomAttempt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addRandomAttempt(@RequestBody RandomAttempt randomAttempt){
        //Fáum notenda sem hefur bara id og notum það til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user að fara á milli bak- og framenda
        randomAttempt.setUser(userService.findById(randomAttempt.getUser().getId()));
        statisticsService.addRandomAttempt(randomAttempt);
    }

    @CrossOrigin
    @RequestMapping (value="/addQuoteAttempts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addQuoteAttempts(@RequestBody List<QuoteAttempt> quoteAttempts){
        //Fáum notenda og quote sem hafa bara id og notum þau til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user og quote að fara á milli bak- og framenda
        for (QuoteAttempt quoteAttempt: quoteAttempts) {
            quoteAttempt.setQuote(typingService.getQuoteById(quoteAttempt.getQuote().getId()));
            quoteAttempt.setUser(userService.findById(quoteAttempt.getUser().getId()));
        }
        statisticsService.addQuoteAttempts(quoteAttempts);
    }



    //TODO eyða þessari aðferð
    //Hún er til að búa til dummy gögn til prófana
    @CrossOrigin
    @RequestMapping (value="/addAttempts")
    public void addAttempts(){
        User user = userService.findById(1);
        Quote quote = typingService.getQuoteById(2);
        for (int i = 0; i < 20; i++) {
            QuoteAttempt quoteAttempt = new QuoteAttempt(user,quote,new Timestamp(Clock.systemUTC().millis()),new Timestamp(Clock.systemUTC().millis()+1000*i),100,100-i,true,false,true);
            statisticsService.addQuoteAttempt(quoteAttempt);
        }
    }
}
