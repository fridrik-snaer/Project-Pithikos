package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
import is.hi.hbv501g.hbv1.Persistence.Repositories.LessonRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.QuoteRepository;
import is.hi.hbv501g.hbv1.Persistence.Repositories.WordRepository;
import is.hi.hbv501g.hbv1.Services.TypingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TypingServiceImplementation implements TypingService {
    private WordRepository wordRepository;
    private QuoteRepository quoteRepository;
    private LessonRepository lessonRepository;
    private final long millisInDay = 86400000L;
    private final long the_first_day_millis = 1666457121515L;
    private Timestamp theFirstDay = new Timestamp(the_first_day_millis);

    @Autowired
    public TypingServiceImplementation(WordRepository wordRepository,QuoteRepository quoteRepository,LessonRepository lessonRepository) {
        this.wordRepository = wordRepository;
        this.quoteRepository = quoteRepository;
        this.lessonRepository = lessonRepository;
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
     * Returns all quotes of language lang, even those marked as unaccepted
     * @param lang The language of the quotes
     * @return List of all quotes of specified language excluding daily challenges
     */
    @Override
    public List<Quote> getQuotesWithNonAcc(Lang lang){
        return quoteRepository.findAllByDailyFalseAndLanguage(lang);
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
        return dailies.get((int)day_diff%dailies.size());
    }


    @Override
    public Quote getUnacceptedQuote(Lang lang) {
        return null;
    }

    /**
     * Adds a quote to the database marks it as unaccepted
     * @param quote Quote to be added
     * @return The quote with its newly generated id
     */
    @Override
    public Quote submitQuote(Quote quote) {
        Quote quote1 = new Quote(quote.getText(),quote.getLanguage(),quote.getOrigin(),false,false);
        quoteRepository.save(quote1);
        return quote;
    }

    @Override
    public List<Word> getAllWords() {
        return null;
    }

    /**
     * Get a lesson by lvl and language
     * @param lvl the level of the lesson
     * @param lang the language of the lesson
     * @return The lesson in question
     */
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

    /**
     * Deletes a quote if its unaccepted
     * @param quote
     */
    @Override
    public void deleteQuote(Quote quote) {
        Quote quote1 = getQuoteById(quote.getId());
        if (quote1.isAccepted()){
            return;
        }
        quoteRepository.delete(quote1);
    }

    /**
     * Finds a lesson by its id
     * @param id id of lesson
     * @return Lesson in question
     */
    @Override
    public Lesson getLessonByID(long id) {
        return lessonRepository.findById(id);
    }

    /**
     * Returns all lessons of language
     * @param lang langauge required
     * @return All lessons of language
     */
    @Override
    public List<Lesson> getLessonsByLanguage(Lang lang) {
        return lessonRepository.findAllByLangOrderByLvl(lang);
    }
}
