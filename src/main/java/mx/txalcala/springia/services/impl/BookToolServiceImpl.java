package mx.txalcala.springia.services.impl;

import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import mx.txalcala.springia.models.Book;
import mx.txalcala.springia.repos.IBookRepo;

@RequiredArgsConstructor
public class BookToolServiceImpl implements Function<BookToolServiceImpl.Request, BookToolServiceImpl.Response> {

    private final IBookRepo repo;

    public record Request(String bookName) {
    }

    public record Response(List<Book> books) {
    }

    @Override
    public Response apply(Request request) {
        List<Book> books = repo.findByNameLike("%" + request.bookName + "%");

        return new Response(books);
    }

}
