package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.QuoteAttempt;
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
        System.out.println(quote_id);
        return statisticsService.getLeaderboardForQuote(quote_id);
    }
    //TODO eyða þessari aðferð
    @CrossOrigin
    @RequestMapping (value="/addAttempts")
    public void addAttempts(){
        User user = userService.findByID(1);
        Quote quote = typingService.getQuoteById(2);
        for (int i = 0; i < 20; i++) {
            QuoteAttempt quoteAttempt = new QuoteAttempt(user,quote,new Timestamp(Clock.systemUTC().millis()),new Timestamp(Clock.systemUTC().millis()+1000*i),100,100-i,true,false,true);
            statisticsService.addQuoteAttempt(quoteAttempt);
        }
    }
}
