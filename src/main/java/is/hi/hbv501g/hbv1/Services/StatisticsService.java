package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.*;
import java.util.List;

public interface StatisticsService {
    RandomAttempt addRandomAttempt(RandomAttempt randomAttempt);
    QuoteAttempt addQuoteAttempt(QuoteAttempt quoteAttempt);
    List<QuoteAttempt> addQuoteAttempts(List<QuoteAttempt> quoteAttempts);
    List<QuoteAttempt> getLeaderboardForQuote(long quote_id);
    int getSpeedPercentileForQuoteAttempt(long quoteAttempt_id);
    int getAccuracyPercentileForQuoteAttempt(long quoteAttempt_id);
    Stats getStatisticsOfUser(User user);
    Stats updateStatsOfUser(User user,QuoteAttempt quoteAttempt);
    Stats updateStatsOfUser(User user,RandomAttempt randomAttempt);
    List<Stats> getAllStats();

    LessonAttempt addLessonAttempt(LessonAttempt lessonAttempt);

    List<Lesson> getUsersLessonsCompleted(User user,Lang lang);

    List<Stats> getLeaderBoardOfUsers();
}
