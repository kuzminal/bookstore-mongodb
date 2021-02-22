package com.kuzmin.bookstore.api.controller;

import com.kuzmin.bookstore.db.domain.entity.Author;
import com.kuzmin.bookstore.api.model.AuthorCommonDTO;
import com.kuzmin.bookstore.api.service.AuthorService;
import org.bson.types.ObjectId;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors")
public class AuthorsApiController {
    private final AuthorService authorService;

    public AuthorsApiController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("{id}")
    public AuthorCommonDTO getAuthorById(@PathVariable ObjectId id) {
        return authorService.getAuthorById(id, LocaleContextHolder.getLocale());
    }

    @GetMapping
    public List<AuthorCommonDTO> getAuthors() {
        return authorService.getAllAuthors(LocaleContextHolder.getLocale());
    }
}
