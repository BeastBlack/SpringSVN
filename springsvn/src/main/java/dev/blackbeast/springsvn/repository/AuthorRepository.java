package dev.blackbeast.springsvn.repository;

import dev.blackbeast.springsvn.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();
}
