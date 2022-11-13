package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Persistence.Repositories.*;
import is.hi.hbv501g.hbv1.Services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class StatisticsServiceImplementation implements StatisticsService {
    private final int LeaderboardLength = 10;
    private final float KeystrokesPerWord = (float)5.156;
    private final int AttemptsToAccept = 10;
    private final StatsRepository statsRepository;
    private final QuoteAttemptRepository quoteAttemptRepository;
    private final RandomAttemptRepository randomAttemptRepository;
    private final LessonAttemptRepository lessonAttemptRepository;
    private final QuoteRepository quoteRepository;
    private final UserRepository userRepository;

    @Autowired
    public StatisticsServiceImplementation(UserRepository userRepository,StatsRepository statsRepository, QuoteAttemptRepository quoteAttemptRepository, RandomAttemptRepository randomAttemptRepository, QuoteRepository quoteRepository,LessonAttemptRepository lessonAttemptRepository) {
        this.statsRepository = statsRepository;
        this.quoteAttemptRepository = quoteAttemptRepository;
        this.randomAttemptRepository = randomAttemptRepository;
        this.quoteRepository = quoteRepository;
        this.lessonAttemptRepository = lessonAttemptRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds a random attempt to the database, also updates the stats of the user with respect to the attempt
     * @param randomAttempt the attempt being added
     * @return The attempt being added along with its newly generated id for future reference
     */
    @Override
    public RandomAttempt addRandomAttempt(RandomAttempt randomAttempt) {
        updateStatsOfUser(randomAttempt.getUser(),randomAttempt);
        RandomAttempt r = randomAttemptRepository.save(randomAttempt);
        //To prevent infinite recursions
        r.getUser().clear();
        return r;
    }

    /**
     * Adds a quote attempt to the database, also updates the stats of the user with respect to the attempt
     * @param quoteAttempt the attempt being added
     * @return The attempt being added along with its newly generated id for future reference
     */
    @Override
    public QuoteAttempt addQuoteAttempt(QuoteAttempt quoteAttempt) {
        boolean quoteAccepted = quoteAttempt.getQuote().isAccepted();
        boolean attemptedEnough = quoteAttemptRepository.countAllByQuote(quoteAttempt.getQuote())>AttemptsToAccept;
        if (!quoteAccepted && attemptedEnough){
                quoteAttempt.getQuote().setAccepted(true);
                quoteRepository.save(quoteAttempt.getQuote());
        }
        updateStatsOfUser(quoteAttempt.getUser(),quoteAttempt);
        QuoteAttempt q = quoteAttemptRepository.save(quoteAttempt);
        q.getUser().clear();
        return q;
    }

    /**
     * Adds quote attempts to the database, also updates the stats of the user with respect to the attempts
     * @param  quoteAttempts the attempts being added
     * @return The attempts being added along with their newly generated id for future reference
     */
    @Override
    public List<QuoteAttempt> addQuoteAttempts(List<QuoteAttempt> quoteAttempts) {
        List<QuoteAttempt> quoteAttemptList = new ArrayList<QuoteAttempt>();
        QuoteAttempt q;
        for (QuoteAttempt quoteAttempt:quoteAttempts) {
            updateStatsOfUser(quoteAttempt.getUser(),quoteAttempt);
            q = quoteAttemptRepository.save(quoteAttempt);
            q.getUser().clear();
            quoteAttemptList.add(q);
        }
        return quoteAttemptList;
    }

    @Override
    public LessonAttempt addLessonAttempt(LessonAttempt lessonAttempt) {
        LessonAttempt l = lessonAttemptRepository.save(lessonAttempt);
        l.getUser().clear();
        return l;
    }

    /**
     * Returns the leaderboard for a quote in regard to speed
     * @param quote_id Id of quote in question
     * @return The quoteAttempts with info about the user and quote
     */
    @Override
    public List<QuoteAttempt> getLeaderboardForQuote(long quote_id) {
        Quote quote = quoteRepository.findById(quote_id);
        List<QuoteAttempt> qa = quoteAttemptRepository.findByQuote(quote);
        //TODO útfæra þetta betur
        qa.sort(QuoteAttempt::compareTo);
        List<QuoteAttempt> ret = new ArrayList<QuoteAttempt>();
        int retLength = Math.max(LeaderboardLength,qa.size());
        for (int i = 0; i < retLength; i++) {
            System.out.println("Bætt við");
            qa.get(i).getUser().clear();
            ret.add(qa.get(i));
        }
        return ret;
    }

    @Override
    public List<Stats> getLeaderBoardOfUsers() {
        //return null;
        List<Stats> stats = statsRepository.findTop10ByOrderByAvgWpm();
        int leaderboard = Math.max(stats.size(),LeaderboardLength);
        for (Stats s: stats) {
            s.getUser().clear();
        }
        return stats.subList(0,leaderboard);
    }

    /**
     * Calculates the ranking of a quoteAttempt in comparisons to others in regard to speed
     * @param quoteAttempt_id Id of quoteAttempts in question
     * @return The attempt was in the top "return value"% of all attempts
     */
    @Override
    public int getSpeedPercentileForQuoteAttempt(long quoteAttempt_id) {
        System.out.println(quoteAttempt_id);
        QuoteAttempt quoteAttempt = quoteAttemptRepository.findById(quoteAttempt_id);
        List<QuoteAttempt> quoteAttempts = quoteAttemptRepository.findByQuote(quoteAttempt.getQuote());
        int total = 0;
        int over = 0;
        for (QuoteAttempt qa:quoteAttempts) {
            if (((quoteAttempt.getTime_finish().getTime()-quoteAttempt.getTime_start().getTime())>=(qa.getTime_finish().getTime()-qa.getTime_start().getTime()))){
                over++;
            }
            total++;
        }
        if (total==0){
            return 1;
        }
        if (over==0 || total==over){
            return 99;
        }
        return over*100/total;
    }
    /**
     * Calculates the ranking of a quoteAttempt in comparisons to others in regard to accuracy
     * @param quoteAttempt_id Id of quoteAttempts in question
     * @return The attempt was in the top "return value"% of all attempts
     */
    @Override
    public int getAccuracyPercentileForQuoteAttempt(long quoteAttempt_id) {
        QuoteAttempt quoteAttempt = quoteAttemptRepository.findById(quoteAttempt_id);
        List<QuoteAttempt> quoteAttempts = quoteAttemptRepository.findByQuote(quoteAttempt.getQuote());
        int total = 0;
        int over = 0;
        for (QuoteAttempt qa:quoteAttempts) {
            if (((float)quoteAttempt.getCorrect()/(float)quoteAttempt.getKeystrokes())>=((float)qa.getCorrect()/(float)qa.getKeystrokes())){
                over++;
            }
            total++;
        }
        if (total==0 || total==over){
            return 1;
        }
        if (over==0){
            return 99;
        }
        return 100-(over*100/total);
    }

    /**
     * Return the Stats object that User contains which includes statistical info about stats
     * @param user the user in question
     * @return a stats object with statistical info about user
     */
    @Override
    public Stats getStatisticsOfUser(User user) {
        user = userRepository.findByUsername(user.getUsername());
        Stats stats = statsRepository.findByUser(user);
        stats.getUser().clear();
        return stats;
    }

    /**
     * Updates the stats of a user in regard to a new quote attempt
     * @param user the user whose stats need updating
     * @param quoteAttempt the quoteattempt which needs to update according to
     * @return the updated stats
     */
    @Override
    public Stats updateStatsOfUser(User user,QuoteAttempt quoteAttempt) {
        //Uppfærum ekki neitt ef ekki var klárað tilraunina
        if (!quoteAttempt.isCompleted()){
            return null;
        }
        //System.out.println(quoteAttempt);
        //Reiknum út tíma tilraunar í mínutum
        float time_in_ms = (quoteAttempt.getTime_finish().getTime()-quoteAttempt.getTime_start().getTime());
        float time_in_s = time_in_ms/1000;
        float time = time_in_s/60;
        //TODO ákveða hvort við viljum miða þetta við correct eða keystrokes
        //Reiknum wpm á þessari tilraun
        float wpm = (float) (quoteAttempt.getKeystrokes()/(float)KeystrokesPerWord)/time;
        //Reiknum acc á þessari tilraun
        float acc = (float) quoteAttempt.getCorrect()/(float) quoteAttempt.getKeystrokes();
        //Ef stats eru ekki til þá þarf sértilfælli
        if (isNull(statsRepository.findByUser(user))){
            Stats stats = new Stats(user,wpm,acc,1,quoteAttempt.isCompleted() ? 1 : 0);
            statsRepository.save(stats);
            return stats;
        }
        Stats stats = statsRepository.findByUser(user);
        //Uppfæra avgWpm
        float old_avgWpm = stats.getAvgWpm();
        float new_avgWpm = (old_avgWpm*(float)stats.getTestsCompleted()+wpm)/(float)(stats.getTestsCompleted()+1);
        System.out.println(new_avgWpm);
        stats.setAvgWpm(new_avgWpm);
        //Uppfæra avg_acc
        float old_avg_acc = stats.getAvgAcc();
        float new_avg_acc = (old_avg_acc*(float)stats.getTestsCompleted()+acc)/(float)(stats.getTestsCompleted()+1);
        stats.setAvgAcc(new_avg_acc);
        //Uppfæra completed
        if (quoteAttempt.isCompleted()){
            stats.setTestsCompleted(stats.getTestsCompleted()+1);
        }
        //Uppfæra tests taken
        stats.setTestsTaken(stats.getTestsTaken()+1);
        //Breytum gamla í nýja
        statsRepository.delete(statsRepository.findByUser(user));
        statsRepository.save(stats);
        //Skilum bara til gamans
        return stats;
    }

    /**
     * Updates the stats of a user in regard to a new random attempt
     * @param user the user whose stats need updating
     * @param randomAttempt the randomattempt which needs to update according to
     * @return the updated stats
     */
    @Override
    public Stats updateStatsOfUser(User user,RandomAttempt randomAttempt) {
        if (!randomAttempt.isCompleted()){
            return null;
        }
        float time_in_ms = (randomAttempt.getTime_finish().getTime()-randomAttempt.getTime_start().getTime());
        float time_in_s = time_in_ms/1000;
        float time = time_in_s/60;
        //TODO ákveða hvort við viljum miða þetta við correct eða keystrokes
        float wpm = (float) (randomAttempt.getKeystrokes()/(float)KeystrokesPerWord)/time;
        float acc = (float) randomAttempt.getCorrect()/(float) randomAttempt.getKeystrokes();
        if (isNull(statsRepository.findByUser(user))){
            Stats stats = new Stats(user,wpm,acc,1,randomAttempt.isCompleted() ? 1 : 0);
            statsRepository.save(stats);
            return stats;
        }
        Stats stats = statsRepository.findByUser(user);
        //Uppfæra avgWpm
        float old_avgWpm = stats.getAvgWpm();
        float new_avgWpm = (old_avgWpm*(float)stats.getTestsCompleted()+wpm)/(float)(stats.getTestsCompleted()+1);
        System.out.println(new_avgWpm);
        stats.setAvgWpm(new_avgWpm);
        //Uppfæra avg_acc
        float old_avg_acc = stats.getAvgAcc();
        float new_avg_acc = (old_avg_acc*(float)stats.getTestsCompleted()+acc)/(float)(stats.getTestsCompleted()+1);
        stats.setAvgAcc(new_avg_acc);
        //Uppfæra completed
        if (!randomAttempt.isCompleted()){
            stats.setTestsCompleted(stats.getTestsCompleted()+1);
        }
        //Uppfæra tests taken
        stats.setTestsTaken(stats.getTestsTaken()+1);
        //Breytum gamla í nýja
        statsRepository.delete(statsRepository.findByUser(user));
        statsRepository.save(stats);
        //Skilum bara til gamans
        return stats;
    }

    /**
     * Gives all stats objects in database
     * @return all stats object in database
     */
    @Override
    public List<Stats> getAllStats() {
        return statsRepository.findAll();
    }

    @Override
    public List<Lesson> getUsersLessonsCompleted(User user,Lang lang) {
        List<Lesson> lessons = new ArrayList<>();
        List<LessonAttempt> attempts = lessonAttemptRepository.findByUserAndLessonLang(user,lang);
        if (attempts.size()==0){return null;}
        for (LessonAttempt la: attempts) {
            Lesson l = la.getLesson();
            if (!lessons.contains(l)){
                lessons.add(l);
            }
        }
        return lessons;
    }
}
