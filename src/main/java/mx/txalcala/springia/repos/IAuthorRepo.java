package mx.txalcala.springia.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.txalcala.springia.models.Author;

public interface IAuthorRepo extends JpaRepository<Author, Integer> {

}
