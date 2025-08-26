package com.example.blog.service;

import com.example.blog.domain.Author;
import com.example.blog.repository.AuthorRepository;
import com.example.blog.web.exception.BusinessException;
import com.example.blog.web.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepo;
    public AuthorService(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Author findById(Long id) {
        return authorRepo.findById(id).orElseThrow(() -> new NotFoundException("Autor nao encontrado"));
    }

    @Transactional
    public Author create(Author author) {
        try {
            return authorRepo.save(author);
        } catch (DataIntegrityViolationException e){
            throw new BusinessException("Nao foi possivel salvar o autor (email ja utilizado ou dados invalidos)");
        }
    }

    @Transactional
    public Author update(Long id, Author author){
        Author a = findById(id);
        a.setName(author.getName());
        a.setEmail(author.getEmail());
        a.setBio(author.getBio());
        try {
            return authorRepo.save(a);
        } catch (DataIntegrityViolationException e){
            throw new BusinessException("Nao foi possivel atualizar o autor (email duplicado ou dados invalidos)");
        }
    }

    @Transactional
    public void delete(Long id) {
        Author a = findById(id);
        authorRepo.delete(a);
    }
}
