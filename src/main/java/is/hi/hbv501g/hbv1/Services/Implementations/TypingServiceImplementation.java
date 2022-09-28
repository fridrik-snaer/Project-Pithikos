package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Services.TypingService;

import java.util.List;

public class TypingServiceImplementation implements TypingService {
    @Override
    public List<Word> getRandomWords() {
        return null;
    }

    @Override
    public List<Quote> getQuotes(Lang lang) {
        return null;
    }

    @Override
    public List<Lesson> getLessons(int lvl, Lang lang) {
        return null;
    }

    @Override
    public Quote getDailyChallenge(Lang lang) {
        return null;
    }

    @Override
    public Quote getUnacceptedQuote(Lang lang) {
        return null;
    }

    @Override
    public Quote submitQuote(Quote quote) {
        return null;
    }
}
