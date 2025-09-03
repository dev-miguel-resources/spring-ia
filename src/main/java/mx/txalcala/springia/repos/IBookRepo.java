package mx.txalcala.springia.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.txalcala.springia.models.Book;

public interface IBookRepo extends JpaRepository<Book, Integer> {

}
