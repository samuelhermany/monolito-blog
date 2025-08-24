package com.example.blog.web.controller;

import com.example.blog.domain.Category;
import com.example.blog.dto.CategoryRequest;
import com.example.blog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
class CategoryController {
    private final CategoryService service;
    public CategoryController(CategoryService service){ this.service = service; }

    @GetMapping
    public List<Category> list(){ return service.findAll(); }

    @GetMapping("/{id}")
    public Category get(@PathVariable Long id){ return service.findById(id); }

    @GetMapping("/slug/{slug}")
    public Category getBySlug(@PathVariable String slug){ return service.findBySlug(slug); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody CategoryRequest req){
        Category c = new Category();
        c.setName(req.name()); c.setSlug(req.slug());
        return service.create(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @Valid @RequestBody CategoryRequest req){
        Category c = new Category();
        c.setName(req.name()); c.setSlug(req.slug());
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }
}
