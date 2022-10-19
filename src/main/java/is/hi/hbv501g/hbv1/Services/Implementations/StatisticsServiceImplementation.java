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
    private final int KeystrokesPerWord = 5;
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
    public void addRandomAttempt(RandomAttempt randomAttempt) {
        updateStatsOfUser(randomAttempt.getUser(),randomAttempt);
        randomAttemptRepository.save(randomAttempt);
    }

    @Override
    public void addQuoteAttempt(QuoteAttempt quoteAttempt) {
        updateStatsOfUser(quoteAttempt.getUser(),quoteAttempt);
        quoteAttemptRepository.save(quoteAttempt);
    }

    @Override
    public void addQuoteAttempts(List<QuoteAttempt> quoteAttempts) {
        for (QuoteAttempt quoteAttempt:quoteAttempts) {
            updateStatsOfUser(quoteAttempt.getUser(),quoteAttempt);
            quoteAttemptRepository.save(quoteAttempt);
        }
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
        return over*100/total;
    }

    @Override
    public Stats getStatisticsOfUser(User user) {
        return statsRepository.findByUser(user);
    }

    @Override
    public Stats updateStatsOfUser(User user,QuoteAttempt quoteAttempt) {
        float time_in_ms = (quoteAttempt.getTime_finish().getTime()-quoteAttempt.getTime_start().getTime());
        System.out.println(time_in_ms);
        float time_in_s = time_in_ms/1000;
        float time = time_in_s/60;
        System.out.println(time);
        //TODO ákveða hvort við viljum miða þetta við correct eða keystrokes
        float wpm = (float) (quoteAttempt.getKeystrokes()/(float)KeystrokesPerWord)/time;
        System.out.println(wpm);
        float acc = (float) quoteAttempt.getCorrect()/(float) quoteAttempt.getKeystrokes();
        if (isNull(statsRepository.findByUser(user))){
            Stats stats = new Stats(user,wpm,acc,1,quoteAttempt.isCompleted() ? 1 : 0);
            statsRepository.save(stats);
            return stats;
        }
        if (!quoteAttempt.isCompleted()){
            return null;
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
        if (!quoteAttempt.isCompleted()){
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
        float time_in_ms = (randomAttempt.getTime_finish().getTime()-randomAttempt.getTime_start().getTime());
        System.out.println(time_in_ms);
        float time_in_s = time_in_ms/1000;
        float time = time_in_s/60;
        System.out.println(time);
        //TODO ákveða hvort við viljum miða þetta við correct eða keystrokes
        float wpm = (float) (randomAttempt.getKeystrokes()/(float)KeystrokesPerWord)/time;
        System.out.println(wpm);
        float acc = (float) randomAttempt.getCorrect()/(float) randomAttempt.getKeystrokes();
        if (isNull(statsRepository.findByUser(user))){
            Stats stats = new Stats(user,wpm,acc,1,randomAttempt.isCompleted() ? 1 : 0);
            statsRepository.save(stats);
            return stats;
        }
        if (!randomAttempt.isCompleted()){
            return null;
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
        return null;
    }
}
