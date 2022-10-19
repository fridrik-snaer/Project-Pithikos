package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;

import java.util.List;

public interface StatisticsService {
    void addRandomAttempt(RandomAttempt randomAttempt);
    void addQuoteAttempt(QuoteAttempt quoteAttempt);
    void addQuoteAttempts(List<QuoteAttempt> quoteAttempts);
    List<QuoteAttempt> getLeaderboardForQuote(long quote_id);
    int getSpeedPercentileForQuoteAttempt(QuoteAttempt quoteAttempt);
    int getAccuracyPercentileForQuoteAttempt(QuoteAttempt quoteAttempt);
    Stats getStatisticsOfUser(User user);
    Stats updateStatsOfUser(User user);
    List<Stats> getAllStats();
}
