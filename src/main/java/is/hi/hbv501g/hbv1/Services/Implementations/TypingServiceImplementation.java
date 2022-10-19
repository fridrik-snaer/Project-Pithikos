package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Persistence.Repositories.QuoteRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.WordRepository;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TypingServiceImplementation implements TypingService {
    private WordRepository wordRepository;
    private QuoteRepository quoteRepository;

    @Autowired
    public TypingServiceImplementation(WordRepository wordRepository,QuoteRepository quoteRepository) {
        this.wordRepository = wordRepository;
        this.quoteRepository = quoteRepository;
    }

    @Override
    public List<Word> getRandomWordsByLanguage(Lang lang) {
         return wordRepository.findAllByLanguage(lang);
    }

    @Override
    public List<Word> getRandomWords(Lang lang, int rank) {
        return wordRepository.findAllByLanguageAndAndRankLessThanEqual(lang,rank);
    }

    @Override
    public List<Word> getAllWords() {
        return wordRepository.findAll();
    }

    @Override
    public List<Quote> getQuotes(Lang lang) {
        return quoteRepository.findAllByAcceptedTrueAndDailyFalseAndLanguage(lang);
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

    @Override
    public Quote getQuoteById(long quote_id) {
        return quoteRepository.findById(quote_id);
    }
}
