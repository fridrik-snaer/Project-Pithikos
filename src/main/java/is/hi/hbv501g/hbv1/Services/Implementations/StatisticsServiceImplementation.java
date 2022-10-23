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

    @Override
    public RandomAttempt addRandomAttempt(RandomAttempt randomAttempt) {
        updateStatsOfUser(randomAttempt.getUser(),randomAttempt);
        RandomAttempt r = randomAttemptRepository.save(randomAttempt);
        r.getUser().clear();
        return r;
    }

    @Override
    public QuoteAttempt addQuoteAttempt(QuoteAttempt quoteAttempt) {
        updateStatsOfUser(quoteAttempt.getUser(),quoteAttempt);
        QuoteAttempt q = quoteAttemptRepository.save(quoteAttempt);
        q.getUser().clear();
        System.out.println("Hér er q-Id "+ quoteAttempt.getQuote().getId());
        return q;
    }

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

    @Override
    public Stats getStatisticsOfUser(User user) {
        return statsRepository.findByUser(user);
    }

    //Uppfærir stats á User alltaf þegar bætt er við nýrri tilraun
    //Fullútfært
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

    @Override
    public List<Stats> getAllStats() {
        return statsRepository.findAll();
    }
}
