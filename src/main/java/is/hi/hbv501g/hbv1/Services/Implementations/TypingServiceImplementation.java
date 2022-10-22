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

    @Override
    public List<Word> getRandomWordsByLanguage(Lang lang) {
         return wordRepository.findAllByLanguage(lang);
    }

    @Override
    public List<Word> getRandomWords(Lang lang, int rank) {
        List<Word> words = wordRepository.findAllByLanguageAndAndRankLessThanEqual(lang,rank);
        //Þetta ætti ekki að þurfa en What do I know?
        //words.forEach(word -> {
        //    String decodedToUTF8 = new String(word.getText().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //    word.setText(decodedToUTF8);
        //});
        return words;
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
    public Quote getQuoteById(long quote_id) {
        return quoteRepository.findById(quote_id);
    }
}
