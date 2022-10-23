package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import is.hi.hbv501g.hbv1.Persistence.Repositories.QuoteAttemptRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.QuoteRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.RandomAttemptRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.StatsRepository;
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
    private StatsRepository statsRepository;
    private QuoteAttemptRepository quoteAttemptRepository;
    private RandomAttemptRepository randomAttemptRepository;
    private QuoteRepository quoteRepository;

    @Autowired
    public StatisticsServiceImplementation(StatsRepository statsRepository, QuoteAttemptRepository quoteAttemptRepository, RandomAttemptRepository randomAttemptRepository, QuoteRepository quoteRepository) {
        this.statsRepository = statsRepository;
        this.quoteAttemptRepository = quoteAttemptRepository;
        this.randomAttemptRepository = randomAttemptRepository;
        this.quoteRepository = quoteRepository;
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
            return 0;
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
            if (((float)quoteAttempt.getCorrect()/(float)quoteAttempt.getKeystrokes())<=((float)qa.getCorrect()/(float)qa.getKeystrokes())){
                over++;
            }
            total++;
        }
        if (total==0){
            return 0;
        }
        System.out.println("Over er : " + over);
        System.out.println("Total er : " + total);
        return over*100/total;
    }

    /**
     * Return the Stats object that User contains which includes statistical info about stats
     * @param user the user in question
     * @return a stats object with statistical info about user
     */
    @Override
    public Stats getStatisticsOfUser(User user) {
        return statsRepository.findByUser(user);
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
        //Uppfæra avg_wpm
        float old_avg_wpm = stats.getAvg_wpm();
        float new_avg_wpm = (old_avg_wpm*(float)stats.getTests_completed()+wpm)/(float)(stats.getTests_completed()+1);
        System.out.println(new_avg_wpm);
        stats.setAvg_wpm(new_avg_wpm);
        //Uppfæra avg_acc
        float old_avg_acc = stats.getAvg_acc();
        float new_avg_acc = (old_avg_acc*(float)stats.getTests_completed()+acc)/(float)(stats.getTests_completed()+1);
        stats.setAvg_acc(new_avg_acc);
        //Uppfæra completed
        if (quoteAttempt.isCompleted()){
            stats.setTests_completed(stats.getTests_completed()+1);
        }
        //Uppfæra tests taken
        stats.setTests_taken(stats.getTests_taken()+1);
        //Breytum gamla í nýja
        statsRepository.delete(statsRepository.findByUser(user));
        statsRepository.save(stats);
        //Skilum bara til gamans
        return stats;
    }

    /**
     * Updates the stats of a user in regard to a new random attempt
     * @param user the user whose stats need updating
     * @param quoteAttempt the randomattempt which needs to update according to
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
        //Uppfæra avg_wpm
        float old_avg_wpm = stats.getAvg_wpm();
        float new_avg_wpm = (old_avg_wpm*(float)stats.getTests_completed()+wpm)/(float)(stats.getTests_completed()+1);
        System.out.println(new_avg_wpm);
        stats.setAvg_wpm(new_avg_wpm);
        //Uppfæra avg_acc
        float old_avg_acc = stats.getAvg_acc();
        float new_avg_acc = (old_avg_acc*(float)stats.getTests_completed()+acc)/(float)(stats.getTests_completed()+1);
        stats.setAvg_acc(new_avg_acc);
        //Uppfæra completed
        if (!randomAttempt.isCompleted()){
            stats.setTests_completed(stats.getTests_completed()+1);
        }
        //Uppfæra tests taken
        stats.setTests_taken(stats.getTests_taken()+1);
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
}
