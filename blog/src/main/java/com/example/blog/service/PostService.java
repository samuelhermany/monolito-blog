package com.example.blog.service;

import com.example.blog.domain.Author;
import com.example.blog.domain.Category;
import com.example.blog.domain.Post;
import com.example.blog.domain.PostStatus;
import com.example.blog.dto.PostCreateRequest;
import com.example.blog.dto.PostResponse;
import com.example.blog.dto.PostUpdateRequest;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.web.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;

import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepo;
    private final AuthorRepository authorRepo;
    private final CategoryRepository categoryRepo;

    public PostService(PostRepository postRepo, AuthorRepository authorRepo, CategoryRepository categoryRepo) {
        this.postRepo = postRepo;
        this.authorRepo = authorRepo;
        this.categoryRepo = categoryRepo;
    }

    public Page<PostResponse> list(PostStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,  "createdAt" ));
        Page<Post> result = (status != null) ? postRepo.findByStatus(status, pageable) : postRepo.findAll(pageable);
        return result.map(this::toResponse);
    }

    public PostResponse getBySlug(String slug){
        Post p = postRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Post nao encontrado"));
        return toResponse(p);
    }


    @Transactional
    public PostResponse create(PostCreateRequest req){
        Author a = authorRepo.findById(req.authorId()).orElseThrow(() -> new NotFoundException("Autor nao encontrado"));
        Category c = categoryRepo.findById(req.categoryId()).orElseThrow(() -> new NotFoundException("Categoria nao encontrada"));
        String slug = ensureUniqueSlug(slugify(req.title()));


        Post p = new Post();
        p.setTitle(req.title());
        p.setSlug(slug);
        p.setContent(req.content());
        p.setStatus(Optional.ofNullable(req.status()).orElse(PostStatus.DRAFT));
        p.setAuthor(a);
        p.setCategory(c);


        return toResponse(postRepo.save(p));
    }


    @Transactional
    public PostResponse update(String slug, PostUpdateRequest req){
        Post p = postRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Post nao encontrado"));
        p.setTitle(req.title());
        p.setContent(req.content());
        p.setStatus(Optional.ofNullable(req.status()).orElse(PostStatus.DRAFT));
        Category c = categoryRepo.findById(req.categoryId()).orElseThrow(() -> new NotFoundException("Categoria nao encontrada"));
        p.setCategory(c);
        return toResponse(p);
    }


    @Transactional
    public void delete(String slug){
        Post p = postRepo.findBySlug(slug).orElseThrow(() -> new NotFoundException("Post nao encontrado"));
        postRepo.delete(p);
    }


    private PostResponse toResponse(Post p){
        return new PostResponse(
                p.getId(), p.getTitle(), p.getSlug(), p.getContent(),
                p.getAuthor().getName(), p.getCategory().getName(), p.getStatus()
        );
    }


    private String ensureUniqueSlug(String base){
        String candidate = base; int i = 1;
        while (postRepo.findBySlug(candidate).isPresent()) {
            candidate = base + "-" + (++i);
        }
        return candidate;
    }


    private String slugify(String input){
        String noAccents = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return noAccents.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+","-")
                .replaceAll("(^-|-$)", "");
    }
}
