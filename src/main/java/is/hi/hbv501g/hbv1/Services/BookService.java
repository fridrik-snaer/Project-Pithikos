package is.hi.hbv501g.hbv1.Services;

import is.hi.hbv501g.hbv1.Persistance.Entities.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book findByTitle(String title);
    List<Book> findAll();
    Optional<Book> findByID(long ID);
    Book save(Book book);
    void delete(Book book);
}
