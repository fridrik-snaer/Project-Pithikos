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

@Service
public class StatisticsServiceImplementation implements StatisticsService {
    private final int LeaderboardLength = 10;
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
        randomAttemptRepository.save(randomAttempt);
    }

    @Override
    public void addQuoteAttempt(QuoteAttempt quoteAttempt) {
        quoteAttemptRepository.save(quoteAttempt);
    }

    @Override
    public void addQuoteAttempts(List<QuoteAttempt> quoteAttempts) {
        for (QuoteAttempt quoteAttempt:quoteAttempts) {
            quoteAttemptRepository.save(quoteAttempt);
        }
    }

    @Override
    public List<QuoteAttempt> getLeaderboardForQuote(long quote_id) {
        Quote quote = quoteRepository.findByID(quote_id);
        List<QuoteAttempt> qa = quoteAttemptRepository.findByQuote(quote);
        //TODO útfæra þetta betur
        qa.sort(QuoteAttempt::compareTo);
        List<QuoteAttempt> ret = new ArrayList<QuoteAttempt>();
        int retLength = Math.max(LeaderboardLength,qa.size());
        for (int i = 0; i < retLength; i++) {
            System.out.println("Bætt við");
            ret.add(qa.get(i));
        }
        return ret;
    }

    @Override
    public int getSpeedPercentileForQuoteAttempt(QuoteAttempt quoteAttempt) {
        return 0;
    }

    @Override
    public int getAccuracyPercentileForQuoteAttempt(QuoteAttempt quoteAttempt) {
        return 0;
    }

    @Override
    public Stats getStatisticsOfUser(User user) {
        return null;
    }

    @Override
    public Stats updateStatsOfUser(User user) {
        return null;
    }

    @Override
    public List<Stats> getAllStats() {
        return null;
    }
}
