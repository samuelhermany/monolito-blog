package com.example.blog.web.controller;

import com.example.blog.domain.Author;
import com.example.blog.dto.AuthorRequest;
import com.example.blog.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
class AuthorController {
    private final AuthorService service;
    public AuthorController(AuthorService service){ this.service = service; }

    @GetMapping
    public List<Author> list(){ return service.findAll(); }

    @GetMapping("/{id}")
    public Author get(@PathVariable Long id){ return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@Valid @RequestBody AuthorRequest req){
        Author a = new Author();
        a.setName(req.name()); a.setEmail(req.email()); a.setBio(req.bio());
        return service.create(a);
    }

    @PutMapping("/{id}")
    public Author update(@PathVariable Long id, @Valid @RequestBody AuthorRequest req){
        Author a = new Author();
        a.setName(req.name()); a.setEmail(req.email()); a.setBio(req.bio());
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }
}
