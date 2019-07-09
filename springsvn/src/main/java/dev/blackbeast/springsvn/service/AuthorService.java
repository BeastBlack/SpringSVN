package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.Author;
import dev.blackbeast.springsvn.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorService {
    private Map<String,String> authors = null;

    @Autowired
    AuthorRepository authorRepository;

    public void refresh() {
        authors = new HashMap<>();

        for(Author author : authorRepository.findAll())
            authors.put(author.getAuthorId(), author.getAuthorName());
    }

    public String getAuthorName(String authorId) {
        if(authors == null)
            refresh();

        String authorName = authors.get(authorId);
        return authorName != null ? authorName : "N/A";
    }
}
