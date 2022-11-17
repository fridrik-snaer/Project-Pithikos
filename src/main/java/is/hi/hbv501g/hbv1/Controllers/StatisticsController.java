package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Services.StatisticsService;
import is.hi.hbv501g.hbv1.Services.TypingService;
import is.hi.hbv501g.hbv1.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles all the stat related endpoints of our api
 */
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

    /**
     * Fetches top 10 attempts at quote with respects to wpm.
     * @param quote_id Id of the quote in question
     * @return List of top 10 attempts including information about the user and the quote
     */
    @CrossOrigin
    @RequestMapping(value="/getLeaderboardForQuote/{quote_id}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<QuoteAttempt> getLeaderboardForQuote(@PathVariable long quote_id){
        //Tilraunastarfsemi
        System.out.println(quote_id);
        List<QuoteAttempt> attempts  = statisticsService.getLeaderboardForQuote(quote_id);
        for (QuoteAttempt attempt: attempts) {
            System.out.println(attempt);
        }
        return attempts;
    }

    /**
     * Returns the overall leaderboard of users stats based on avgWpm
     * @return The lists of stats where ovgWpm is the highest in order
     */
    @CrossOrigin
    @RequestMapping(value="/users/leaderboard/get",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Stats> getLeaderboardOfUsers(){

        return statisticsService.getLeaderBoardOfUsers();
    }

    /**
     * Adds an attempt by a user on a quote to the database, also results in an update of user statistics
     * @param quoteAttempt attempt to be saved in the database, must include id of quote and username of user
     * @return Attempt with its newly generated id
     */
    @CrossOrigin
    @RequestMapping (value="/addQuoteAttempt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public QuoteAttempt addQuoteAttempt(@RequestBody QuoteAttempt quoteAttempt){
        //Fáum notenda og quote sem hafa bara id og notum þau til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user og quote að fara á milli bak- og framenda
        quoteAttempt.setQuote(typingService.getQuoteById(quoteAttempt.getQuote().getId()));
        quoteAttempt.setUser(userService.findByUsername(quoteAttempt.getUser().getUsername()));
        //Bara beint kall síðan
        return statisticsService.addQuoteAttempt(quoteAttempt);
    }

    /**
     * Adds an attempt by a user on random words to the database, also results in an update of user statistics
     * @param randomAttempt attempt to be saved in the database, must include id of user
     * @return Attempt with its newly generated id
     */
    @CrossOrigin
    @RequestMapping (value="/addRandomAttempt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public RandomAttempt addRandomAttempt(@RequestBody RandomAttempt randomAttempt){
        //Fáum notenda sem hefur bara id og notum það til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user að fara á milli bak- og framenda
        randomAttempt.setUser(userService.findByUsername(randomAttempt.getUser().getUsername()));
        return statisticsService.addRandomAttempt(randomAttempt);
    }

    /**
     * Adds a lesson attempt to the database, does not the stats of the user with respect to the attempt
     * @param  lessonAttempt the attempts being added
     * @return The attempt being added along with its newly generated id for future reference
     */
    @CrossOrigin
    @RequestMapping (value="/lessons/addAttempt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public LessonAttempt addLessonAttempt(@RequestBody LessonAttempt lessonAttempt){
        lessonAttempt.setLesson(typingService.getLessonByID(lessonAttempt.getLesson().getId()));
        System.out.println("Þetta er lesson id: " + lessonAttempt.getLesson().getId());
        lessonAttempt.setUser(userService.findByUsername(lessonAttempt.getUser().getUsername()));
        //Bara beint kall síðan
        return statisticsService.addLessonAttempt(lessonAttempt);
    }

    /**
     * Adds an attempts by a user on quotes to the database, also results in an update of user statistics
     * @param quoteAttempts List of attempts to be saved in the database, must include id of quotes and user
     * @return List of attempts with their newly generated id
     */
    @CrossOrigin
    @RequestMapping (value="/addQuoteAttempts", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<QuoteAttempt> addQuoteAttempts(@RequestBody List<QuoteAttempt> quoteAttempts){
        //Fáum notenda og quote sem hafa bara id og notum þau til að sækja alla upplýsingar
        //Svo það þurfi ekki allar upplýsingar um user og quote að fara á milli bak- og framenda
        for (QuoteAttempt quoteAttempt: quoteAttempts) {
            quoteAttempt.setQuote(typingService.getQuoteById(quoteAttempt.getQuote().getId()));
            quoteAttempt.setUser(userService.findByUsername(quoteAttempt.getUser().getUsername()));
        }
        return statisticsService.addQuoteAttempts(quoteAttempts);
    }

    /**
     * Returns a quote attempts percentile ranking based on accuracy and typing speed
     * @param quoteAttempt_id id of quote attempt in question
     * @return the two percentile rankings
     */
    @CrossOrigin
    @RequestMapping (value="/getComparisons/{quoteAttempt_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int[] getComparisons(@PathVariable int quoteAttempt_id){
        int[] stats = new int[2];
        stats[0] = statisticsService.getSpeedPercentileForQuoteAttempt(quoteAttempt_id);
        stats[1] = statisticsService.getAccuracyPercentileForQuoteAttempt(quoteAttempt_id);
        return stats;
    }

    /**
     * Get statistic object of user
     * @param user containing only the id
     * @return statistic object of user of type stats
     */
    @CrossOrigin
    @RequestMapping (value="/getStatisticsOfUser", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Stats getStatisticsOfUser(@RequestBody User user){
        userService.findByUsername(user.getUsername());
        Stats stats = statisticsService.getStatisticsOfUser(user);
        stats.getUser().clear();
        return stats;
    }

    /**
     * Gets a list of all the lessons a user has completed so he can see it in the interface
     * @param user The user you want to see the lessons completed of
     * @param lang The language the user has completed the lessons in
     * @return The lessons of lang the user has completed
     */
    @CrossOrigin
    @RequestMapping (value="/lessons/getUserCompleted/{lang}", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Lesson> getUsersLessonsCompleted(@RequestBody User user,@PathVariable String lang){
        Lang language = Lang.valueOf(lang);
        User user1 = userService.findByUsername(user.getUsername());
        return statisticsService.getUsersLessonsCompleted(user1,language);
    }
}
