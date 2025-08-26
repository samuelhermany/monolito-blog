package com.example.blog.service;

import com.example.blog.domain.Comment;
import com.example.blog.domain.Post;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.web.exception.NotFoundException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    public CommentService(CommentRepository commentRepo, PostRepository postRepo) {
        this.commentRepo = commentRepo;
        this.postRepo = postRepo;
    }

    public List<Comment> listByPostSlug(String postSlug){
        Post post = postRepo.findBySlug(postSlug).orElseThrow(() -> new NotFoundException("Post nao encontrado"));
        // Lazy-safe dentro da transacao
        return post.getComments();
    }

    @Transactional
    public Comment addToPost(String postSlug,
                             @NotBlank String authorName,
                             @Email String authorEmail,
                             @NotBlank String content){
        Post p = postRepo.findBySlug(postSlug).orElseThrow(() -> new NotFoundException("Post nao encontrado"));
        Comment c = new Comment();
        c.setPost(p);
        c.setAuthorName(authorName);
        c.setAuthorEmail(authorEmail);
        c.setContent(content);
        c.setCreatedAt(LocalDateTime.now());
        return commentRepo.save(c);
    }

    @Transactional
    public void delete(Long commentId){
        Comment c = commentRepo.findById(commentId).orElseThrow(() -> new NotFoundException("Comentario nao encontrado"));
        commentRepo.delete(c);
    }
}
