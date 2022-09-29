package is.hi.hbv501g.hbv1.Services.Implementations;

import is.hi.hbv501g.hbv1.Persistence.Entities.Lang;
import is.hi.hbv501g.hbv1.Persistence.Entities.Lesson;
import is.hi.hbv501g.hbv1.Persistence.Entities.Quote;
import is.hi.hbv501g.hbv1.Persistence.Entities.Word;
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

    @Autowired
    public TypingServiceImplementation(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @Override
    public List<Word> getRandomWords() {
         return wordRepository.findAll();
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
