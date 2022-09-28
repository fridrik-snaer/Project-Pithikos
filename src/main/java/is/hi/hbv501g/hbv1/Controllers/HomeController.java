package is.hi.hbv501g.hbv1.Controllers;

import is.hi.hbv501g.hbv1.Persistance.Entities.Book;
import is.hi.hbv501g.hbv1.Services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {
    private BookService bookService;

    @Autowired
    public HomeController(BookService bookService){
        this.bookService=bookService;
    }

    @RequestMapping("/")
    public String homePage(Model model){
        //Business logic
        //Not here
        //Call a method in a service class
        List<Book> allBooks = bookService.findAll();
        //Add some data to the model
        model.addAttribute("books",allBooks);
        return "Home";
    }
    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    public String deleteBook(@PathVariable("id") long id, Model model){
        //Business logic
        Optional<Book> bookToDelete = bookService.findByID(id);
        if (bookToDelete.isPresent()){
            bookService.delete((bookToDelete.get()));
        }
        return "redirect:/";
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public String addBook(Book book, BindingResult result, Model model){
        if (result.hasErrors()){
            return "newBook";
        }
        bookService.save(book);
        return "redirect:/";
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.GET)
    public String addBook(Book book){
        return "newBook";
    }
}
