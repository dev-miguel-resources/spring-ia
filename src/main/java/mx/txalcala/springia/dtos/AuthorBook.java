package mx.txalcala.springia.dtos;

import java.util.List;

public record AuthorBook(
        String author,
        List<String> books) {

}
