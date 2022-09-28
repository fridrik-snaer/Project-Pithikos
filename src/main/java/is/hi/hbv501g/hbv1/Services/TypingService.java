package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;

import java.util.List;

public interface TypingService {
    List<Word> getRandomWords();
    List<Quote> getQuotes(Lang lang);
    List<Lesson> getLessons(int lvl, Lang lang);
    Quote getDailyChallenge(Lang lang);
    Quote getUnacceptedQuote(Lang lang);
    Quote submitQuote(Quote quote);
}
