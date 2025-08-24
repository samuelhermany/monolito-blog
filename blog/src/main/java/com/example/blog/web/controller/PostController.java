package com.example.blog.web.controller;

import com.example.blog.domain.PostStatus;
import com.example.blog.dto.PostCreateRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.dto.PostUpdateRequest;
import com.example.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService service;
    public PostController(PostService service){ this.service = service; }

    @GetMapping
    public Page<PostResponse> list(
            @RequestParam(required = false) PostStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) { return service.list(status, page, size); }

    @GetMapping("/{slug}")
    public PostResponse get(@PathVariable String slug){ return service.getBySlug(slug); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@Valid @RequestBody PostCreateRequest req){ return service.create(req); }

    @PutMapping("/{slug}")
    public PostResponse update(@PathVariable String slug, @Valid @RequestBody PostUpdateRequest req){
        return service.update(slug, req);
    }

    @DeleteMapping("/{slug}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String slug){ service.delete(slug); }
}
