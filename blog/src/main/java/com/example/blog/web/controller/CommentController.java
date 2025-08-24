package com.example.blog.web.controller;

import com.example.blog.domain.Comment;
import com.example.blog.dto.CommentCreateRequest;
import com.example.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
class CommentController {
    private final CommentService service;
    public CommentController(CommentService service){ this.service = service; }

    @GetMapping("/posts/{slug}/comments")
    public List<Comment> listByPost(@PathVariable String slug){ return service.listByPostSlug(slug); }

    @PostMapping("/posts/{slug}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment add(@PathVariable String slug, @Valid @RequestBody CommentCreateRequest req){
        return service.addToPost(slug, req.authorName(), req.authorEmail(), req.content());
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }
}
