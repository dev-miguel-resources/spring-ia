package mx.txalcala.springia.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import mx.txalcala.springia.models.Book;
import mx.txalcala.springia.repos.IBookRepo;
import mx.txalcala.springia.services.IBookService;

@Service
@AllArgsConstructor
public class BookServiceImpl implements IBookService {

    private final IBookRepo repo;

    @Override
    public Book save(Book Book) throws Exception {
        return repo.save(Book);
    }

    @Override
    public List<Book> saveAll(List<Book> list) throws Exception {
        return repo.saveAll(list);
    }

    @Override
    public List<Book> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Book findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Book());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }

    @Override
    public Book update(Book book, Integer id) throws Exception {
        return repo.save(book);
    }

}
