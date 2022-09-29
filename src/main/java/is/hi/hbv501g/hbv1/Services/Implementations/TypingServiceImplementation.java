package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Persistence.Repositories.LessonRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.QuoteRepository;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TypingServiceImplementation implements TypingService {
    private QuoteRepository quoteRepository;
    @Autowired
    public TypingServiceImplementation(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

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
        return quoteRepository.save(quote);
    }
}
