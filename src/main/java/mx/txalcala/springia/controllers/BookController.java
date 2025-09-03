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
import mx.txalcala.springia.models.Book;
import mx.txalcala.springia.services.IBookService;

@RestController()
@RequestMapping("/books")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookController {

    private final IBookService service;

    @GetMapping
    public ResponseEntity<List<Book>> findAll() throws Exception {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Book> save(@RequestBody Book book) throws Exception {
        Book obj = service.save(book);
        return ResponseEntity.created(URI.create("http://localhost:8080/books/" + obj.getIdBook())).body(obj);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@RequestBody Book book, @PathVariable("id") Integer id) throws Exception {
        return ResponseEntity.ok(service.update(book, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteById(@PathVariable("id") Integer id) throws Exception {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
