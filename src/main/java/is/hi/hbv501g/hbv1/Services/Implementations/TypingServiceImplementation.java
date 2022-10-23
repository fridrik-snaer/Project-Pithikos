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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TypingServiceImplementation implements TypingService {
    private WordRepository wordRepository;
    private QuoteRepository quoteRepository;
    private final long millisInDay = 86400000L;
    private final long the_first_day_millis = 1666457121515L;
    private Timestamp theFirstDay = new Timestamp(the_first_day_millis);

    @Autowired
    public TypingServiceImplementation(WordRepository wordRepository,QuoteRepository quoteRepository) {
        this.wordRepository = wordRepository;
        this.quoteRepository = quoteRepository;
    }

    /**
     * Returns all words of a certain language
     * @param lang the language you want
     * @return all words of that language
     */
    @Override
    public List<Word> getRandomWordsByLanguage(Lang lang) {
         return wordRepository.findAllByLanguage(lang);
    }

    /**
     * Returns all words of a certain language with a rank higher than rank
     * @param lang the language you want
     * @param rank the max rank you want
     * @return a list the size og rank of the rank most common words of language lang
     */
    @Override
    public List<Word> getRandomWords(Lang lang, int rank) {
        List<Word> words = wordRepository.findAllByLanguageAndAndRankLessThanEqual(lang,rank);
        return words;
    }

    /**
     * Gets all quotes of a certain language
     * @param lang the language in question
     * @return all quotes of the language
     */
    @Override
    public List<Quote> getQuotes(Lang lang) {
        return quoteRepository.findAllByAcceptedTrueAndDailyFalseAndLanguage(lang);
    }

    /**
     * Returns the quote of the day, all requests in the span of 24 hours return the same quote
     * @param lang language of quote
     * @return The same quote in the span of 24 hours
     */
    @Override
    public Quote getDailyChallenge(Lang lang) {
        long diff = System.currentTimeMillis()-theFirstDay.getTime();
        long day_diff = diff/millisInDay;
        List<Quote> dailies = quoteRepository.findAllByDailyTrue();
        return dailies.get((int)day_diff);
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
    public List<Word> getAllWords() {
        return null;
    }

    @Override
    public List<Lesson> getLessons(int lvl, Lang lang) {
        return null;
    }

    /**
     * Finds a quote by its id
     * @param quote_id the id of the quote
     * @return the quote
     */
    @Override
    public Quote getQuoteById(long quote_id) {
        return quoteRepository.findById(quote_id);
    }
}
