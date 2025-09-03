package mx.txalcala.springia.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.models.Author;
import mx.txalcala.springia.services.IAuthorService;

@RestController()
@RequestMapping("/authors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthorController {

    private final IAuthorService service;

    @GetMapping
    public ResponseEntity<List<Author>> findAll() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Author> save(@RequestBody Author author) throws Exception {
        Author obj = service.save(author);
        return ResponseEntity.created(URI.create("http://localhost:8080/authors/" + obj.getIdAuthor())).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@RequestBody Author author, @PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(service.update(author, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteById(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
