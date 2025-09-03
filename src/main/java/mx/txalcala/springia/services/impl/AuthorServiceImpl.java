package mx.txalcala.springia.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.models.Author;
import mx.txalcala.springia.repos.IAuthorRepo;
import mx.txalcala.springia.services.IAuthorService;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final IAuthorRepo repo;

    @Override
    public Author save(Author author) throws Exception {
        return repo.save(author);
    }

    @Override
    public List<Author> saveAll(List<Author> list) throws Exception {
        return repo.saveAll(list);
    }

    @Override
    public Author update(Author author, Integer id) throws Exception {
        return repo.save(author);
    }

    @Override
    public List<Author> findAll() throws Exception {
        return repo.findAll();
    }

    @Override
    public Author findById(Integer id) throws Exception {
        return repo.findById(id).orElse(new Author());
    }

    @Override
    public void delete(Integer id) throws Exception {
        repo.deleteById(id);
    }

}
