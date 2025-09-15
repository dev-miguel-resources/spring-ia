package mx.txalcala.springia.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.txalcala.springia.models.Book;

public interface IBookRepo extends JpaRepository<Book, Integer> {

    // @Query("FROM Book b WHERE b.name LIKE :bookName")
    List<Book> findByNameLike(String name);

}
