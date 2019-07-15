package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Override
    List<Author> findAll();

    @Override
    void delete(Author author);

    List<Author> findAllByAuthorId(String authorId);

    @Override
    @Transactional
    Author save(Author author);
}
