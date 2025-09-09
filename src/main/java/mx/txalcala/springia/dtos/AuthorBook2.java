package mx.txalcala.springia.dtos;

import java.util.List;

import mx.txalcala.springia.models.Book;

public record AuthorBook2(
        String author,
        List<Book> books) {

}
