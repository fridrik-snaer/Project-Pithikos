package is.hi.hbv501g.hbv1.Persistance.Repositories;

import is.hi.hbv501g.hbv1.Persistance.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book save(Book book);
    void delete(Book book);
    List<Book> findAll();
    List<Book> findByTitle(String title);
    Optional<Book> findByID(long id);
}
