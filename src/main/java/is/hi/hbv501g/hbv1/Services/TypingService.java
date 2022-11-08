package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TypingService {
    List<Word> getRandomWords(Lang lang, int rank);
    List<Word> getAllWords();
    List<Word> getRandomWordsByLanguage(Lang lang);
    List<Quote> getQuotes(Lang lang);
    List<Lesson> getLessons(int lvl, Lang lang);
    Quote getDailyChallenge(Lang lang);
    Quote getUnacceptedQuote(Lang lang);
    Quote submitQuote(Quote quote);
    Quote getQuoteById(long quote_id);
    List<Quote> getQuotesWithNonAcc(Lang valueOf);

    void deleteQuote(Quote quote);
}
